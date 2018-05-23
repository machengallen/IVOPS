package com.iv.aggregation.api.service;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.aggregation.api.constant.StrategyCycle;
import com.iv.aggregation.api.dto.AlarmPagingDto;
import com.iv.aggregation.api.dto.AlarmQueryDto;
import com.iv.aggregation.api.dto.AlarmTransferDto;
import com.iv.common.response.ResponseDto;

public interface IAlarmAggregationService {

	/**
	 * 告警认领
	 * @param lifeId
	 * @return
	 */
	@RequestMapping(value = "/modify/claim", method = RequestMethod.GET)
	ResponseDto claimAlarm(@RequestParam("token") String token, @RequestParam("lifeId") String lifeId);

	/**
	 * 告警转让
	 * @param session
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/modify/transfer", method = RequestMethod.POST)
	ResponseDto transferAlarm(@RequestBody AlarmTransferDto dto);
	
	/**
	 * 查询“我的”告警
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/get/myAlarm", method = RequestMethod.POST)
	AlarmPagingDto getMyAlarmPaging(@RequestBody AlarmQueryDto query);
	
	/**
	 * 查询所有告警
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/get/alarm", method = RequestMethod.POST)
	AlarmPagingDto getAlarmPaging(@RequestBody AlarmQueryDto query);
	
	/**
	 * 修改告警数据清理频率
	 * @param exp
	 * @return
	 */
	@RequestMapping(value = "/update/clean", method = RequestMethod.GET)
	ResponseDto updateAlarmCleanQuartz(@RequestParam("exp") String exp);
	
	/**
	 * 修改告警数据保留周期
	 * @param cycle
	 * @return
	 */
	@RequestMapping(value = "/update/store", method = RequestMethod.GET)
	ResponseDto updateAlarmCleanCycle(@RequestParam("cycle") StrategyCycle cycle);
	
	/**
	 * 获取告警详情
	 * @param lifeId
	 * @return
	 */
	@RequestMapping(value = "/get/alarm/details", method = RequestMethod.GET)
	ResponseDto getAlarmDetails(@RequestParam("lifeId")String lifeId);
	
}
