package com.iv.message.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iv.common.dto.ObjectPageDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.message.api.dto.MsgFrontQueryDto;
import com.iv.message.api.dto.MsgInputDto;
import com.iv.message.binding.MsgSink;
import com.iv.message.controller.MessageWebSocket;
import com.iv.message.dao.impl.MsgDaoImpl;
import com.iv.message.entity.MsgEntity;

/**
 * 消息服务类
 * 
 * @author macheng 2018年7月16日 message-service
 * 
 */
@Service
@EnableBinding(MsgSink.class)
public class MessageService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
	@Autowired
	private MsgDaoImpl msgDao;

	public ObjectPageDto<MsgEntity> getMsgPaging(MsgFrontQueryDto queryDto) {
		int userId = Integer.parseInt(JWTUtil.getReqValue("userId"));
		return msgDao.selectPageByTypeAndConfirm(userId, queryDto.getCurPage(), queryDto.getIsConfirmed(),
				queryDto.getType(), queryDto.getIsConfirmed());
	}
	
	public void mark(List<String> ids) {
		msgDao.updateConfirmed(ids, true);
	}
	
	public void markAll() {
		int userId = Integer.parseInt(JWTUtil.getReqValue("userId"));
		msgDao.updateConfirmedAllByUserId(userId, true);
	}
	
	public void delete(List<String> ids) {
		msgDao.deleteByIds(ids);
	}

	public void msgInput(MsgInputDto msgInput) {
		EXECUTOR_SERVICE.execute(new Runnable() {

			@Override
			public void run() {
				msgProcess(msgInput);
			}
		});

	}

	@StreamListener(MsgSink.INPUT)
	public void receive(MsgInputDto inputDto) {
		msgProcess(inputDto);
	}

	private void msgProcess(MsgInputDto msgInput) {
		Set<Integer> websocketOnline = MessageWebSocket.webSocketMap.keySet();
		// 存储告警消息
		for (Integer userId : msgInput.getUserIds()) {
			MsgEntity msgEntity = new MsgEntity();
			msgEntity.setMsgDate(System.currentTimeMillis());
			msgEntity.setConfirmed(false);
			msgEntity.setMsgType(msgInput.getMsgType());
			msgEntity.setUserId(userId);
			msgEntity.setDataMap(msgInput.getDataMap());
			msgDao.save(msgEntity);
			ObjectPageDto<MsgEntity> pageDto = new ObjectPageDto<>();
			pageDto.setData(Arrays.asList(msgEntity));
			pageDto.setTotal(1);
			// websocket推送在线用户
			if (websocketOnline.contains(userId)) {
				MessageWebSocket messageWebSocket = MessageWebSocket.webSocketMap.get(userId);
				messageWebSocket.sendMsg(JSONObject.toJSONString(pageDto));
			}
		}
	}

}
