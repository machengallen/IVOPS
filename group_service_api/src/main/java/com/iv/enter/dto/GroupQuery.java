package com.iv.enter.dto;

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
	private boolean ifPaging;
		
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
	public boolean isIfPaging() {
		return ifPaging;
	}
	public void setIfPaging(boolean ifPaging) {
		this.ifPaging = ifPaging;
	}
	
}
