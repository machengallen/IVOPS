package com.iv.tenant.api.dto;

import com.iv.common.enumeration.WorkflowType;

public class TaskEnterpriseApplyResp {

	private String taskId;
	private WorkflowType type;
	private String applicant;
	private String email;
	private String tel;
	private TenantInfoDto tenantInfo;
	private long createTime;
	
	public TaskEnterpriseApplyResp() {
		super();
	}

	public TaskEnterpriseApplyResp(String taskId, WorkflowType type, String applicant, String email, String tel,
			TenantInfoDto tenantInfo, long createTime) {
		super();
		this.taskId = taskId;
		this.type = type;
		this.applicant = applicant;
		this.email = email;
		this.tel = tel;
		this.tenantInfo = tenantInfo;
		this.createTime = createTime;
	}

	public WorkflowType getType() {
		return type;
	}
	public void setType(WorkflowType type) {
		this.type = type;
	}
	public TenantInfoDto getTenantInfo() {
		return tenantInfo;
	}
	public void setTenantInfo(TenantInfoDto tenantInfo) {
		this.tenantInfo = tenantInfo;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
}
