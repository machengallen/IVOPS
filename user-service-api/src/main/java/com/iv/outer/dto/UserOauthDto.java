package com.iv.outer.dto;

import com.iv.enumeration.LoginType;

public class UserOauthDto {
	private String id;
	/**用户Id*/
	private int userId;
	/**用户三方登录类型*/
	private LoginType loginType ;
	/**三方登录的唯一标识*/
	private String unionid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public LoginType getLoginType() {
		return loginType;
	}
	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	
	public UserOauthDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserOauthDto(int userId, LoginType loginType, String unionid) {
		super();
		this.userId = userId;
		this.loginType = loginType;
		this.unionid = unionid;
	}
	
	
}
