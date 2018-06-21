package com.iv.permission.dao;

import java.util.List;

import com.iv.permission.entity.FunctionInfo;
import com.iv.permission.entity.SubTenantRole;
import com.iv.permission.entity.SubTenantUserRole;

public interface SubTenantUserRoleDao {
	
	void save(SubTenantUserRole subTenantUserRole) throws RuntimeException;
	
	void saveByTenantId(SubTenantUserRole subTenantUserRole, String tenantId) throws RuntimeException;
	
	List<Integer> getUserIdsByFuncIds(String tenantId, List<Integer> funcIds) throws RuntimeException;
	
	SubTenantUserRole selectUserRoleByUserId(int userId) throws RuntimeException;
	
	List<SubTenantRole> selectUserRoleByTenantId(int userId, String tenantId) throws RuntimeException;
}
