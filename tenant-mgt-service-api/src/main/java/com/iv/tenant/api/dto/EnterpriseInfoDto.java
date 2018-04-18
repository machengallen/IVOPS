package com.iv.tenant.api.dto;

import java.util.Set;

public class EnterpriseInfoDto {

	private int id;
	private String name;
	private String identifier;// 租户号，对外租户唯一识别号
	private String tenantId;// 租户id，系统内部使用，对应租户db名
	private String orgCode;
	private String address;
	private String industry;
	private String businessInvolves;
	private String staffSize;
	private Set<Integer> userIds;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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
	public Set<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(Set<Integer> userIds) {
		this.userIds = userIds;
	}
	
}
