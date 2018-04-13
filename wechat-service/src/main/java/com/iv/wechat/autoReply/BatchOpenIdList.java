package com.iv.wechat.autoReply;

import java.util.List;

public class BatchOpenIdList {

	private List<BatchOpenId> user_list;

	public BatchOpenIdList(List<BatchOpenId> user_list) {
		super();
		this.user_list = user_list;
	}

	public BatchOpenIdList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<BatchOpenId> getUser_list() {
		return user_list;
	}

	public void setUser_list(List<BatchOpenId> user_list) {
		this.user_list = user_list;
	}
	
	
}
