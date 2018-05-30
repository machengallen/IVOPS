package com.iv.dto;
/**
 * 工单微信模板
 * @author zhangying
 * 2018年5月28日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public class FormContent extends ContentData {
	private Note keyword1;//需求单位
	private Note keyword2;//需求类别
	private Note keyword3;//工单级别
	private Note keyword4;//工单状态
	public Note getKeyword1() {
		return keyword1;
	}
	public void setKeyword1(Note keyword1) {
		this.keyword1 = keyword1;
	}
	public Note getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(Note keyword2) {
		this.keyword2 = keyword2;
	}
	public Note getKeyword3() {
		return keyword3;
	}
	public void setKeyword3(Note keyword3) {
		this.keyword3 = keyword3;
	}
	public Note getKeyword4() {
		return keyword4;
	}
	public void setKeyword4(Note keyword4) {
		this.keyword4 = keyword4;
	}
	
}
