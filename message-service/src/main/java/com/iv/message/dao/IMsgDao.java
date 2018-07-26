package com.iv.message.dao;

import java.util.List;

import com.iv.common.dto.ObjectPageDto;
import com.iv.message.entity.MsgEntity;

public interface IMsgDao {

	MsgEntity save(MsgEntity entity) throws RuntimeException;
	
	ObjectPageDto<MsgEntity> selectPage(int userId, int page, int items) throws RuntimeException;
	
	ObjectPageDto<MsgEntity> selectPageByConfirm(int userId, int page, int items, boolean confirmed) throws RuntimeException;
	
	ObjectPageDto<MsgEntity> selectPageByTypeAndConfirm(int userId, int page, int items, String type, int confirmeType) throws RuntimeException;
	
	int updateConfirmed(List<String> ids, boolean confirmed) throws RuntimeException;
	
	int updateConfirmedPageByUserId(int userId, int page, int items, boolean confirmed) throws RuntimeException;
	
	int updateConfirmedAllByUserId(int userId, boolean confirmed) throws RuntimeException;
	
	int deleteByIds(List<String> ids) throws RuntimeException;
}
