package com.iv.message.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.iv.common.util.spring.JWTUtil;
import com.iv.common.util.spring.SpringContextUtil;
import com.iv.message.api.dto.MsgReqClear;
import com.iv.message.api.dto.MsgReqQuery;
import com.iv.message.api.dto.MsgReqType;
import com.iv.message.dao.IMsgDao;
import com.iv.message.dao.impl.MsgDaoImpl;

@ServerEndpoint("/ws")
@Component
public class MessageWebSocket {

	public final static Logger LOGGER = LoggerFactory.getLogger(MessageWebSocket.class);
	public final static int FIRST_PAGE = 1; 
	public static Map<Integer, MessageWebSocket> webSocketMap = new ConcurrentHashMap<Integer, MessageWebSocket>();
	private Integer userId;
	private Session session;
	private IMsgDao msgDao;

	private IMsgDao getDao() {
		if(null == msgDao) {
			synchronized (IMsgDao.class) {
				if(null == msgDao) {
					//this.service = new MsgCenterService();
					this.msgDao = SpringContextUtil.getBean(MsgDaoImpl.class);
				}
			}
		}
		return msgDao;
	}
	
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		//Principal user = session.getUserPrincipal();
		//Map<String, Object> map1 = session.getUserProperties();
		Map<String, List<String>> map = session.getRequestParameterMap();
		String token = map.get("token").get(0);
		this.userId = JWTUtil.getJWtJson("Bearer " + token).getInt("userId");
		//this.userId = new Random().nextInt(100);
		MessageWebSocket.webSocketMap.put(this.userId, this);
		sendMsg("连接成功");
	}
	
	@OnClose
	public void onClose(Session session) {
		try {
			session.close();
		} catch (IOException e) {
			LOGGER.error("session关闭失败", e);
		} finally {
			MessageWebSocket.webSocketMap.remove(this.userId);
		}
	}
	
	@OnMessage
	public void onMessage(String request, Session session) {
		MsgReqType type = JSONObject.parseObject(request, MsgReqType.class);
		String response = "";
		switch (type.getType()) {
		case "query":
			MsgReqQuery query = JSONObject.parseObject(request, MsgReqQuery.class);
			response = JSONObject.toJSONString(getDao().selectPageByConfirm(this.userId, FIRST_PAGE, query.getItems(), false));
			break;
		case "clear":
			MsgReqClear clear = JSONObject.parseObject(request, MsgReqClear.class);
			if (StringUtils.isEmpty(clear.getId())) {
				getDao().updateConfirmedPageByUserId(this.userId, FIRST_PAGE, clear.getItems(), true);
			} else {
				getDao().updateConfirmed(Arrays.asList(clear.getId()), true);
			}
			response = JSONObject.toJSONString(getDao().selectPageByConfirm(this.userId, FIRST_PAGE, clear.getItems(), false));
			break;

		default:
			break;
		}
		sendMsg(response);
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
		sendMsg(error.toString());
	}
	
	public void sendMsg(String message) {
		try {
			this.session.getAsyncRemote().sendText(message);
		} catch (Exception e) {
			LOGGER.error("消息发送失败", e);
		}
	}
	
}
