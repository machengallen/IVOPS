package com.iv.operation.script.dao.impl;

import com.iv.operation.script.entity.SingleTaskLifeEntity;

public interface ISingleTaskLifeDao {

	void save(SingleTaskLifeEntity entity) throws RuntimeException;
	
	SingleTaskLifeEntity selectByTaskId(int taskId) throws RuntimeException;
}
