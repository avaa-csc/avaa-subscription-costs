/**
 * 
 */
package fi.csc.avaa.kuhiti.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;

import com.vaadin.ui.CustomComponent;

import fi.csc.avaa.kuhiti.common.DropdownFilters.DropdownFilterType;
import fi.csc.avaa.tools.Const;
import fi.csc.avaa.tools.vaadin.language.Translator;
import fi.csc.avaa.vaadin.tools.VaadinTools;

/**
 * @author jmlehtin
 *
 */
public class DropdownFilter extends CustomComponent {
	private static final long serialVersionUID = 1L;
	private ComboBoxMultiselect filter;

	public DropdownFilter(DropdownFilterType type, Translator translator) {
		String caption = Const.STRING_EMPTY;
		setImmediate(true);
		addStyleName("filter-dropdown");
		List<String> itemIds = null, itemCaps = null;
		int width = 0;
		
		switch (type) {
		case MATERIAL_TYPE:
			caption = translator.localize("Description.MaterialType");
			itemIds = new ArrayList<>(KuhitiCache.allMaterialTypes);
			itemCaps = KuhitiConst.getTranslations(itemIds, translator, KuhitiConst.TRANSLATION_KEY_PREFIX_MATERIAL_TYPE);
			width = 220;
			setCompositionRoot(filter);
			break;
		case ACQUISITION_WAY:
			caption = translator.localize("Description.AcquisitionWay");
			itemIds = new ArrayList<>(KuhitiCache.allAcquisitionWays);
			itemCaps = KuhitiConst.getTranslations(itemIds, translator, KuhitiConst.TRANSLATION_KEY_PREFIX_ACQUISITION_WAY);
			width = 220;
			break;
		case ORG_NAME:
			caption = translator.localize("Description.OrganisationName");
			itemIds = new ArrayList<>(KuhitiCache.allOrgNames);
			itemCaps = KuhitiConst.getTranslations(itemIds, translator, KuhitiConst.TRANSLATION_KEY_PREFIX_ORGANISATION_NAME);
			width = 220;
			break;
		case YEAR:
			caption = translator.localize("Description.Year");
			itemIds = KuhitiCache.allYears.stream().map(v -> String.valueOf(v)).collect(Collectors.toList());
			itemCaps = itemIds;
			width = 150;
			break;
		case ORG_TYPE:
			caption = translator.localize("Description.OrganisationType");
			itemIds = new ArrayList<>(KuhitiCache.allOrgTypes);
			itemCaps = KuhitiConst.getTranslations(itemIds, translator, KuhitiConst.TRANSLATION_KEY_PREFIX_ORGANISATION_TYPE);
			width = 220;
			break;
		default:
			break;
		}
		
		filter = VaadinTools.createMultiselectComboBox(itemIds, itemCaps, translator.localize("Search.NoSelection"), width);
		filter.setItemCaption(Const.STRING_EMPTY, translator.localize(KuhitiConst.TRANSLATION_KEY_NOT_CHOSEN));
		filter.setNewItemsAllowed(false);
		setCaption(caption);
		setCompositionRoot(filter);
	}

	public ComboBoxMultiselect getFilter() {
		return filter;
	}
	
	public void resetFilter() {
		filter.setValue(new TreeSet<String>());
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> getFilterValue() {
		if(filter.getValue() != null) {
			return (Set<String>) filter.getValue();
		}
		return null;
	}
}