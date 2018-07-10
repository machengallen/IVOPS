package com.iv.wechat.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.Constants;
import com.iv.dto.TemplateFormMessageDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.dto.WeChat;
import com.iv.entity.dto.UserWechatEntityDto;
import com.iv.service.IWechatService;
import com.iv.wechat.dto.ErrorMsg;
import com.iv.wechat.dto.QrcodeTicket;
import com.iv.wechat.service.WeChatService;
import com.iv.wechat.util.SignUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author zhangying
 * 2018年4月4日
 * aggregation-1.4.0-SNAPSHOT
 */
@RestController
@Api(description = "微信管理公共接口")
public class WeChatController implements IWechatService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WeChatController.class);
	
	@Value("${iv.wechat.signToken}")
	private String signToken;
	@Autowired
	private WeChatService weChatService;

	@Override
	@ApiOperation(value = "获取微信登录二维码", notes = "82400")
	public ResponseDto getWechatLoginCode() {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			dto.setData(weChatService.getWechatLoginCode());
			dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
			return dto;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：获取登录二维码失败", e);
			dto.setErrorMsg(ErrorMsg.GET_WEXIN_LOGINCODE_FAILED);
			return dto;		
		}
	}
	
	@Override
	@ApiOperation(value = "扫描微信二维码后回调地址")
	@ApiIgnore
	public ResponseDto wxLoginCallBack(String code, HttpServletRequest request) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {			
			return weChatService.wxLoginCallBack(code,request);							
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("微信错误：微信扫描回调失败", e);
			dto.setErrorMsg(ErrorMsg.WECHAT_CALLBACK_FAILURE);	
			return dto;
		}
	}

	@Override
	@ApiOperation(value = "微信服务器认证")
	public String xxtInterface(WeChat wc) {
		// TODO Auto-generated method stub
		String signature = wc.getSignature(); // 微信加密签名
		String timestamp = wc.getTimestamp(); // 时间戳
		String nonce = wc.getNonce();// 随机数
		String echostr = wc.getEchostr();// 随机字符串s
		LOGGER.info(signature);
		LOGGER.info(timestamp);
		LOGGER.info(nonce);
		LOGGER.info(echostr);
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce, signToken)) {
			return echostr;
		} else {
			return null;
		}
	}
	
	@Override
	@ApiOperation(value = "公众号二维码生成", notes = "82401")
	public ResponseDto qrcodeCreate(int userId) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			QrcodeTicket qrcodeTicket = weChatService.qrcodeCreate(userId);
			dto.setData(qrcodeTicket);
			dto.setErrorMsg(ErrorMsg.OK);
			return dto;	
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：公众号二维码加载失败", e);
			dto.setErrorMsg(ErrorMsg.QRCODE_CREATE_FAILED);
			return dto;		
		}
	}

	@Override
	@ApiOperation(value = "微信服务器推送消息入口")	
	public String getWeiXinMessage(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8"); // 微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
		// response.setCharacterEncoding("UTF-8"); //
		// 在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
		// 初始化配置文件
		String respMessage = weChatService.processRequest(request);// 调用CoreService类的processRequest方法接收、处理消息，并得到处理结果；
		// 响应消息
		// 调用response.getWriter().write()方法将消息的处理结果返回给用户
		return respMessage;
	}

	@Override
	@ApiOperation(value = "根据unionid查询微信信息")
	@ApiIgnore
	public UserWechatEntityDto selectUserWechatByUnionid(String unionid) {
		// TODO Auto-generated method stub
		try {
			return weChatService.selectUserWechatByUnionid(unionid);
			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：根据unionid获取微信信息失败", e);
			return null;
		}		
	}

	@Override
	@ApiOperation(value = "发送微信模板消息", notes = "82402")
	public ResponseDto SendWeChatInfo(@RequestBody TemplateMessageDto templateMessageDto) {
		// TODO Auto-generated method stub
		try {
			weChatService.SendWeChatInfo(templateMessageDto);	
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：发送微信模板消息失败", e);
			return ResponseDto.builder(ErrorMsg.SEND_WECHATINFO_FAILED);
		}		
	}

	@Override
	@ApiOperation(value = "根据userId判断是否已关注微信公众号")
	@ApiIgnore
	public boolean ifFocusWechat(int userId) {
		// TODO Auto-generated method stub
		try {
			return weChatService.ifFocusWechat(userId);
			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：查询是否关注微信公众号信息失败", e);
			return false;
		}	
	}

	@Override
	@ApiOperation(value = "发送工单微信模板消息", notes = "82403")
	public ResponseDto SendFormWeChatInfo(@RequestBody TemplateFormMessageDto templateFormMessageDto) {
		// TODO Auto-generated method stub
		try {
			weChatService.SendFormWeChatInfo(templateFormMessageDto);	
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：发送工单微信模板消息失败", e);
			return ResponseDto.builder(ErrorMsg.SEND_FORM_WECHATINFO_FAILED);
		}	
	}

	/**
	 * 获取用户微信统一id
	 */
	@Override
	public String getUnionid(String code) {
		// TODO Auto-generated method stub
		return weChatService.getUnionid(code);
	}

}
	

