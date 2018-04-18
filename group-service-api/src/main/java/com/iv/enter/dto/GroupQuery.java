package com.iv.enter.dto;

import java.util.List;

/**
 * 查询、操作用户组入参
 * @author zhangying
 * 2018年4月13日
 * aggregation-1.4.0-SNAPSHOT
 */
public class GroupQuery {
	private int curPage;
	private int items;
	private short groupId;
	private String tenantId;
	private String groupName;
	private List<Integer> userIds;
	private OpsGroup ops;	
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getItems() {
		return items;
	}
	public void setItems(int items) {
		this.items = items;
	}
	public short getGroupId() {
		return groupId;
	}
	public void setGroupId(short groupId) {
		this.groupId = groupId;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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
