package com.iv.permission.dao;

import java.util.List;
import java.util.Set;

import com.iv.common.enumeration.ServiceType;
import com.iv.permission.entity.FunctionInfo;
import com.iv.permission.entity.PermissionInfo;

public interface FunctionInfoDao {
	void saveFunction(FunctionInfo functionInfo) throws RuntimeException;
	
	List<FunctionInfo> selectFunctionByIds(Set<Integer> ids) throws RuntimeException;
	
	List<FunctionInfo> selectAllFunction() throws RuntimeException;
	
	void deleteFunctions(Set<Integer> ids) throws RuntimeException;
	
	FunctionInfo selectFunctionById(int id) throws RuntimeException;
	
	List<FunctionInfo> selectFunctionByServiceType(List<ServiceType> services) throws RuntimeException;
	
	List<Integer> selectAllSubFunction() throws RuntimeException;
	
	List<Integer> selectApproveFuncIds(String code) throws RuntimeException;
}
