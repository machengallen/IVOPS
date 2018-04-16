package com.iv.outer.dto;

import java.util.List;

/**
 * 团队信息（只提供人员id）
 * @author zhangying
 * 2018年4月16日
 * aggregation-1.4.0-SNAPSHOT
 */
public class GroupEntityDto {
	private short groupId;
	private String groupName;	
	private List<Integer> userId;
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
	public List<Integer> getUserId() {
		return userId;
	}
	public void setUserId(List<Integer> userId) {
		this.userId = userId;
	}
	
}
