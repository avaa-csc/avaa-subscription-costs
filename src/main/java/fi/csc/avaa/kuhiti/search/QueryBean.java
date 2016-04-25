/**
 * 
 */
package fi.csc.avaa.kuhiti.search;

import java.util.HashSet;
import java.util.Set;

import fi.csc.avaa.tools.search.SearchBean;
import fi.csc.avaa.tools.vaadin.language.Translator;

/**
 * @author jmlehtin
 *
 */
public class QueryBean extends SearchBean {

	private String searchStr;
	private Set<String> orgTypes;
	private Set<String> orgNames;
	private Set<Integer> years;
	private Set<String> materialTypes;
	private Set<String> acquisitionWays;
	
	public QueryBean(String searchStr) {
		this.searchStr = searchStr;
		this.orgTypes = new HashSet<>();
		this.orgNames = new HashSet<>();
		this.years = new HashSet<>();
		this.materialTypes = new HashSet<>();
		this.acquisitionWays = new HashSet<>();
	}
	
	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	@Override
	public String getFieldsAsHtml(Translator translator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFieldsAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getOrgTypes() {
		return orgTypes;
	}

	public void setOrgTypes(Set<String> orgTypes) {
		this.orgTypes = orgTypes;
	}

	public Set<String> getOrgNames() {
		return orgNames;
	}

	public void setOrgNames(Set<String> orgNames) {
		this.orgNames = orgNames;
	}

	public Set<Integer> getYears() {
		return years;
	}

	public void setYears(Set<Integer> years) {
		this.years = years;
	}

	public Set<String> getMaterialTypes() {
		return materialTypes;
	}

	public void setMaterialTypes(Set<String> materialTypes) {
		this.materialTypes = materialTypes;
	}

	public Set<String> getAcquisitionWays() {
		return acquisitionWays;
	}

	public void setAcquisitionWay(Set<String> acquisitionWays) {
		this.acquisitionWays = acquisitionWays;
	}
	
	public static boolean isEmptySet(Set<?> set) {
		return set == null ? true : set.isEmpty();
	}
	
}
