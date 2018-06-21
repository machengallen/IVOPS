package com.iv.permission.dao;

import java.util.List;
import java.util.Set;

import com.iv.permission.entity.SubTenantRole;

public interface SubTenantRoleDao {

	void saveOrUpdateSubTenantRole(SubTenantRole subTenantRole) throws RuntimeException;
	
	void saveOrUpdateSubTenantRoleBytenantId(SubTenantRole subTenantRole, String tenantId) throws RuntimeException;

	void deleteSubTenantRole(Set<Integer> ids) throws RuntimeException;
	
	SubTenantRole selectSubTenantRoleById(int id) throws RuntimeException;
	
	List<SubTenantRole> selectAllSubTenantRole() throws RuntimeException;
	
	List<SubTenantRole> selectSubTenantRoleByIds(Set<Integer> ids) throws RuntimeException;
	
	List<Integer> selectSubTenantRoleIds(Set<Integer> Permissionids) throws RuntimeException;
	
}
