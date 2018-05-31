package com.iv.authentication.pojo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class LocalUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 959185884805562554L;
	public LocalUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	private int userId;
	private String realName;
	private String curTenantId;
	private String email;
	private String tel;
	public String getCurTenantId() {
		return curTenantId;
	}
	public void setCurTenantId(String curTenantId) {
		this.curTenantId = curTenantId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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

}
