package com.iv.enter.dto;

import java.util.List;

public class UsersWechatsQuery {
	private List<Integer> userIds;
	private String loginType;
	
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
}
