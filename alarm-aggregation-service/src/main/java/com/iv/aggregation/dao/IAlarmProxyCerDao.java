package com.iv.aggregation.dao;

import com.iv.aggregation.entity.AlarmProxyCerEntity;

public interface IAlarmProxyCerDao {

	String save(AlarmProxyCerEntity entity) throws RuntimeException;
	
	AlarmProxyCerEntity selectByToken(String token) throws RuntimeException;
}
