package com.iv.permission.api.dto;
import java.util.Set;

public class GlobalRoleCreate {
	private int id;
	/*角色名称*/
	private String name;
	/*角色描述*/
	private String description;	
	/*项目组id列表*/
	private Set<Integer> subTenantIds;	
	private Set<Integer> functionIds;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<Integer> getSubTenantIds() {
		return subTenantIds;
	}
	public void setSubTenantIds(Set<Integer> subTenantIds) {
		this.subTenantIds = subTenantIds;
	}
	public Set<Integer> getFunctionIds() {
		return functionIds;
	}
	public void setFunctionIds(Set<Integer> functionIds) {
		this.functionIds = functionIds;
	}
	
}
