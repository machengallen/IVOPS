package com.iv.strategy.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.strategy.api.dto.StrategyQueryDto;

/**
 * 告警策略服务
 * 
 * @author macheng 2018年4月10日
 *  alarm-strategy-service-1.0.0-SNAPSHOT
 * 
 */
public interface IAlarmStrategyService {

	@RequestMapping(value = "/get/strategy", method = RequestMethod.POST)
	ResponseDto getStrategy(@RequestBody StrategyQueryDto queryDto);
	
	@RequestMapping(value = "/strategy/exist", method = RequestMethod.GET)
	boolean strategyExist(@RequestParam("groupId") short groupId);
	
}
