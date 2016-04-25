/**
 * 
 */
package fi.csc.avaa.kuhiti.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import fi.csc.avaa.kuhiti.common.KuhitiConst;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.tools.Const;
import fi.csc.avaa.tools.StringTools;
import fi.csc.avaa.tools.search.Searcher;
import fi.csc.avaa.tools.vaadin.language.Translator;

/**
 * @author jmlehtin
 *
 */
public final class SubscriptionCostSearchTools extends Searcher<SubscriptionCost, QueryBean> {
	
	private Translator translator;
	
	public SubscriptionCostSearchTools(Translator translator) {
		this.translator = translator;
	}
	
	@Override
	public void queryData(Collection<SubscriptionCost> searchDataset, QueryBean queryParams) {
		if(queryParams != null && searchDataset != null) {
			String searchStr = (queryParams.getSearchStr() == null) ? null : queryParams.getSearchStr().toLowerCase();
			
			Predicate<SubscriptionCost> resultFilter = new Predicate<SubscriptionCost>() {
				
				@Override
				public boolean test(SubscriptionCost sc) {
					if (!QueryBean.isEmptySet(queryParams.getOrgTypes()) && !stringSetItemEqualsString(queryParams.getOrgTypes(), sc.getOrgType())) {
						return false;
					}
					if (!QueryBean.isEmptySet(queryParams.getOrgNames()) && !stringSetItemEqualsString(queryParams.getOrgNames(), sc.getOrgName())) {
						return false;
					}
					if (!QueryBean.isEmptySet(queryParams.getYears()) && !integerSetItemEqualsInt(queryParams.getYears(), sc.getYear())) {
						return false;
					}
					if (!QueryBean.isEmptySet(queryParams.getMaterialTypes()) && !stringSetContainsItemInStringSet(queryParams.getMaterialTypes(), sc.getMaterialTypes())) {
						return false;
					}
					
					if (!QueryBean.isEmptySet(queryParams.getAcquisitionWays()) && !stringSetContainsItemInStringSet(queryParams.getAcquisitionWays(), sc.getAcquisitionWays())) {
						return false;
					}
					return (searchStr == null || searchStr.equals(Const.STRING_EMPTY)) ? true: isWordPresent(searchStr, sc);
				}
			};
			searchResults = searchDataset.stream().filter(resultFilter).collect(Collectors.toList());
		} else {
			searchResults = new ArrayList<>();
		}
	}
	
	private boolean isWordPresent(String searchStr, SubscriptionCost sc) {
		String orgTypeKey = StringTools.getStringOrEmptyValue(sc.getOrgType());
		String orgType = StringTools.isEmptyOrNull(orgTypeKey) ? orgTypeKey : translator.localize(KuhitiConst.TRANSLATION_KEY_PREFIX_ORGANISATION_TYPE + orgTypeKey);
		String orgNameKey = StringTools.getStringOrEmptyValue(sc.getOrgName());
		String orgName = StringTools.isEmptyOrNull(orgNameKey) ? orgNameKey : translator.localize(KuhitiConst.TRANSLATION_KEY_PREFIX_ORGANISATION_NAME + orgNameKey);
		Set<String> materialTypes = KuhitiConst.getTranslations(sc.getMaterialTypes(), translator, KuhitiConst.TRANSLATION_KEY_PREFIX_MATERIAL_TYPE);
		Set<String> acquisitionWays = KuhitiConst.getTranslations(sc.getAcquisitionWays(), translator, KuhitiConst.TRANSLATION_KEY_PREFIX_ACQUISITION_WAY);
		if (	doesFieldContainValue(searchStr, orgType) ||
				doesFieldContainValue(searchStr, orgName) ||
				doesFieldContainValue(searchStr, sc.getPublisher()) ||
				doesFieldContainValue(searchStr, String.valueOf(sc.getYear())) ||
				stringSetItemContainsStringIgnoreCase(searchStr, sc.getMaterials()) ||
				stringSetItemContainsStringIgnoreCase(searchStr, materialTypes) ||
				stringSetItemContainsStringIgnoreCase(searchStr, acquisitionWays)) {
			return true;
		}
		return false;
	}
	
	private boolean stringSetContainsItemInStringSet(Set<String> valuesToSearch, Set<String> searchFromStrings) {
		if(searchFromStrings == null || valuesToSearch == null) {
			return false;
		}
		return valuesToSearch.stream().anyMatch(v -> searchFromStrings.contains(v));
	}
	
	private boolean stringSetItemEqualsString(Set<String> valuesToSearch, String searchFromStr) {
		if(valuesToSearch == null || searchFromStr == null) {
			return false;
		}
		return valuesToSearch.stream().anyMatch(v -> v.equals(searchFromStr));
	}
	
	private boolean stringSetItemContainsStringIgnoreCase(String searchFromStr, Set<String> valuesToSearch) {
		if(valuesToSearch == null || searchFromStr == null) {
			return false;
		}
		return valuesToSearch.stream().anyMatch(v -> v.toLowerCase().contains(searchFromStr.toLowerCase()));
	}
	
	private boolean integerSetItemEqualsInt(Set<Integer> valuesToSearch, int searchInt) {
		if(valuesToSearch == null || searchInt == 0) {
			return false;
		}
		return valuesToSearch.stream().anyMatch(v -> searchInt == v);
	}
}
