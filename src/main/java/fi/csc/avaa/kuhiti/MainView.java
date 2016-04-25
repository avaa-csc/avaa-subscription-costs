package fi.csc.avaa.kuhiti;

import java.io.InputStream;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinSession;
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
import fi.csc.avaa.kuhiti.common.KuhitiConst;
import fi.csc.avaa.kuhiti.common.SearchField;
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

public class MainView extends CustomComponent implements Listener, LanguageChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	private SearchField searchField;
	private SubscriptionCostSearchTools searchTools;
	private ResultGridWrapper<SubscriptionCostGridBean, SubscriptionCost> gridWrapper;
	
	private Translator translator;
	private VerticalLayout viewLayout;
	private DropdownFilters dropdownFilters;

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
		TwoLanguageButtons langBtns = new TwoLanguageButtons(LanguageConst.LOCALE_FI, LanguageConst.LOCALE_EN, translator, false);
		langBtns.addLanguageChangeListener(this);
		
		descRow.addComponents(descLabel, langBtns);
		descRow.setExpandRatio(descLabel, 0.65f);
		descRow.setExpandRatio(langBtns, 0.35f);
		descRow.setComponentAlignment(langBtns, Alignment.MIDDLE_CENTER);
		viewLayout.addComponent(descRow);
		viewLayout.addComponent(createDownloadLink());
		
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
		NativeButton resetBtn = VaadinTools.createLinkNativeButton(translator.localize("Search.Reset"), null, null, "reset-search borderless");
		resetBtn.addClickListener(e -> {
			dropdownFilters.resetFilters();
			searchField.reset();
			gridWrapper.getCurrentGrid().populateGrid(KuhitiCache.getDataCache());
			gridWrapper.getCurrentControlRow().createNewContents(KuhitiCache.getDataCache(), null);
		});
		resetBtn.setIcon(FontAwesome.TRASH);
		return resetBtn;
	}

	private Button createDownloadLink() {
		Button downloadLink = VaadinTools.createLinkNativeButton(translator.localize("Download.Text"), FontAwesome.DOWNLOAD, null, "download-link borderless");
		FileDownloader downloader = new FileDownloader(new StreamResource(new StreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				return MainView.class.getResourceAsStream(KuhitiConst.DATAFILE_PATH);
			}
		}, KuhitiConst.XLSX_DOWNLOAD_FILE_NAME));
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
		GridControlRow resultControlRow = new GridControlRow(translator, false, false, false);
		gridWrapper = new ResultGridWrapper<>(grid, resultControlRow);
		gridWrapper.setMargin(new MarginInfo(true, false, true, false));
		gridWrapper.setWidth(80, Unit.PERCENTAGE);
	}

	@Override
	public void changeComponentLanguage(LanguageChangeEvent e) {
		VaadinSession.getCurrent().setLocale(e.getLocale());
		JavaScript.eval("window.location.reload();");
	}
}
