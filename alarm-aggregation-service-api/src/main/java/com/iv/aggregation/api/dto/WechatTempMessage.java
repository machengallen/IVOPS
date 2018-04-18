package com.iv.aggregation.api.dto;

/**
 * 微信公众号模板消息体
 * @author macheng
 * 2018年4月3日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public class WechatTempMessage {

	//openID
	private String touser;
	//微信模板id
	private String template_id;
	//详情页面路径
	private String url;
	//告警数据
	private AlarmTempData data;
	
	public WechatTempMessage() {
		
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
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
	public AlarmTempData getData() {
		return data;
	}
	public void setData(AlarmTempData data) {
		this.data = data;
	}
	
}
