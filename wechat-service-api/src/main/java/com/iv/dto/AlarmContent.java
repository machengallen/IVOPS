package com.iv.dto;

/**
 * {{first.DATA}}
告警对象：{{keyword1.DATA}}
告警地址：{{keyword2.DATA}}
告警内容：{{keyword3.DATA}}
告警级别：{{keyword4.DATA}}
告警时间：{{keyword5.DATA}}
{{remark.DATA}}
 * @author zhangying
 * 2018年4月12日
 * aggregation-1.4.0-SNAPSHOT
 */
public class AlarmContent extends ContentData{	
	private Note keyword1;
	private Note keyword2;
	private Note keyword3;
	private Note keyword4;
	private Note keyword5;	
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
	public Note getKeyword5() {
		return keyword5;
	}
	public void setKeyword5(Note keyword5) {
		this.keyword5 = keyword5;
	}
	
}
