package com.iv.tenant.dao;

import java.util.List;

import com.iv.tenant.entity.EnterpriseEntity;
import com.iv.tenant.entity.SubEnterpriseEntity;

public interface ISubEnterpriseDao {

	void save(SubEnterpriseEntity subEnterpriseEntity) throws RuntimeException;
	
	List<SubEnterpriseEntity> selectByEnterprise(EnterpriseEntity enterpriseEntity) throws RuntimeException;
	
	SubEnterpriseEntity countWithEnterprise(EnterpriseEntity enterpriseEntity) throws RuntimeException;
	
	SubEnterpriseEntity selectByTenantId(String tenantId) throws RuntimeException;
	
	SubEnterpriseEntity selectById(int id) throws RuntimeException;
	
	SubEnterpriseEntity selectByName(String name) throws RuntimeException;
	
	List<String> selectAllTenantId() throws RuntimeException;
	
	List<SubEnterpriseEntity> selectAll() throws RuntimeException;
	
	void delById(int id) throws RuntimeException;
	
	List<SubEnterpriseEntity> selectByUserId(int userId) throws RuntimeException;
}
