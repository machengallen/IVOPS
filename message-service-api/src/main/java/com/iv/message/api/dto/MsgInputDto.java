package com.iv.message.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iv.message.api.constant.MsgType;

public class MsgInputDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1536132422790930311L;
	private List<Integer> userIds;// 用户id
	private MsgType msgType;// 消息类型
	private Map<String, String> dataMap;// 消息业务数据
	
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	public MsgType getMsgType() {
		return msgType;
	}
	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}
	public Map<String, String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	
}
