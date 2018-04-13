package com.iv.wechat.autoReply;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 用户关注/取消关注事件消息体
 * @author macheng
 *
 */
@Entity
@Table(name="Event_Sub_Message")
public class EventSubMessage extends BaseMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1378265980090394161L;
	//事件类型
	private String event;
	//创建二维码时的二维码id
	private String eventKey;
	//二维码ticket，可用来获取二维码
	private String ticket;
	
	public EventSubMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventSubMessage(String toUserName, String fromUserName, long createTime, String msgType) {
		super(toUserName, fromUserName, createTime, msgType);
		// TODO Auto-generated constructor stub
	}

	public EventSubMessage(String toUserName, String fromUserName, long createTime, String msgType, String event,
			String eventKey, String ticket) {
		super(toUserName, fromUserName, createTime, msgType);
		this.event = event;
		this.eventKey = eventKey;
		this.ticket = ticket;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	
	
}
