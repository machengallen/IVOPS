package com.iv.outer.dto;

/**
 * 封装用户基础信息以及微信信息类
 * @author zhangying
 * 2018年4月16日
 * aggregation-1.4.0-SNAPSHOT
 */
public class UserInfosDto {
	private int id;
	private String userName;	
	private String realName;
	private String tel;
	private String headimgurl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	} 
	
}
