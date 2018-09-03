package com.iv.aggregation.binding;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSONObject;
import com.iv.aggregation.api.dto.AlarmMsgDto;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.feign.clients.ITenantServiceClient;
import com.iv.common.enumeration.NoticeType;
import com.iv.message.api.constant.MsgType;
import com.iv.message.api.dto.MsgInputDto;

@EnableBinding(MsgSource.class)
public class BinderConfiguration {

	private final static Logger LOGGER = LoggerFactory.getLogger(BinderConfiguration.class);
	@Autowired
	private MsgSource msgSource;
	@Autowired
	private ITenantServiceClient tenantServiceClient;

	public void alarmMsgSend(AlarmLifeEntity alarmLifeEntity, List<Integer> userIds, NoticeType noticeType) {
		AlarmMsgDto msgDto = new AlarmMsgDto();
		msgDto.setTenantId(alarmLifeEntity.getAlarm().getTenantId());
		msgDto.setTenantName(tenantServiceClient.getSubEnterpriseByTenantIdBack(msgDto.getTenantId()).getName());
		msgDto.setAlarmId(alarmLifeEntity.getId()); 
		msgDto.setType(noticeType.name());
		msgDto.setTitle(alarmLifeEntity.getAlarm().getTitle());
		msgDto.setHostName(alarmLifeEntity.getAlarm().getHostName());
		msgDto.setHostIp(alarmLifeEntity.getAlarm().getHostIp());
		msgDto.setTriDate(alarmLifeEntity.getTriDate());
		msgDto.setAlarmStatus(alarmLifeEntity.getAlarmStatus().name());
		// 调用用户消息服务
		MsgInputDto msgInput = new MsgInputDto();
		try {
			msgInput.setDataMap(BeanUtils.describe(msgDto));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("告警web消息发送失败", e);
			return;
		}
		msgInput.setMsgType(MsgType.ALARM_TRIGGER);
		msgInput.setUserIds(userIds);
		msgSource.output().send(MessageBuilder.withPayload(JSONObject.toJSONString(msgInput)).build());
	}
}
