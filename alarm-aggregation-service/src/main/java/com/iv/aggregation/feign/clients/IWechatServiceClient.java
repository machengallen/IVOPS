package com.iv.aggregation.feign.clients;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.response.ResponseDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.dto.WeChat;
import com.iv.entity.dto.UserWechatEntityDto;
import com.iv.service.IWechatService;

@FeignClient(value = "wechat-service", fallback = WechatServiceClientFallBack.class)
public interface IWechatServiceClient extends IWechatService {

}

@Component
class WechatServiceClientFallBack implements IWechatServiceClient{

	@Override
	public ResponseDto getWechatLoginCode() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String xxtInterface(WeChat wc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto qrcodeCreate(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWeiXinMessage(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserWechatEntityDto selectUserWechatByUnionid(String unionid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto SendWeChatInfo(TemplateMessageDto templateMessageDto) {
		// TODO Auto-generated method stub
		return null;		
	}

	@Override
	public boolean ifFocusWechat(int userId) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public ResponseDto wxLoginCallBack(String code, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
