package com.iv.outer.dto;

import java.util.Set;

/**
 * 用户基础信息供其他服务调用类（无三方登录信息）
 * @author zhangying
 * 2018年4月18日
 * aggregation-1.4.0-SNAPSHOT
 */
public class LocalAuthDto {
	private int id;
	private String userName;
	private String realName;
	private String nickName;
	private String email;
	private String tel;	
	private String curTenantId;
	private String headimgurl;
	private String passWord;
	private Set<SubTenantRoleDto> roles;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCurTenantId() {
		return curTenantId;
	}
	public void setCurTenantId(String curTenantId) {
		this.curTenantId = curTenantId;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
		
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}	
		
	
	public Set<SubTenantRoleDto> getRoles() {
		return roles;
	}
	public void setRoles(Set<SubTenantRoleDto> roles) {
		this.roles = roles;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((realName == null) ? 0 : realName.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalAuthDto other = (LocalAuthDto) obj;
		if (id != other.id)
			return false;
		return true;
	}	
	
}
