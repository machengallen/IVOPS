package com.iv.entity.dto;

import java.util.Set;

public class LocalAuthDto {
	private int id;
	private String userName;
	private String passWord;
	private Set<RoleEntityDto> roles;
	private String realName;
	private String tel;
	private byte boundFlag;
	private Set<EnterpriseEntityDto> enterprises;
	private Set<SubEnterpriseEntityDto> subEnterprise;
	private String curTenantId;
	private Set<UserOauthDto> UserOauthDtos;
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
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public Set<RoleEntityDto> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleEntityDto> roles) {
		this.roles = roles;
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
	public Set<EnterpriseEntityDto> getEnterprises() {
		return enterprises;
	}
	public void setEnterprises(Set<EnterpriseEntityDto> enterprises) {
		this.enterprises = enterprises;
	}
	public Set<SubEnterpriseEntityDto> getSubEnterprise() {
		return subEnterprise;
	}
	public void setSubEnterprise(Set<SubEnterpriseEntityDto> subEnterprise) {
		this.subEnterprise = subEnterprise;
	}
	public String getCurTenantId() {
		return curTenantId;
	}
	public void setCurTenantId(String curTenantId) {
		this.curTenantId = curTenantId;
	}
	public Set<UserOauthDto> getUserOauthDtos() {
		return UserOauthDtos;
	}
	public void setUserOauthDtos(Set<UserOauthDto> userOauthDtos) {
		UserOauthDtos = userOauthDtos;
	}	
}
