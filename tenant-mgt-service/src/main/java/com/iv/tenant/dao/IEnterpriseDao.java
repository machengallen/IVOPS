package com.iv.tenant.dao;

import java.util.List;

import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.entity.EnterpriseEntity;

public interface IEnterpriseDao {

	EnterpriseEntity save(EnterpriseEntity entity) throws RuntimeException;
	
	EnterpriseEntity selectById(int id) throws RuntimeException;
	
	List<EnterpriseEntity> selectByCondition(QueryEnterReq req) throws RuntimeException;
	
	EnterpriseEntity selectByOrgCode(String orgCode) throws RuntimeException;
	
	EnterpriseEntity selectByName(String orgCode) throws RuntimeException;
	
	List<EnterpriseEntity> selectAll() throws RuntimeException;
	
	long countAll() throws RuntimeException;
	
	List<EnterpriseEntity> selectByUserId(int userId) throws RuntimeException;
}
