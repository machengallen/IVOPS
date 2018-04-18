package com.iv.aggregation.dao;

import com.iv.aggregation.entity.AlarmCleanStrategyEntity;

public interface IAlarmCleanStrategyDao{

	int id = 1;
	
	void saveStrategy(AlarmCleanStrategyEntity entity) throws RuntimeException;
	
	AlarmCleanStrategyEntity saveStrategy(AlarmCleanStrategyEntity entity, String tenantId) throws RuntimeException;
	
	AlarmCleanStrategyEntity selectById(int id) throws RuntimeException;
	
	AlarmCleanStrategyEntity selectById(int id, String tenantId) throws RuntimeException;
}
