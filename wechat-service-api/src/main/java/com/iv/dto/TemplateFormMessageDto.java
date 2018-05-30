package com.iv.dto;

import java.util.List;

import com.iv.common.enumeration.FormSendType;
/**
 * 工单消息模板
 * @author zhangying
 * 2018年5月28日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public class TemplateFormMessageDto {
	//userId
	private List<Integer> userIds;
	//详情页面路径
	private String redirect_uri;
	//发送主题
	//private FormSendType formSendType;
	//告警数据
	private FormContent data;
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
	
	public FormContent getData() {
		return data;
	}
	public void setData(FormContent data) {
		this.data = data;
	}
	/*public FormSendType getFormSendType() {
		return formSendType;
	}
	public void setFormSendType(FormSendType formSendType) {
		this.formSendType = formSendType;
	}*/
			
}
