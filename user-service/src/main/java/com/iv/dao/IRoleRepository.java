package com.iv.dao;

import org.springframework.stereotype.Repository;

import com.iv.entity.RoleEntity;

@Repository
public interface IRoleRepository {

	void save(RoleEntity entity) throws RuntimeException;
	
	RoleEntity findByName(String name) throws RuntimeException;
	
	RoleEntity findByTenantId(String tenantId) throws RuntimeException;
	
	void delById(int id) throws RuntimeException;
}
