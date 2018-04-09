package com.iv.dto;

import java.util.List;

import com.iv.enumeration.OpsGroup;

public class GroupDto {

	private Short groupId;
	private String groupName;
	private List<Integer> userIds;
	private OpsGroup ops;
	public Short getGroupId() {
		return groupId;
	}
	public void setGroupId(Short groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	public OpsGroup getOps() {
		return ops;
	}
	public void setOps(OpsGroup ops) {
		this.ops = ops;
	}
	
}
