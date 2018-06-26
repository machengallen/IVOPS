package com.iv.operation.script.dao.impl;

import java.util.List;

import com.iv.common.dto.ObjectPageDto;
import com.iv.operation.script.dto.ScheduleQueryDto;
import com.iv.operation.script.entity.SingleTaskScheduleEntity;

public interface ISingleTaskScheduleDao {

	SingleTaskScheduleEntity save(SingleTaskScheduleEntity entity) throws RuntimeException;
	
	SingleTaskScheduleEntity selectById(int id) throws RuntimeException;
	
	List<SingleTaskScheduleEntity> selectByTaskId(int taskId) throws RuntimeException;
	
	void delById(int id) throws RuntimeException;
	
	int delByTaskId(int taskId) throws RuntimeException;
	
	ObjectPageDto selectPage(ScheduleQueryDto queryDto) throws RuntimeException;
}
