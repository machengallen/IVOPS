package com.iv.analysis.api.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iv.analysis.api.dto.AlarmInfoDto;
import com.iv.common.response.ResponseDto;

/**
 * 告警离线分析服务
 * 
 * @author macheng 2018年4月8日
 *  alarm-analysis-service-1.0.0-SNAPSHOT
 * 
 */
public interface IAlarmAnalysisService {

	@RequestMapping(value = "/get/targetEnter", method = RequestMethod.POST)
	ResponseDto alarmAnalysis(AlarmInfoDto infoDto);
	
	
}
