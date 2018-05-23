package com.iv.aggregation.feign.fallback;

import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.IAlarmStrategyClient;
import com.iv.strategy.api.dto.AlarmStrategyDto;
import com.iv.strategy.api.dto.StrategyQueryDto;

@Component
public class AlarmStrategyClientFallBack implements IAlarmStrategyClient {

	@Override
	public AlarmStrategyDto getStrategy(StrategyQueryDto queryDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean strategyExist(short groupId) {
		// TODO Auto-generated method stub
		return false;
	}


}
