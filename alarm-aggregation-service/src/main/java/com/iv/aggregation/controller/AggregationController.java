package com.iv.aggregation.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iv.aggregation.api.dto.AlarmMessageInput;
import com.iv.aggregation.dao.impl.AlarmProxyCerDaoImpl;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmProxyCerEntity;
import com.iv.aggregation.feign.clients.IAlarmAnalysisClient;
import com.iv.aggregation.service.CoreService;
import com.iv.aggregation.service.RabbitAlarmService;
import com.iv.analysis.api.dto.AlarmInfoDto;
import com.iv.common.response.ErrorMsg;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 告警接入配置北向管理api
 * 
 * @author macheng 2018年4月12日 alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@RestController
@Api("告警驱动支持服务")
public class AggregationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AggregationController.class);
	@Autowired
	private AlarmProxyCerDaoImpl alarmProxyCerDao;
	@Autowired
	private RabbitAlarmService rabbitAlarmService;
	@Autowired
	private IAlarmAnalysisClient alarmAnalysisClient;
	@Autowired
	private CoreService coreService;

	/**
	 * 获取告警接入令牌，每个租户对应一个令牌
	 * @param request
	 * @return
	 */
	@ApiOperation("获取三方告警接入令牌")
	@GetMapping("/proxy/access")
	public ResponseDto alarmProxyAccess(HttpServletRequest request) {

		try {
			AlarmProxyCerEntity alarmProxyCerEntity = new AlarmProxyCerEntity();
			// 获取用户会话中的租户id
			alarmProxyCerEntity
					.setTenantId(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("curTenantId"));
			String token = alarmProxyCerDao.save(alarmProxyCerEntity);
			ResponseDto dto = ResponseDto.builder(ErrorMsg.OK);
			dto.setData(token);
			return dto;
		} catch (RuntimeException e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.UNKNOWN);
		}

	}
	
	/**
	 * 开放告警接收入口
	 * 
	 * @param ami
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@ApiOperation("开放告警接收入口")
	@PostMapping("/openalarm")
	public String alarmProcessing(@RequestBody AlarmMessageInput ami) {
		//日志记录
		LOGGER.warn("#告警！ [来源：开放API ]--" + "[token]:" + ami.getToken() + " -- " + new Date().toLocaleString());
		String tenantId = rabbitAlarmService.getTenantId(ami.getToken());
		if (null == tenantId) {
			LOGGER.info("【告警内容】" + ami.toString());
			return "Illegal tenant identification";
		} else {

			AlarmLifeEntity alarmLifeEntity = null;
			try {
				alarmLifeEntity = coreService.alarmProcessing(ami, tenantId);
			} catch (Exception e) {
				LOGGER.error("告警处理失败", e);
				LOGGER.info(ami.toString());
				return "notice failed";
			}

			try {
				if(null != alarmLifeEntity) {
					AlarmInfoDto alarmInfoDto = new AlarmInfoDto();
					alarmInfoDto.setId(alarmLifeEntity.getId());
					alarmInfoDto.setContent(alarmLifeEntity.getAlarm().getContent());
					alarmInfoDto.setTenantId(alarmLifeEntity.getAlarm().getTenantId());
					ResponseDto responseDto = alarmAnalysisClient.alarmAnalysis(alarmInfoDto);
					if(responseDto.getErrcode() != 0) {
						LOGGER.error("告警分析服务请求异常");
					}
				}
			} catch (Exception e) {
				LOGGER.error("告警分析失败", e);
			}
			
			return "ok";
		}
	}
}
