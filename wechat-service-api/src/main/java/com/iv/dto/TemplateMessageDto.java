package com.iv.dto;

import com.iv.common.enumeration.SendType;

/**
 * 模板消息实体类
 * @author macheng
 *
 */
public class TemplateMessageDto {

	//userId
	private int UserId;
	//详情页面路径
	private String redirect_uri;
	//发送主题
	private SendType sendType;
	//告警数据
	private ContentData data;
	
	public TemplateMessageDto() {
		
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}
	

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public ContentData getData() {
		return data;
	}

	public void setData(ContentData data) {
		this.data = data;
	}

	public SendType getSendType() {
		return sendType;
	}

	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}
	
}
