package com.iv.permission.api.dto;

import java.util.Set;

public class SubTenantRoleDto {
	private int id;
	private String name;
	private String des;
	private Set<Integer> permissionIds;
	private Set<Short> groupIds;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public Set<Short> getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(Set<Short> groupIds) {
		this.groupIds = groupIds;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<Integer> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(Set<Integer> permissionIds) {
		this.permissionIds = permissionIds;
	}
	
}
