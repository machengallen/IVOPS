package com.iv.dto;

/**
 * 模板类父类
 * @author zhangying
 * 2018年4月12日
 * aggregation-1.4.0-SNAPSHOT
 */
public class ContentData {
	private Note first;
	private Note remark;
	public Note getFirst() {
		return first;
	}
	public void setFirst(Note first) {
		this.first = first;
	}
	public Note getRemark() {
		return remark;
	}
	public void setRemark(Note remark) {
		this.remark = remark;
	}
	
}
