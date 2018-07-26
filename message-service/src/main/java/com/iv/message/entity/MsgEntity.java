package com.iv.message.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iv.message.api.constant.MsgType;

@Entity
@Table(name = "msg")
public class MsgEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3155087351297982168L;
	private String id;
	@JsonIgnore
	private int userId;
	private boolean confirmed;// 消息是否被确认
	//@JsonIgnore
	private long msgDate;// 消息创建时间
	private MsgType msgType;// 消息类型
	private Map<String, String> dataMap;
	
	public MsgEntity() {
		this.dataMap = new HashMap<String, String>(1);
	}
	
	@Id
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public long getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(long msgDate) {
		this.msgDate = msgDate;
	}
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "msg_data", joinColumns = @JoinColumn(name = "msg_id"))
	@MapKeyColumn(name = "data_key")
	@Column(name = "data_value")
	public Map<String, String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	public MsgType getMsgType() {
		return msgType;
	}
	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}
	
}
