package com.iv.operation.script.dao.impl;

import java.util.List;

import com.iv.operation.script.entity.ScheduleTargetEntity;

public interface IScheduleTargetDao {

	void batchSave(List<ScheduleTargetEntity> entities) throws RuntimeException;
	
	List<ScheduleTargetEntity> selectByScheduleId(int scheduleId) throws RuntimeException;
	
	void delByScheduleId(int scheduleId) throws RuntimeException;
	
}
