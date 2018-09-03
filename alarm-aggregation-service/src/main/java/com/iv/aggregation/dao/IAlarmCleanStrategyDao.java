package com.iv.aggregation.dao;

import com.iv.aggregation.entity.AlarmCleanStrategyEntity;

public interface IAlarmCleanStrategyDao{

	int id = 1;
	
	AlarmCleanStrategyEntity saveStrategy(AlarmCleanStrategyEntity entity) throws RuntimeException;
	
	AlarmCleanStrategyEntity selectById(int id) throws RuntimeException;
	
}
