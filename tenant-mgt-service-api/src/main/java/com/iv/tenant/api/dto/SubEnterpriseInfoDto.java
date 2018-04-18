package com.iv.tenant.api.dto;

import java.util.Set;

public class SubEnterpriseInfoDto {

	private int id;
	private String name;
	private String subIdentifier;// 项目组标识符
	private String subTenantId;//　子租户id
	private EnterpriseInfoDto enterprise;
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
	public String getSubIdentifier() {
		return subIdentifier;
	}
	public void setSubIdentifier(String subIdentifier) {
		this.subIdentifier = subIdentifier;
	}
	public String getSubTenantId() {
		return subTenantId;
	}
	public void setSubTenantId(String subTenantId) {
		this.subTenantId = subTenantId;
	}
	public Set<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(Set<Integer> userIds) {
		this.userIds = userIds;
	}
	public EnterpriseInfoDto getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(EnterpriseInfoDto enterprise) {
		this.enterprise = enterprise;
	}
	
}
