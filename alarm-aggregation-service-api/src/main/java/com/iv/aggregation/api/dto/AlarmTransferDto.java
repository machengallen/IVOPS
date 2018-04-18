package com.iv.aggregation.api.dto;

public class AlarmTransferDto {

	private int toUser;
	private String lifeId;
	private String remark;
	private short groupId;
	public String getLifeId() {
		return lifeId;
	}
	public void setLifeId(String lifeId) {
		this.lifeId = lifeId;
	}
	public int getToUser() {
		return toUser;
	}
	public void setToUser(int toUser) {
		this.toUser = toUser;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public short getGroupId() {
		return groupId;
	}
	public void setGroupId(short groupId) {
		this.groupId = groupId;
	}
	
}
