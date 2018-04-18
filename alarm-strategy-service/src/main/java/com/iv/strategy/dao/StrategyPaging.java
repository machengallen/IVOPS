package com.iv.strategy.dao;

import java.util.List;

import com.iv.strategy.entity.AlarmStrategyEntity;


public class StrategyPaging {
	int count;
	List<AlarmStrategyEntity> strategies;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<AlarmStrategyEntity> getStrategies() {
		return strategies;
	}
	public void setStrategies(List<AlarmStrategyEntity> strategies) {
		this.strategies = strategies;
	}
	
}
