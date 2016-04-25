/**
 * 
 */
package fi.csc.avaa.kuhiti.results;

import java.util.List;
import java.util.Set;


/**
 * @author jmlehtin
 *
 */
public class SubscriptionCostGridBean {

	private String orgType;
	private String orgName;
	private String publisher;
	private String year;
	private double price;
	private Set<String> materials;
	private Set<String> materialTypes;
	private Set<String> acquisitionWays;
	
	public SubscriptionCostGridBean(
			String orgType,
			String orgName, 
			String publisher,
			int year,
			double price,
			Set<String> materials,
			Set<String> materialTypes,
			Set<String> acquisitionWays) {
		
		this.orgType = orgType;
		this.orgName = orgName;
		this.publisher = publisher;
		this.year = String.valueOf(year);
		this.price = price;
		this.materials = materials;
		this.materialTypes = materialTypes;
		this.acquisitionWays = acquisitionWays;
	}
	
	public enum FieldName {
		ORG_TYPE("orgType"),
		ORG_NAME("orgName"),
		PUBLISHER("publisher"),
		YEAR("year"),
		PRICE("price"),
		MATERIALS("materials"),
		MATERIAL_TYPES("materialTypes"),
		ACQUISITION_WAYS("acquisitionWays");
		
		private String value;
		
		private FieldName(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public static FieldName fromValue(String name) {
			if(name == null || name.isEmpty()) {
				return null;
			}
			for(FieldName columnType : FieldName.values()) {
				String col = columnType.getValue();
				if(col.equals(name)) {
					return columnType;
				}
			}
			return null;
		}
	}
	
	public enum GridColumnName {
		ORG_TYPE("Description.OrganisationType"),
		ORG_NAME("Description.OrganisationName"),
		PUBLISHER("Description.Publisher"),
		YEAR("Description.Year"),
		PRICE("Description.PriceWithEuro");
		
		String value;
		
		private GridColumnName(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}	
		
		public static GridColumnName getValuefromName(String name) {
			if(name == null || name.isEmpty()) {
				return null;
			}
			for(GridColumnName columnType : GridColumnName.values()) {
				String col = columnType.name();
				if(col.equals(name)) {
					return columnType;
				}
			}
			return null;
		}
	}
	
	public String getOrgType() {
		return orgType;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getYear() {
		return year;
	}

	public double getPrice() {
		return price;
	}

	public Set<String> getMaterials() {
		return materials;
	}

	public Set<String> getMaterialTypes() {
		return materialTypes;
	}

	public Set<String> getAcquisitionWays() {
		return acquisitionWays;
	}

}
