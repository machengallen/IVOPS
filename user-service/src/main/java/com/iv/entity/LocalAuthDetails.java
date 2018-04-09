package com.iv.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;


public class LocalAuthDetails extends LocalAuth implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -486670679442544244L;

	private boolean enabled = true;//使能用户
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		// 超级管理员用户
		if("admin".equals(this.getUsername())) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_administrator");
			authorities.add(authority);
			return authorities;
		}
		// 普通用户，取当前租户下拥有的角色
		Set<RoleEntity> roles = this.getRoles();
		if(!CollectionUtils.isEmpty(roles)) {
			// 获取当前用户所选租户下的角色列表
			for (RoleEntity roleEntity : roles) {
				if(roleEntity.getTenantId().equals(this.getCurTenantId())) {
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleEntity.getName());
					authorities.add(authority);
				}
			}
			return authorities;
		}
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return super.getPassWord();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return super.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
