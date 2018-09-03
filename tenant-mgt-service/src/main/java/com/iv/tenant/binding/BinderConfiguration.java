package com.iv.tenant.binding;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSONObject;
import com.iv.message.api.constant.MsgType;
import com.iv.message.api.dto.MsgInputDto;
import com.iv.tenant.api.dto.PendingMsgDto;
import com.iv.tenant.api.dto.ResultMsgDto;

@EnableBinding(MsgSource.class)
public class BinderConfiguration {

	private final static Logger LOGGER = LoggerFactory.getLogger(BinderConfiguration.class);
	@Autowired
	private MsgSource msgSource;
	
	/**
	 * 发送待审批消息
	 * @param applyTime
	 * @param applicant
	 * @param enterprise
	 * @param subEnterprise
	 */
	public void pendingMsgSend(List<Integer> userIds, String applicant, String enterprise, String subEnterprise, MsgType type) {
		PendingMsgDto pendingMsg = new PendingMsgDto();
		pendingMsg.setApplyTime(System.currentTimeMillis());
		pendingMsg.setApplicant(applicant);
		pendingMsg.setEnterprise(enterprise);
		pendingMsg.setSubEnterprise(subEnterprise);
		MsgInputDto msgInput = new MsgInputDto();
		try {
			Map<String, String> dataMap = BeanUtils.describe(pendingMsg);
			dataMap.remove("class");
			msgInput.setDataMap(dataMap);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("审批结果web消息发送失败", e);
			return;
		}
		msgInput.setMsgType(type);
		msgInput.setUserIds(userIds);
		msgSource.output().send(MessageBuilder.withPayload(JSONObject.toJSONString(msgInput)).build());
	}
	
	public void resultMsgSend(List<Integer> userIds, boolean approved, String enterprise, String subEnterprise, String remark, MsgType type) {
		ResultMsgDto resultMsg = new ResultMsgDto();
		resultMsg.setApproveTime(System.currentTimeMillis());
		resultMsg.setApproved(approved);
		resultMsg.setEnterprise(enterprise);
		resultMsg.setSubEnterprise(subEnterprise);
		resultMsg.setRemark(remark);
		MsgInputDto msgInput = new MsgInputDto();
		try {
			Map<String, String> dataMap = BeanUtils.describe(resultMsg);
			dataMap.remove("class");
			msgInput.setDataMap(dataMap);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("审批结果web消息发送失败", e);
			return;
		}
		msgInput.setMsgType(type);
		msgInput.setUserIds(userIds);
		msgSource.output().send(MessageBuilder.withPayload(JSONObject.toJSONString(msgInput)).build());
		
	}
	
}
