package com.iv.permission.dao;

import java.util.List;
import java.util.Set;

import com.iv.permission.entity.GlobalRole;

public interface GlobalRoleDao {
	void saveOrUpdate(GlobalRole globalRole) throws RuntimeException;
	
	GlobalRole selectById(int id) throws RuntimeException;
	
	void deleteGlobalRoleById(int id) throws RuntimeException;
	
	List<GlobalRole> selectGlobalRoleByIds(Set<Integer> ids) throws RuntimeException;
	
	void deleteGlobalRoleByIds(Set<Integer> ids) throws RuntimeException;
	
	List<GlobalRole> selectGlobalRoles() throws RuntimeException;
}
