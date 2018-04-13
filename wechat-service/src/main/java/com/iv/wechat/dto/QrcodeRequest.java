package com.iv.wechat.dto;


public class QrcodeRequest {


	private int expire_seconds;
	private String action_name;
	private QrcodeActionInfo action_info;
	
	public QrcodeRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QrcodeRequest(int expire_seconds, String action_name, QrcodeActionInfo action_info) {
		super();
		this.expire_seconds = expire_seconds;
		this.action_name = action_name;
		this.action_info = action_info;
	}
	public int getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(int expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public QrcodeActionInfo getAction_info() {
		return action_info;
	}
	public void setAction_info(QrcodeActionInfo action_info) {
		this.action_info = action_info;
	}
	

}
