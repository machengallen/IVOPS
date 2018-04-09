package com.iv.dto;

public class GroupUserQuery {
	private int curPage;
	private int items;
	private Short groupId;
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
	public Short getGroupId() {
		return groupId;
	}
	public void setGroupId(Short groupId) {
		this.groupId = groupId;
	}
	
}
