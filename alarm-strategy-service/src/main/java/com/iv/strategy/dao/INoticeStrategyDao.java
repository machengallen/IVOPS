package com.iv.strategy.dao;

import com.iv.strategy.entity.NoticeStrategyEntity;

public interface INoticeStrategyDao {

	NoticeStrategyEntity selectByUserId(int userId) throws RuntimeException;
	
	NoticeStrategyEntity save(NoticeStrategyEntity entity) throws RuntimeException;
}
