package com.iv.permission.dao;

import java.util.List;
import java.util.Set;

import com.iv.permission.dto.PermissionPageDto;
import com.iv.permission.entity.PermissionInfo;

public interface PermissionDao {
	
	List<PermissionInfo> getPermissionList() throws RuntimeException;
	
	PermissionPageDto getPermissionPageList(int curItems, int item) throws RuntimeException;
	
	List<PermissionInfo> getPermissionsByIds(List<String> codes) throws RuntimeException;
	
	void saveOrUpdatePermission(PermissionInfo permissionInfo) throws RuntimeException;
	
	void deletePermissionByIds(Set<String> codes) throws RuntimeException;
	
	PermissionInfo getPermissionsById(String code) throws RuntimeException;
}
