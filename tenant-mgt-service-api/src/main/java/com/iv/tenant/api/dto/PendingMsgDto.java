package com.iv.tenant.api.dto;

public class PendingMsgDto {

	private long applyTime;// 申请时间
	private String applicant;// 申请人
	private String enterprise;// 企业名称
	private String subEnterprise;// 项目组名称
	
	public long getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(long applyTime) {
		this.applyTime = applyTime;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	public String getSubEnterprise() {
		return subEnterprise;
	}
	public void setSubEnterprise(String subEnterprise) {
		this.subEnterprise = subEnterprise;
	}
	
}
