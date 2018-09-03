package com.iv.dto;

public class MsgDto {

	private String formId;
	private String demandContent;
	private String formState;
	private String tenantId;
	private String tenantName;
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getDemandContent() {
		return demandContent;
	}
	public void setDemandContent(String demandContent) {
		this.demandContent = demandContent;
	}
	public String getFormState() {
		return formState;
	}
	public void setFormState(String formState) {
		this.formState = formState;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	
}
