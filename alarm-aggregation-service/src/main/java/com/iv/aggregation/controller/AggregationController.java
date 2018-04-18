package com.iv.aggregation.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iv.aggregation.dao.impl.AlarmProxyCerDaoImpl;
import com.iv.aggregation.entity.AlarmProxyCerEntity;
import com.iv.common.response.ErrorMsg;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;

/**
 * 告警接入配置北向管理api
 * 
 * @author macheng 2018年4月12日 alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@RestController
public class AggregationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AggregationController.class);
	@Autowired
	private AlarmProxyCerDaoImpl alarmProxyCerDao;

	@GetMapping("/alarm/proxy/access")
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
}
