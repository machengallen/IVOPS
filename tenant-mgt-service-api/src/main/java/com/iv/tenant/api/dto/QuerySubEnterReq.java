package com.iv.tenant.api.dto;

public class QuerySubEnterReq {

	private int id;
	private String name;
	private String subIdentifier;// 项目组标识符,唯一
	private String tenantId;// 租户id,隔离租户数据,DB名
	private int enterpriseId;
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
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public int getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
}
