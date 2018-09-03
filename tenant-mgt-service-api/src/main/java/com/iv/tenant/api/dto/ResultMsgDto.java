package com.iv.tenant.api.dto;

public class ResultMsgDto {

	//private WorkflowType type;// 工作流类型
	private long approveTime;// 审批时间
	private boolean approved;// 审批结果
	private String remark;// 审批人备注
	private String enterprise;// 企业名称
	private String subEnterprise;// 项目组名称
	public long getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(long approveTime) {
		this.approveTime = approveTime;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
