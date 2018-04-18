package com.iv.tenant.api.dto;


public class TaskTenantApplyResp {

	private String taskId;
	private WorkflowType type;
	private int applicantId;
	private TenantInfoDto tenantInfo;
	private String createTime;
	
	public TaskTenantApplyResp() {
		super();
	}

	public TaskTenantApplyResp(String taskId, WorkflowType type, int applicantId, TenantInfoDto tenantInfo,
			String createTime) {
		super();
		this.taskId = taskId;
		this.type = type;
		this.applicantId = applicantId;
		this.tenantInfo = tenantInfo;
		this.createTime = createTime;
	}

	public WorkflowType getType() {
		return type;
	}
	public void setType(WorkflowType type) {
		this.type = type;
	}
	public int getApplicant() {
		return applicantId;
	}
	public void setApplicant(int applicant) {
		this.applicantId = applicantId;
	}
	public TenantInfoDto getTenantInfo() {
		return tenantInfo;
	}
	public void setTenantInfo(TenantInfoDto tenantInfo) {
		this.tenantInfo = tenantInfo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
