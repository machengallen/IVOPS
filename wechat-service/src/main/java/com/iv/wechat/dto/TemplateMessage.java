package com.iv.wechat.dto;

import com.iv.dto.ContentData;

/**
 * 告警发送信息封装
 * @author zhangying
 * 2018年4月12日
 * aggregation-1.4.0-SNAPSHOT
 */
public class TemplateMessage {
	//用户公众号openId
	private String openId;
	//微信模板id
	private String template_id;
	//详情页面路径
	private String url;
	//告警数据
	private ContentData data;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ContentData getData() {
		return data;
	}
	public void setData(ContentData data) {
		this.data = data;
	}
	
	
	
}
