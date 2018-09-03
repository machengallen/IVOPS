package com.iv.aggregation.feign.clients;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.response.ResponseDto;
import com.iv.dto.TemplateFormMessageDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.dto.WeChat;
import com.iv.entity.dto.UserWechatEntityDto;
import com.iv.service.IWechatService;

@FeignClient(value = "wechat-service", fallback = WechatServiceClientFallBack.class)
public interface IWechatServiceClient extends IWechatService {

}

@Component
class WechatServiceClientFallBack implements IWechatServiceClient{

	private final static Logger LOGGER = LoggerFactory.getLogger(WechatServiceClientFallBack.class);
	@Override
	public ResponseDto getWechatLoginCode() {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;
	}


	@Override
	public String xxtInterface(WeChat wc) {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;
	}

	@Override
	public ResponseDto qrcodeCreate(int userId) {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;
	}

	@Override
	public String getWeiXinMessage(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;
	}

	@Override
	public UserWechatEntityDto selectUserWechatByUnionid(String unionid) {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;
	}

	@Override
	public ResponseDto SendWeChatInfo(TemplateMessageDto templateMessageDto) {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;		
	}

	@Override
	public boolean ifFocusWechat(int userId) {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return false;
	}


	@Override
	public ResponseDto wxLoginCallBack(String code, HttpServletRequest request) {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;
	}


	@Override
	public ResponseDto SendFormWeChatInfo(TemplateFormMessageDto templateFormMessageDto) {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;
	}


	@Override
	public String getUnionid(String code) {
		// TODO Auto-generated method stub
		LOGGER.error("微信服务调用失败");
		return null;
	}
	
}
