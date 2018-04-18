package com.iv.aggregation.dao;

import java.util.List;

import com.iv.aggregation.api.constant.NoticeType;
import com.iv.aggregation.entity.AlarmMsgEntity;
import com.iv.common.dto.ObjectPageDto;


public interface IAlarmMsgDao {

	void save(AlarmMsgEntity entity, String tenantId) throws RuntimeException;

	ObjectPageDto selectAlarmByUserId(int userId, int firstResult, int maxResults) throws RuntimeException;
	
	ObjectPageDto selectRecoveryByUserId(int userId, int firstResult, int maxResults) throws RuntimeException;
	
	ObjectPageDto selectAllByUserId(int userId, int firstResult, int maxResults) throws RuntimeException;
	
	AlarmMsgEntity selectUnconfirmedMsgById(String alarmId, int userId, String tenantId) throws RuntimeException;
	
	void updateConfirmed(List<String> ids, boolean confirmed) throws RuntimeException;
	
	void updateConfirmedAllByUserId(int userId, boolean confirmed, NoticeType type) throws RuntimeException;
	
	void updateConfirmedAllByUserId(int userId, boolean confirmed) throws RuntimeException;
}
