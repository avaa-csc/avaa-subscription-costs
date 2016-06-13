package fi.csc.avaa.kuhiti;

import com.vaadin.server.*;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Component.Listener;
import fi.csc.avaa.kuhiti.common.*;
import fi.csc.avaa.kuhiti.csv.CSVTools;
import fi.csc.avaa.kuhiti.logging.AvaaStatisticsLogger;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.kuhiti.results.GridControlRow;
import fi.csc.avaa.kuhiti.results.SubscriptionCostGrid;
import fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean;
import fi.csc.avaa.kuhiti.search.QueryBean;
import fi.csc.avaa.kuhiti.search.SubscriptionCostSearchTools;
import fi.csc.avaa.tools.search.result.ResultGridWrapper;
import fi.csc.avaa.tools.vaadin.customcomponent.TwoLanguageButtons;
import fi.csc.avaa.tools.vaadin.language.LanguageChangeEvent;
import fi.csc.avaa.tools.vaadin.language.LanguageChangeListener;
import fi.csc.avaa.tools.vaadin.language.LanguageConst;
import fi.csc.avaa.tools.vaadin.language.Translator;
import fi.csc.avaa.vaadin.tools.VaadinTools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

	private static AvaaStatisticsLogger statLog = new AvaaStatisticsLogger(MainView.class.getName());

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
		TwoLanguageButtons langBtns = new TwoLanguageButtons(LanguageConst.LOCALE_EN, LanguageConst.LOCALE_FI,
				translator, false);
		langBtns.addLanguageChangeListener(this);

		descRow.addComponents(descLabel, langBtns);
		descRow.setExpandRatio(descLabel, 2.0f);
		descRow.setExpandRatio(langBtns, 0.35f);
		descRow.setComponentAlignment(langBtns, Alignment.MIDDLE_RIGHT);
		viewLayout.addComponent(descRow);

		HorizontalLayout labelAndDownloadButtonsLayout = new HorizontalLayout();
		labelAndDownloadButtonsLayout.setMargin(new MarginInfo(true, false, true, false));
		labelAndDownloadButtonsLayout.setSpacing(true);
		labelAndDownloadButtonsLayout.setResponsive(true);
		labelAndDownloadButtonsLayout.addComponents(new Label(translator.localize("Download.Text")), createXLSXFullDataDownloadLink
				(), new Label(translator.localize("Download.Text.Or")), createCSVFullDataDownloadLink());
		viewLayout.addComponent(labelAndDownloadButtonsLayout);

		CssLayout searchBarRow = new CssLayout();
		searchBarRow.setSizeFull();
		SearchField searchField = createSearchField();
		searchField.setSizeUndefined();
		Button resetBtn = getSearchResetButton();
		resetBtn.addStyleName("reset-btn");
		Button helpBtn = getHelpButton();
		helpBtn.addStyleName("help-btn");
		searchBarRow.addComponents(searchField, resetBtn, helpBtn);
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
		NativeButton resetBtn = VaadinTools.createLinkNativeButton(translator.localize("Search.Reset"), FontAwesome.TRASH, null,
				"reset-search borderless");
		resetBtn.addClickListener(e -> {
			dropdownFilters.resetFilters();
			searchField.reset();
			gridWrapper.getCurrentGrid().populateGrid(KuhitiCache.getDataCache());
			gridWrapper.getCurrentControlRow().createNewContents(KuhitiCache.getDataCache(), null);
			searchTools.setSearchResults(KuhitiCache.getDataCache().stream().collect(Collectors.toList()));
		});
		return resetBtn;
	}

	private Button getHelpButton() {
		NativeButton helpBtn = VaadinTools.createLinkNativeButton(translator.localize("Help.Description"), FontAwesome.QUESTION_CIRCLE, null,
				"help-layout borderless");
		helpBtn.addClickListener(e -> {
			HelpWindow helpWindow = new HelpWindow(translator, 600, 500, true, false, true);
			UI.getCurrent().addWindow(helpWindow);
			helpWindow.focus();
		});
		return helpBtn;
	}


	private Button createXLSXFullDataDownloadLink() {
		Button downloadLink = VaadinTools.createLinkNativeButton(XLSL_DOWNLOAD_FILE_EXTENTION, FontAwesome
				.DOWNLOAD, null, "download-link-csv borderless");
		FileDownloader downloader = new FileDownloader(new StreamResource(new StreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				long fileSize = 0L;
				try {
					Path file = Paths.get(getClass().getClassLoader()
							.getResource(DATAFILE_PATH).toURI());
					fileSize = Files.size(file);
				} catch (IOException | URISyntaxException e) {
//					e.printStackTrace();
				}
				statLog.logDownloadEvent(APPLICATION_NAME, DOWNLOAD_FILE_FULL_EXCEL, fileSize, LocalDateTime.now());
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
				statLog.logDownloadEvent(APPLICATION_NAME, DOWNLOAD_FILE_FULL_CSV, LocalDateTime.now());
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
				statLog.logDownloadEvent(APPLICATION_NAME, DOWNLOAD_FILE_FILTERED_CSV, LocalDateTime.now());
				setFileDownloadResource(csvTools.getSubsciptionCostCSVResource(searchTools.getSearchResults(),
						HEADERS_LIST, "Download.Filtered.Filename"));
				return super.handleConnectorRequest(request, response, path);
			}
		};
		downloader.extend(downloadLink);
		return downloadLink;
	}

	private SearchField createSearchField() {
		searchField = new SearchField("search-input", translator, 550);
		searchField.addListener(e -> {
			updateResults();
		});
		return searchField;
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
		Label csvDlLabel = new Label(translator.localize("Download.Filtered"));
		csvDlLabel.addStyleName("filtered-csv-download-label");
		GridControlRow resultControlRow = new GridControlRow(translator, csvDlLabel, createCSVDownloadLink());
		resultControlRow.setMargin(new MarginInfo(false, false, true, false));
		resultControlRow.setStyleName("subscriptioncost-result-control-row");
		gridWrapper = new ResultGridWrapper<>(grid, resultControlRow);
		gridWrapper.setSizeFull();
	}

	@Override
	public void changeComponentLanguage(LanguageChangeEvent e) {
		VaadinSession.getCurrent().setLocale(e.getLocale());
		JavaScript.eval("window.location.reload();");
	}
}
