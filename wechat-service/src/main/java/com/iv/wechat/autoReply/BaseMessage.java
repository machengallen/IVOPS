package com.iv.wechat.autoReply;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/** 
 * 消息基类（普通用户-> 公众帐号）
 *  
 * @author
 */

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="Base_Message")
public class BaseMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2159635957343160824L;
	// 公众号微信号
    private String ToUserName;  
    // 发送者微信号  
    private String FromUserName;  
    // 消息创建时间 （整型）  
    private long CreateTime;  
    // 消息类型(event/text/music/)
    private String MsgType;  
    
    public BaseMessage(String toUserName, String fromUserName, long createTime, String msgType) {
		super();
		ToUserName = toUserName;
		FromUserName = fromUserName;
		CreateTime = createTime;
		MsgType = msgType;
	}
    
	public BaseMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getToUserName() {  
        return ToUserName;  
    }  
  
    public void setToUserName(String toUserName) {  
        ToUserName = toUserName;  
    }  
  
    @Id
	@Column
    public String getFromUserName() {  
        return FromUserName;  
    }  
  
    public void setFromUserName(String fromUserName) {  
        FromUserName = fromUserName;  
    }  
  
    public long getCreateTime() {  
        return CreateTime;  
    }  
  
    public void setCreateTime(long createTime) {  
        CreateTime = createTime;  
    }  
  
    public String getMsgType() {  
        return MsgType;  
    }  
  
    public void setMsgType(String msgType) {  
        MsgType = msgType;  
    }  
  
}
