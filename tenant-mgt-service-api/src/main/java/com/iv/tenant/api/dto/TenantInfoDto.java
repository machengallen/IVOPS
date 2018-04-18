package com.iv.tenant.api.dto;

import java.io.Serializable;

public class TenantInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7235014165243176969L;
	private String name;
	private String identifier;
	private String orgCode;
	private String address;
	private String industry;
	private String businessInvolves;
	private String staffSize;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIndustry() {
		return industry;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getBusinessInvolves() {
		return businessInvolves;
	}
	public void setBusinessInvolves(String businessInvolves) {
		this.businessInvolves = businessInvolves;
	}
	public String getStaffSize() {
		return staffSize;
	}
	public void setStaffSize(String staffSize) {
		this.staffSize = staffSize;
	}
	
}
