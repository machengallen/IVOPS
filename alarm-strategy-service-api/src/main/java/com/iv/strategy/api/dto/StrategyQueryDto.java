package com.iv.strategy.api.dto;

import com.iv.common.enumeration.Severity;

public class StrategyQueryDto {

	private String id;
	// 策略标签
	private String tag;
	// 告警等级
	private Severity severity;
	// 监控类型
	private String itemType;
	// 租户id
	private String tenantId;
	//当前页
	private int curPage;
	//每页显示条数
	private int items;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
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
