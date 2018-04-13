package com.iv.wechat.dao;

import java.util.List;

import com.iv.wechat.autoReply.EventSubMessage;

public interface IMessageDao {

	public void saveEventSubMessage(EventSubMessage esm) throws RuntimeException;

	public List<EventSubMessage> selectEventSubMessageByEventKey(String eventKey) throws RuntimeException;

	public void deleteSubMessage(String fromUserName) throws RuntimeException;


}
