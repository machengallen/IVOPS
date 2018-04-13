package com.iv.wechat.autoReply;

/**
 * 微信用户openID列表
 * @author admin
 *
 */
public class UserOpenIdList {

	private Integer total;
	private Integer count;
	private OpenIdData data;
	private String next_openid;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public OpenIdData getData() {
		return data;
	}
	public void setData(OpenIdData data) {
		this.data = data;
	}
	public String getNext_openid() {
		return next_openid;
	}
	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}
	
	
}
