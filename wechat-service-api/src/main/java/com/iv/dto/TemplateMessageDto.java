package com.iv.dto;

import java.util.List;

import com.iv.common.enumeration.SendType;

/**
 * 模板消息实体类
 * @author macheng
 *
 */
public class TemplateMessageDto {

	//userId
	private List<Integer> userIds;
	//详情页面路径
	private String redirect_uri;
	//发送主题
	private SendType sendType;
	//告警数据
	private AlarmContent data;
	
	public TemplateMessageDto() {
		
	}	
	
	public List<Integer> getUserIds() {
		return userIds;
	}
	
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	
	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public AlarmContent getData() {
		return data;
	}

	public void setData(AlarmContent data) {
		this.data = data;
	}

	public SendType getSendType() {
		return sendType;
	}

	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}
	
}
