package com.iv.strategy.dao;

import java.util.List;

import com.iv.common.enumeration.Severity;
import com.iv.strategy.api.dto.StrategyQueryDto;
import com.iv.strategy.entity.AlarmStrategyEntity;


public interface IAlarmStrategyDao {

	void saveStrategy(AlarmStrategyEntity alarmLifeStrategy) throws RuntimeException;
	
	AlarmStrategyEntity selectStrategy(Severity severity, String itemType) throws RuntimeException;
	
	AlarmStrategyEntity selectById(String id) throws RuntimeException;
	
	StrategyPaging selectAll() throws RuntimeException;
	
	StrategyPaging selectByCurPage(int curItems, int item, StrategyQueryDto query) throws RuntimeException;
	
	void updateEnable(String id) throws RuntimeException;
	
	void delStrategies(List<String> ids) throws RuntimeException;
	
	List<AlarmStrategyEntity> selectBatchStrategies(List<String> ids) throws RuntimeException;
	
	int selectCountsByType(String ItemType) throws RuntimeException;
	
	int countByGroupId(short groupId) throws RuntimeException;

}
