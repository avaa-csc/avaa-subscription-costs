package fi.csc.avaa.kuhiti;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

import com.vaadin.server.*;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fi.csc.avaa.kuhiti.common.DropdownFilters;
import fi.csc.avaa.kuhiti.common.HelpWindow;
import fi.csc.avaa.kuhiti.common.KuhitiCache;
import fi.csc.avaa.kuhiti.common.SearchField;
import fi.csc.avaa.kuhiti.csv.CSVTools;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.kuhiti.results.GridControlRow;
import fi.csc.avaa.kuhiti.results.SubscriptionCostGrid;
import fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean;
import fi.csc.avaa.kuhiti.search.QueryBean;
import fi.csc.avaa.kuhiti.search.SubscriptionCostSearchTools;
import fi.csc.avaa.tools.search.result.ResultGridWrapper;
import fi.csc.avaa.tools.vaadin.customcomponent.HorizontalKeyValueComponent;
import fi.csc.avaa.tools.vaadin.customcomponent.TwoLanguageButtons;
import fi.csc.avaa.tools.vaadin.language.LanguageChangeEvent;
import fi.csc.avaa.tools.vaadin.language.LanguageChangeListener;
import fi.csc.avaa.tools.vaadin.language.LanguageConst;
import fi.csc.avaa.tools.vaadin.language.Translator;
import fi.csc.avaa.vaadin.tools.VaadinTools;

import static fi.csc.avaa.kuhiti.common.KuhitiConst.*;

public class MainView extends CustomComponent implements Listener, LanguageChangeListener {

	private static final long serialVersionUID = 1L;

	private SearchField searchField;
	private SubscriptionCostSearchTools searchTools;
	private ResultGridWrapper<SubscriptionCostGridBean, SubscriptionCost> gridWrapper;

	private Translator translator;
	private VerticalLayout viewLayout;
	private DropdownFilters dropdownFilters;

	private CSVTools csvTools;

	public MainView(Translator translator) {
		this.translator = translator;
		viewLayout = new VerticalLayout();
		viewLayout.addStyleName("main-layout");
		setSizeFull();
		viewLayout.setSizeFull();
		viewLayout.setMargin(new MarginInfo(false, true, true, true));
		viewLayout.setImmediate(true);
		viewLayout.setResponsive(true);
		setCompositionRoot(viewLayout);
		searchTools = new SubscriptionCostSearchTools(this.translator);
		init();
	}

	private void init() {
		HorizontalLayout descRow = new HorizontalLayout();
		descRow.setSizeFull();

		Label descLabel = new Label(translator.localize("Application.Description"), ContentMode.HTML);
		TwoLanguageButtons langBtns = new TwoLanguageButtons(LanguageConst.LOCALE_FI, LanguageConst.LOCALE_EN,
				translator, false);
		langBtns.addLanguageChangeListener(this);

		descRow.addComponents(descLabel, langBtns);
		descRow.setExpandRatio(descLabel, 0.65f);
		descRow.setExpandRatio(langBtns, 0.35f);
		descRow.setComponentAlignment(langBtns, Alignment.MIDDLE_CENTER);
		viewLayout.addComponent(descRow);

		HorizontalLayout downloadButtonsLayout = new HorizontalLayout();
		downloadButtonsLayout.addComponents(createXLSXFullDataDownloadLink(), createCSVFullDataDownloadLink());
		HorizontalKeyValueComponent downdloadButtonsAndKeyLayout = new HorizontalKeyValueComponent(translator.localize
				("Download.Text"), 3.0f, downloadButtonsLayout, 3.0f, false, null, null, null);
		downdloadButtonsAndKeyLayout.setSizeUndefined();
//		downloadButtonsLayout.setMargin(new MarginInfo(true, true, true, true));
		viewLayout.addComponent(downdloadButtonsAndKeyLayout);

		HorizontalLayout searchBarRow = new HorizontalLayout();
		searchBarRow.setSizeFull();
		searchBarRow.setMargin(false);
		searchBarRow.setSpacing(true);

		VerticalLayout searchFieldLayout = createSearchFieldLayout();
		Button resetBtn = getSearchResetButton();
		Label helpLabel = new Label(FontAwesome.QUESTION_CIRCLE.getHtml(), ContentMode.HTML);
		HorizontalLayout helpClickLayout = new HorizontalLayout(helpLabel);
		helpClickLayout.addStyleName("help-layout");
		helpClickLayout.addLayoutClickListener(e -> {
			HelpWindow helpWindow = new HelpWindow(translator, 600, 500, true, false, true);
			UI.getCurrent().addWindow(helpWindow);
			helpWindow.focus();
		});

		searchBarRow.addComponents(searchFieldLayout, resetBtn, helpClickLayout);
		searchBarRow.setComponentAlignment(helpClickLayout, Alignment.MIDDLE_CENTER);
		viewLayout.addComponents(searchBarRow);

		dropdownFilters = new DropdownFilters(translator);
		dropdownFilters.addListener(this);
		VerticalLayout dropdownLayout = new VerticalLayout(dropdownFilters);
		dropdownLayout.setMargin(new MarginInfo(true, false, true, false));
		viewLayout.addComponents(dropdownLayout);
		initResultGrid();
		updateResults();
		viewLayout.addComponent(gridWrapper);
	}

