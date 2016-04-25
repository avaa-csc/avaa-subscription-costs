package fi.csc.avaa.kuhiti.common;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import fi.csc.avaa.tools.vaadin.language.Translator;

public class DropdownFilters extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private Translator translator;
	private HashMap<DropdownFilterType, DropdownFilter> filters;
	
	public DropdownFilters(Translator translator) {
		this.translator = translator;
		filters = new HashMap<>();
		init();
	}
	
	public enum DropdownFilterType {
		ORG_TYPE,
		ORG_NAME,
		YEAR,
		MATERIAL_TYPE,
		ACQUISITION_WAY;
	}

	public Set<String> getOrgTypeValues() {
		return filters.get(DropdownFilterType.ORG_TYPE).getFilterValue();
	}
	
	public Set<String> getOrgNameValues() {
		return filters.get(DropdownFilterType.ORG_NAME).getFilterValue();
	}
	
	public Set<Integer> getYearValues() {
		Set<String> yearsAsStr = filters.get(DropdownFilterType.YEAR).getFilterValue();
		Set<Integer> years = new TreeSet<>();
		if(yearsAsStr != null) {
			yearsAsStr.stream().forEach(y -> years.add(Integer.parseInt(y)));
			return years;
		}
		return null;
	}

	public Set<String> getMaterialTypeValues() {
		return filters.get(DropdownFilterType.MATERIAL_TYPE).getFilterValue();
	}

	public Set<String> getAcquistionWayValues() {
		return filters.get(DropdownFilterType.ACQUISITION_WAY).getFilterValue();
	}
	
	public void resetFilters() {
		filters.forEach((type, filter) -> filter.resetFilter());
	}

	private void init() {
		filters = new HashMap<>();
		CssLayout filtersLayout = new CssLayout();
		filtersLayout.addStyleName("filters-layout");
		filtersLayout.setSizeFull();
		for(DropdownFilterType filterType : DropdownFilterType.values()) {
			DropdownFilter filter = new DropdownFilter(filterType, translator);
			filter.getFilter().addValueChangeListener(e -> {
				fireComponentEvent();
			});
			filter.setSizeUndefined();
			VerticalLayout filterLayout = new VerticalLayout();
			filterLayout.addStyleName("filter-layout");
			filterLayout.setSizeUndefined();
			filterLayout.addComponent(filter);
			filters.put(filterType, filter);
			filtersLayout.addComponent(filterLayout);
		}
		setCompositionRoot(filtersLayout);
	}

}
