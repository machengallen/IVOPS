package com.iv.aggregation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iv.aggregation.api.constant.NagiosNoticeType;
import com.iv.aggregation.api.dto.NagiosMessageInput;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.util.DataConvert;


@Service
public class NagiosAlarmHandler {
	
	@Autowired
	private CoreService coreService;
	
	public AlarmLifeEntity alarmProcessing(NagiosMessageInput nmi, String tenantId) throws Exception {
		NagiosNoticeType noticeType = NagiosNoticeType.valueOf(nmi.getNotificationType());
		switch (noticeType) {
		case PROBLEM:
			return coreService.alarmTrigger(DataConvert.nagiosAlarmConvert(nmi, tenantId));
			
		case RECOVERY:
			coreService.alarmRecovery(DataConvert.nagiosRecoveryConvert(nmi, tenantId));
			return null;
			
		default:
			return null;
		}
	}
}
