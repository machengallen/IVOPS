package com.iv.operation.script.dao.impl;

import java.util.List;

import com.iv.operation.script.entity.OperationScriptEntity;

public interface IOperationScriptDao {

	void save(OperationScriptEntity entity) throws RuntimeException;
	
	List<OperationScriptEntity> selectById(int id) throws RuntimeException;
	
	List<OperationScriptEntity> selectPage(int page, int items) throws RuntimeException;
}
