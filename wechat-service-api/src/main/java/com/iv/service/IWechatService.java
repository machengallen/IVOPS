package com.iv.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.dto.WeChat;
import com.iv.entity.dto.UserWechatEntityDto;

/**
 * 微信管理公共接口
 * @author zhangying
 * 2018年4月4日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface IWechatService {
	
	/**
	 * 获取微信登录二维码
	 * @return
	 */
	@RequestMapping(value = "/wechat/getWechatLoginCode", method = RequestMethod.GET)
	public ResponseDto getWechatLoginCode();
	
	/**
	 * 扫描微信二维码后回调地址
	 * @return
	 */
	@RequestMapping(value = "/wechat/wxLoginCallBack", method = RequestMethod.GET)
	public ResponseDto wxLoginCallBack(HttpServletRequest request);
	
	
	/**
	 * 微信服务器认证
	 * @param wc
	 * @return
	 */
	@RequestMapping(value = "/wechat", method = RequestMethod.GET)
	public String xxtInterface(WeChat wc);
		
	
	/**
	 * 注册后，如果选择关注公众号，获得公众号带参二维码
	 * @return
	 */
	@RequestMapping(value = "/wechat/qrcodeCreate", method = RequestMethod.GET)
	public ResponseDto qrcodeCreate(@RequestParam int userId);
	
	/**
	 * 微信服务器推送消息入口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/wechat", method = RequestMethod.POST, produces = "text/xml;charset=UTF-8")
	public String getWeiXinMessage(HttpServletRequest request) throws Exception;				
	
	/**
	 * 根据unionid查询微信信息
	 * @param unionid
	 * @return
	 */
	@RequestMapping(value = "/select/userWechatByUnionid", method = RequestMethod.GET)	
	public UserWechatEntityDto selectUserWechatByUnionid(String unionid);
	
	/**
	 * 发送微信模板消息
	 * @param templateMessageDto
	 */
	@RequestMapping(value = "/send/weChatInfo", method = RequestMethod.POST)
	void SendWeChatInfo(TemplateMessageDto templateMessageDto);
	
	/**
	 * 根据userId判断是否已关注微信公众号
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/boolean/ifFocusWechat", method = RequestMethod.GET)
	boolean ifFocusWechat(int userId);
}
