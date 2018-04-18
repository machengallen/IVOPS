package com.iv.dto;

import java.util.List;

public class UserIdsPagingDto {
	private long totalCount;
	private List<Integer> userIds;
	private short groupId;
	private String groupName;
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	public short getGroupId() {
		return groupId;
	}
	public void setGroupId(short groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	
}
