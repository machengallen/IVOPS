package com.iv.wechat.service;

/**
 * 描述：菜单点击事件，处理
 * @author
 */
public class MenuClickService {

	/**
	 * 
	 * 描述：@param eventKey
	 * 描述：@param fromUserName
	 * 描述：@param toUserName
	 * 描述：@return 接受用户点击事件，通过微信推送给用户消息，跳转页面，发送消息等
	 * 作者：herosky
	 */
	public static String getClickResponse(String eventKey, String fromUserName,
			String toUserName) {
		// TODO 判断evetKey事件处理
		if(eventKey.equals("test"))
		{
			
		}
		return null;
	}

}
