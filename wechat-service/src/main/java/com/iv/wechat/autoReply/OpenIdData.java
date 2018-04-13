package com.iv.wechat.autoReply;

import java.util.List;

public class OpenIdData {

	private List<String> openid;

	public OpenIdData(List<String> openid) {
		super();
		this.openid = openid;
	}

	public OpenIdData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<String> getOpenid() {
		return openid;
	}

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}
	
	
}
