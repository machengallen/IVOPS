package com.iv.permission.api.dto;
import java.util.Set;

public class PersonRoleDto {
	private String id;
	/*用户主键*/
	private int userId;
	/*角色列表*/ 
	private Set<Integer> RoleIds;
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
	public Set<Integer> getRoleIds() {
		return RoleIds;
	}
	public void setRoleIds(Set<Integer> roleIds) {
		RoleIds = roleIds;
	}
		
}
