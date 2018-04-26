package com.iv.message.dao;

import java.util.List;

import com.iv.common.dto.ObjectPageDto;
import com.iv.common.enumeration.NoticeType;
import com.iv.message.entity.AlarmMsgEntity;


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
