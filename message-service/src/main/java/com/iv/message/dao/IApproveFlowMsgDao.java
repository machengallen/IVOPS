package com.iv.message.dao;

import java.util.List;

import com.iv.common.dto.ObjectPageDto;
import com.iv.message.entity.ApproveFlowMsgEntity;


public interface IApproveFlowMsgDao {

	void save(ApproveFlowMsgEntity entity) throws RuntimeException;
	
	ObjectPageDto selectByUserId(int userId, int firstResult, int maxResults) throws RuntimeException;
	
	void updateConfirmed(List<String> id, boolean confirmed) throws RuntimeException;
	
	void updateConfirmedAllByUserId(int userId, boolean confirmed) throws RuntimeException;
}
