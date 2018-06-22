package com.iv.operation.script.dao.impl;

import java.util.List;

import com.iv.operation.script.entity.SingleTaskTargetEntity;

public interface ISingleTaskTargetDao {

	void batchSave(List<SingleTaskTargetEntity> entities) throws RuntimeException;
	
	List<SingleTaskTargetEntity> selectByScheduleId(int scheduleId) throws RuntimeException;
	
	void delBySingleTaskId(int taskId) throws RuntimeException;
	
}
