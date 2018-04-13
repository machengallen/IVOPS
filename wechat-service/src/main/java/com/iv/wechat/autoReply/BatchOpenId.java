package com.iv.wechat.autoReply;

public class BatchOpenId {

	private String openid;
	private String lang;
	
	public BatchOpenId() {
		// TODO Auto-generated constructor stub
	}

	public BatchOpenId(String openid, String lang) {
		super();
		this.openid = openid;
		this.lang = lang;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	
}
