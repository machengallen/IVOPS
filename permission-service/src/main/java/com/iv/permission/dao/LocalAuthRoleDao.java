package com.iv.permission.dao;

import java.util.List;

import com.iv.permission.entity.LocalAuthRole;

public interface LocalAuthRoleDao {
	
	List<Integer> getUserIdsByCode(String code) throws RuntimeException;
	
	LocalAuthRole selectLocalAuthRoleById(int userId) throws RuntimeException;
	
	void saveOrUpdate(LocalAuthRole localAuthRole) throws RuntimeException;
}
