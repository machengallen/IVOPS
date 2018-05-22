package com.iv.permission.api.dto;

/**
 * 分页查询条件
 * @author zhangying
 * 2018年5月17日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public class PageQueryDto {
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
