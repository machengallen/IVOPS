package com.iv.wechat.dto;

public class QrcodeTicket {
	// 二维码有效期
	private int expire_seconds;
	// 二维码获取地址
	private String url;
	public QrcodeTicket() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QrcodeTicket(int expire_seconds, String url) {
		super();
		this.expire_seconds = expire_seconds;
		this.url = url;
	}
	public int getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(int expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
