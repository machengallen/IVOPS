package com.iv.permission.dto;

import java.util.Set;

import com.iv.permission.entity.FunctionInfo;

/**
 * 封装权限信息
 * @author zhangying
 * 2018年6月12日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public class SubTenantRoleFunctionDto {
	private int id;
	private String name;
	private String des;
	private Set<FunctionInfo> functionInfos;
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
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public Set<FunctionInfo> getFunctionInfos() {
		return functionInfos;
	}
	public void setFunctionInfos(Set<FunctionInfo> functionInfos) {
		this.functionInfos = functionInfos;
	}
	
}
