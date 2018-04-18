package com.iv.tenant.api.dto;


public class HisProTenantApplyReq {

	private String id;
	private String startTime;
	private String endTime;
	private String duration;
	private String type;
	private int startUser;
	private TenantInfoDto tenantInfo;
	private boolean approved;
	private String remark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStartUser() {
		return startUser;
	}
	public void setStartUser(int startUser) {
		this.startUser = startUser;
	}
	public TenantInfoDto getTenantInfo() {
		return tenantInfo;
	}
	public void setTenantInfo(TenantInfoDto tenantInfo) {
		this.tenantInfo = tenantInfo;
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
	
}
