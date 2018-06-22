package com.iv.operation.script.dao.impl;

import java.util.List;

import com.iv.operation.script.entity.ImmediateTargetEntity;

public interface IImmediateTargetDao {

	void batchSave(List<ImmediateTargetEntity> entities) throws RuntimeException;
	
	List<ImmediateTargetEntity> selectByTaskId(int taskId) throws RuntimeException;
	
	void delByTaskId(int taskId) throws RuntimeException;
}