	private Button getSearchResetButton() {
		NativeButton resetBtn = VaadinTools.createLinkNativeButton(translator.localize("Search.Reset"), null, null,
				"reset-search borderless");
		resetBtn.addClickListener(e -> {
			dropdownFilters.resetFilters();
			searchField.reset();
			gridWrapper.getCurrentGrid().populateGrid(KuhitiCache.getDataCache());
			gridWrapper.getCurrentControlRow().createNewContents(KuhitiCache.getDataCache(), null);
			searchTools.setSearchResults(KuhitiCache.getDataCache().stream().collect(Collectors.toList()));
		});
		resetBtn.setIcon(FontAwesome.TRASH);
		return resetBtn;
	}

	private Button createXLSXFullDataDownloadLink() {
		Button downloadLink = VaadinTools.createLinkNativeButton(XLSL_DOWNLOAD_FILE_EXTENTION, FontAwesome
				.DOWNLOAD, null, "download-link-csv borderless");
		FileDownloader downloader = new FileDownloader(new StreamResource(new StreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				return MainView.class.getResourceAsStream(DATAFILE_PATH);
			}
		}, translator.localize(DOWNLOAD_TEXT_FILENAME) + XLSL_DOWNLOAD_FILE_EXTENTION));
		downloader.extend(downloadLink);
		return downloadLink;
	}

	private Button createCSVFullDataDownloadLink() {
		csvTools = new CSVTools(translator, false);
		Button downloadLink = VaadinTools.createLinkNativeButton(CSV_DOWNLOAD_FILE_EXTENTION, FontAwesome
				.DOWNLOAD, null, "download-link-csv borderless");

		FileDownloader downloader = new FileDownloader(new StreamResource(null, "")) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws
					IOException {
				setFileDownloadResource(csvTools.getSubsciptionCostCSVResource(KuhitiCache.getDataCache(),
						HEADERS_LIST, DOWNLOAD_TEXT_FILENAME));
				return super.handleConnectorRequest(request, response, path);
			}
		};
		downloader.extend(downloadLink);
		return downloadLink;
	}

	private Button createCSVDownloadLink() {
		csvTools = new CSVTools(translator, false);
		Button downloadLink = VaadinTools.createLinkNativeButton(CSV_DOWNLOAD_FILE_EXTENTION, FontAwesome
				.DOWNLOAD, null, "download-link-csv borderless");

		FileDownloader downloader = new FileDownloader(new StreamResource(null, "")) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws
					IOException {
				setFileDownloadResource(csvTools.getSubsciptionCostCSVResource(searchTools.getSearchResults(),
						HEADERS_LIST, "Download.Filtered.Filename"));
				return super.handleConnectorRequest(request, response, path);
			}
		};
		downloader.extend(downloadLink);
		return downloadLink;
	}

	private VerticalLayout createSearchFieldLayout() {
		searchField = new SearchField("search-input", translator, 550);
		searchField.addListener(e -> {
			updateResults();
		});
		VerticalLayout searchFieldLayout = new VerticalLayout(searchField);
		searchFieldLayout.setSpacing(true);
		searchFieldLayout.addComponents(searchField);
		return searchFieldLayout;
	}

	private void updateResults() {
		String value = searchField.getValue();
		QueryBean queryBean = new QueryBean(value);
		queryBean.setOrgTypes(dropdownFilters.getOrgTypeValues());
		queryBean.setOrgNames(dropdownFilters.getOrgNameValues());
		queryBean.setYears(dropdownFilters.getYearValues());
		queryBean.setMaterialTypes(dropdownFilters.getMaterialTypeValues());
		queryBean.setAcquisitionWay(dropdownFilters.getAcquistionWayValues());
		updateGrid(queryBean);
	}

	@Override
	public void componentEvent(Event event) {
		updateResults();
	}

	public void updateGrid(QueryBean queryBean) {
		searchTools.queryData(KuhitiCache.getDataCache(), queryBean);
		gridWrapper.getCurrentGrid().populateGrid(searchTools.getSearchResults());
		gridWrapper.getCurrentControlRow().createNewContents(searchTools.getSearchResults(), queryBean);
	}

	private void initResultGrid() {
		SubscriptionCostGrid grid = new SubscriptionCostGrid(translator);
		GridControlRow resultControlRow = new GridControlRow(translator, createCSVDownloadLink());
		gridWrapper = new ResultGridWrapper<>(grid, resultControlRow);
//		gridWrapper.setMargin(new MarginInfo(false, false, true, false));
		gridWrapper.setWidth(80, Unit.PERCENTAGE);
	}

	@Override
	public void changeComponentLanguage(LanguageChangeEvent e) {
		VaadinSession.getCurrent().setLocale(e.getLocale());
		JavaScript.eval("window.location.reload();");
	}
}
