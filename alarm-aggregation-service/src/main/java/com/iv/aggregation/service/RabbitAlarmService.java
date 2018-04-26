package com.iv.aggregation.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iv.aggregation.api.dto.AlarmMessageInput;
import com.iv.aggregation.api.dto.NagiosMessageInput;
import com.iv.aggregation.dao.impl.AlarmProxyCerDaoImpl;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmProxyCerEntity;
import com.iv.aggregation.feign.clients.IAlarmAnalysisClient;
import com.iv.analysis.api.dto.AlarmInfoDto;
import com.iv.common.response.ResponseDto;

/**
 * 南向告警消息队列监听服务
 * @author macheng
 * 2018年4月2日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Service
public class RabbitAlarmService {

	@Autowired
	private NagiosAlarmHandler nagiosAlarmHandler;
	@Autowired
	private ZabbixAlarmHandler zabbixAlarmHandler;
	/*@Autowired
	private ITenantServiceClient tenantServiceClient;*/
	@Autowired
	private IAlarmAnalysisClient alarmAnalysisClient;
	@Autowired
	private AlarmProxyCerDaoImpl alarmProxyCerDao;

	private final static Logger LOGGER = LoggerFactory.getLogger(RabbitAlarmService.class);
	
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		return jsonConverter;
	}

	@SuppressWarnings("deprecation")
	@RabbitListener(containerFactory = "rabbitListenerContainerFactory", bindings = @QueueBinding(exchange = @Exchange(value = "${iv.mq.config.exchange.zabbix}", durable = "true", type = ExchangeTypes.DIRECT), value = @Queue(value = "${iv.mq.config.queue.zabbix}", durable = "true"), key = "${iv.mq.config.routingkey.zabbix}"))
	public void processZabbixQueue(AlarmMessageInput ami) {

		LOGGER.warn("#告警！ [来源：zabbix ]--" + "[token]:" + ami.getToken() + " -- " + new Date().toLocaleString());
		String tenantId = getTenantId(ami.getToken());
		if (null == tenantId) {
			LOGGER.info("【告警内容】" + ami.toString());
			return;
		} else {

			AlarmLifeEntity alarmLifeEntity = null;
			try {
				alarmLifeEntity = zabbixAlarmHandler.alarmProcessing(ami, tenantId);
			} catch (Exception e) {
				LOGGER.error("告警处理失败", e);
				LOGGER.info(ami.toString());
				return;
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
		}
	}

	@SuppressWarnings("deprecation")
	@RabbitListener(containerFactory = "rabbitListenerContainerFactory", bindings = @QueueBinding(exchange = @Exchange(value = "${iv.mq.config.exchange.nagios}", durable = "true", type = ExchangeTypes.DIRECT), value = @Queue(value = "${iv.mq.config.queue.nagios}", durable = "true"), key = "${iv.mq.config.routingkey.nagios}"))
	public void processNagiosQueue(NagiosMessageInput nmi) {
		LOGGER.warn("#告警！ [来源：nagios ]--" + "[租户ID]:" + nmi.getToken() + " -- " + new Date().toLocaleString());
		String tenantId = getTenantId(nmi.getToken());
		if (null == tenantId) {
			LOGGER.info("【告警内容】" + nmi.toString());
			return;
		} else {

			AlarmLifeEntity alarmLifeEntity = null;
			try {
				alarmLifeEntity = nagiosAlarmHandler.alarmProcessing(nmi, tenantId);

			} catch (Exception e) {
				LOGGER.error("告警处理失败", e);
				LOGGER.info(nmi.toString());
				return;
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
		}
	}
	
	private String getTenantId(String token) {
		if (StringUtils.isEmpty(token)) {
			LOGGER.warn("【警告】告警未携带有效的token，已过滤");
			return null;
		}
		AlarmProxyCerEntity proxyCerEntity = alarmProxyCerDao.selectByToken(token);
		if(null == proxyCerEntity) {
			LOGGER.warn("【警告】告警携带非法token，已过滤");
			return null;
			
		}
		return proxyCerEntity.getTenantId();
	}

}
