package com.iv.form.binding;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSONObject;
import com.iv.common.util.spring.JWTUtil;
import com.iv.dto.MsgDto;
import com.iv.form.feign.clients.ITenantServiceClient;
import com.iv.message.api.constant.MsgType;
import com.iv.message.api.dto.MsgInputDto;

@EnableBinding(MsgSource.class)
public class BinderConfiguration {

	private final static Logger LOGGER = LoggerFactory.getLogger(BinderConfiguration.class);
	@Autowired
	private MsgSource msgSource;
	@Autowired
	private ITenantServiceClient tenantServiceClient;

	public void msgSend(List<Integer> userIds, String formId, String demandContent, String formState, MsgType type) {
		String tenantId = JWTUtil.getReqValue("curTenantId");
		MsgDto msgDto = new MsgDto();
		msgDto.setFormId(formId);
		msgDto.setDemandContent(demandContent);
		msgDto.setFormState(formState);
		msgDto.setTenantId(tenantId);
		msgDto.setTenantName(tenantServiceClient.getSubEnterpriseByTenantIdBack(tenantId).getName());
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
