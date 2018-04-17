package com.iv.outer.dto;

import java.util.Set;

public class LocalAuthDto {
	private int id;
	private String userName;
	//private Set<RoleEntity> roles;
	private String realName;
	private String tel;
	private byte boundFlag;
	private Set<Integer> enterprisIds;
	private Set<Integer> subEnterpriIds;
	private String curTenantId;
	private Set<UserOauthDto> UserOauthes;
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
	public byte getBoundFlag() {
		return boundFlag;
	}
	public void setBoundFlag(byte boundFlag) {
		this.boundFlag = boundFlag;
	}
	public Set<Integer> getEnterprisIds() {
		return enterprisIds;
	}
	public void setEnterprisIds(Set<Integer> enterprisIds) {
		this.enterprisIds = enterprisIds;
	}
	public Set<Integer> getSubEnterpriIds() {
		return subEnterpriIds;
	}
	public void setSubEnterpriIds(Set<Integer> subEnterpriIds) {
		this.subEnterpriIds = subEnterpriIds;
	}
	public String getCurTenantId() {
		return curTenantId;
	}
	public void setCurTenantId(String curTenantId) {
		this.curTenantId = curTenantId;
	}
	public Set<UserOauthDto> getUserOauthes() {
		return UserOauthes;
	}
	public void setUserOauthes(Set<UserOauthDto> userOauthes) {
		UserOauthes = userOauthes;
	}
	@Override
	public String toString() {
		return "LocalAuthDto [id=" + id + ", userName=" + userName + ", realName=" + realName + ", tel=" + tel
				+ ", boundFlag=" + boundFlag + ", enterprisIds=" + enterprisIds + ", subEnterpriIds=" + subEnterpriIds
				+ ", curTenantId=" + curTenantId + ", UserOauthes=" + UserOauthes + "]";
	}
	
}
