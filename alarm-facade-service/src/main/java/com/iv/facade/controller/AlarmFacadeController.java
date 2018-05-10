package com.iv.facade.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iv.aggregation.api.constant.AlarmQueryType;
import com.iv.aggregation.api.dto.AlarmPagingDto;
import com.iv.aggregation.api.dto.AlarmQueryDto;
import com.iv.aggregation.api.dto.AlarmTransferDto;
import com.iv.common.response.ErrorMsg;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.JWTUtil;
import com.iv.facade.feign.client.IAlarmAggregationClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 告警北向服务
 * @author macheng
 * 2018年4月9日
 * alarm-facade-service-1.0.0-SNAPSHOT
 * 
 */
@RestController
@Api(description = "告警北向相关接口")
public class AlarmFacadeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmFacadeController.class);

	@Autowired
	private IAlarmAggregationClient alarmAggregationClient;
	/**
	 * 告警认领
	 * 
	 * @param session
	 * @param lifeId
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/claim", method = RequestMethod.GET)
	@ApiOperation("告警认领")
	public ResponseDto claimAlarm(HttpServletRequest request, @RequestParam String lifeId) {
		
		try {
			return alarmAggregationClient.claimAlarm(request.getHeader("Authorization"), lifeId);
		} catch (Exception e) {
			LOGGER.error("系统内部错误:", e);
			return ResponseDto.builder(ErrorMsg.UNKNOWN);
		}
	}

	/**
	 * 告警转让
	 * 
	 * @param session
	 * @param lifeId
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	@ApiOperation("告警转让")
	public ResponseDto transferAlarm(HttpServletRequest request, @RequestBody AlarmTransferDto dto) {

		try {
			
			return alarmAggregationClient.transferAlarm(dto);

		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.UNKNOWN);
		}
	}
	
	/**
	 * 我的告警以及查询
	 * 
	 * @return
	 */	
	@RequestMapping(value = "/get/myAlarm", method = RequestMethod.POST)
	@ApiOperation("查询我的告警")
	public ResponseDto getMyAlarmPaging(HttpServletRequest request, @RequestBody AlarmQueryDto query) {

		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			query.setHandlerCurrent(userId);
			query.setAlarmQueryType(AlarmQueryType.MY);
			AlarmPagingDto alarmPagingDto = alarmAggregationClient.getMyAlarmPaging(query);
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(alarmPagingDto);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.GET_DATA_FAILED);
		}
	}

	/**
	 * 所有告警以及查询
	 * 
	 * @return
	 */	
	@RequestMapping(value = "/get/alarm", method = RequestMethod.POST)
	@ApiOperation("查询所有告警")
	public ResponseDto getAlarmPaging(@RequestBody AlarmQueryDto query) {

		try {
			query.setAlarmQueryType(AlarmQueryType.ALL);
			AlarmPagingDto alarmPagingDto = alarmAggregationClient.getAlarmPaging(query);
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(alarmPagingDto);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.GET_DATA_FAILED);
		}
	}
	
	/**
	 * 微信客户端刷新告警详情页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/refresh/alarmDetail", method = RequestMethod.GET)
	@ApiOperation("微信客户端刷新告警详情页")
	public ResponseDto refreshAlarmDetail(@RequestParam String lifeId) {

		ResponseDto responseDto = new ResponseDto();
		try {
			ResponseDto dto = alarmAggregationClient.getAlarmDetails(lifeId);
			if(null != dto) {
				return dto;
			}else {
				return ResponseDto.builder(ErrorMsg.GET_DATA_FAILED);
			}
            
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("微信错误：刷新微信端告警详情页错误", e);
			responseDto.setErrorMsg(ErrorMsg.GET_DATA_FAILED);
			return responseDto;	
		}
	}

	
	/**
	 * 查询告警的分析
	 * 
	 * @param query
	 * @return
	 *//*
	@RequestMapping(value = "/get/alarm/analysis/{id}", method = RequestMethod.GET)
	@ApiOperation("查询告警的分析")
	public ResponseDto getAlarmAnalysis(@PathVariable(required = true) String id) {

		ResponseDto responseDto = new ResponseDto();
		try {
			responseDto.setData(service.getAlarmAnalysis(id));
			responseDto.setErrorMsg(ErrorMsg.OK);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：获取告警分析失败", e);
			responseDto.setErrorMsg(ErrorMsg.GET_ALARM_ANALYSIS_FAILED);
			return responseDto;
		}
	}*/
	
}
