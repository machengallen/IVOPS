package com.iv.aggregation.feign.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.IAlarmStrategyClient;
import com.iv.strategy.api.dto.AlarmStrategyDto;
import com.iv.strategy.api.dto.NoticeStrategyDto;
import com.iv.strategy.api.dto.StrategyQueryDto;

@Component
public class AlarmStrategyClientFallBack implements IAlarmStrategyClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(AlarmStrategyClientFallBack.class);
	@Override
	public AlarmStrategyDto getStrategy(StrategyQueryDto queryDto) {
		// TODO Auto-generated method stub
		LOGGER.error("告警策略服务调用失败");
		return null;
	}

	@Override
	public boolean strategyExist(short groupId) {
		// TODO Auto-generated method stub
		LOGGER.error("告警策略服务调用失败");
		return false;
	}


	@Override
	public NoticeStrategyDto selectNoticeStrategy(Integer userId) {
		// TODO Auto-generated method stub
		LOGGER.error("告警策略服务调用失败");
		return null;
	}


}
