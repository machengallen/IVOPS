package com.iv.script.dto;

import com.iv.common.enumeration.WorkflowType;
import com.iv.script.entity.ScriptEntity;

public class TaskScriptStoreApplyResp {

	private String taskId;
	private WorkflowType type;
	private String applicant;
	private String email;
	private String tel;
	private ScriptDetailInfoDto scriptInfo;
	private String createTime;
	
	public TaskScriptStoreApplyResp() {
		super();
	}

	public TaskScriptStoreApplyResp(String taskId, WorkflowType type, String applicant, String email, String tel,
			ScriptDetailInfoDto scriptInfo, String createTime) {
		super();
		this.taskId = taskId;
		this.type = type;
		this.applicant = applicant;
		this.email = email;
		this.tel = tel;
		this.scriptInfo = scriptInfo;
		this.createTime = createTime;
	}

	public WorkflowType getType() {
		return type;
	}
	public void setType(WorkflowType type) {
		this.type = type;
	}
	
	public ScriptDetailInfoDto getScriptInfo() {
		return scriptInfo;
	}

	public void setScriptInfo(ScriptDetailInfoDto scriptInfo) {
		this.scriptInfo = scriptInfo;
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
