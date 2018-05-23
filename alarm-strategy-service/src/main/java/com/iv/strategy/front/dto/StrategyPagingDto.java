package com.iv.strategy.front.dto;

import java.util.List;


public class StrategyPagingDto {
	long count;
	List<AlarmStrategyFrontDto> strategies;
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public List<AlarmStrategyFrontDto> getStrategies() {
		return strategies;
	}
	public void setStrategies(List<AlarmStrategyFrontDto> strategies) {
		this.strategies = strategies;
	}
	
}
