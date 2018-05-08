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
}
