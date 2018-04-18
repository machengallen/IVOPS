package com.iv.tenant.api.dto;

public class EnterNameListResp {

	private String name;
	private String tenantId;
	
	public EnterNameListResp(String name, String tenantId) {
		super();
		this.name = name;
		this.tenantId = tenantId;
	}
	public EnterNameListResp() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
}
