package fi.csc.avaa.kuhiti.model;

import java.util.Set;


public class SubscriptionCost {

	private String orgType;
	private String orgName;
	private String publisher;
	private int year;
	private double price;
	private Set<String> materials;
	private Set<String> materialTypes;
	private Set<String> acquisitionWays;
	
	public SubscriptionCost(String orgType, String orgName, String publisher,
			int year, double price, Set<String> materials, Set<String> materialTypes, 
			Set<String> acquisitionWays) {
		super();
		this.orgType = orgType;
		this.orgName = orgName;
		this.publisher = publisher;
		this.year = year;
		this.price = price;
		this.materials = materials;
		this.materialTypes = materialTypes;
		this.acquisitionWays = acquisitionWays;
	}

	public SubscriptionCost() {
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Set<String> getMaterials() {
		return materials;
	}

	public void setMaterials(Set<String> materials) {
		this.materials = materials;
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

	public void setAcquisitionWays(Set<String> acquisitionWays) {
		this.acquisitionWays = acquisitionWays;
	}

}
