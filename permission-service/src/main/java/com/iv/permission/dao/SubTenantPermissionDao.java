package com.iv.permission.dao;

import java.util.List;
import java.util.Set;

import com.iv.permission.entity.SubTenantPermission;

public interface SubTenantPermissionDao {
	
	List<SubTenantPermission> selectSubTenantPermissionByIds(Set<Integer> ids) throws RuntimeException;
	
	List<Integer> selectAllSubTenantPermission() throws RuntimeException;
	
	List<Integer> selectAllSubTenantPermissionByTenId(String tenantId) throws RuntimeException;
	
	void saveOrUpdate(List<SubTenantPermission> permissions, String tenantId) throws RuntimeException;
}
