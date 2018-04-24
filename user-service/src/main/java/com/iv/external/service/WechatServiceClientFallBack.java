package com.iv.external.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.iv.common.response.ResponseDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.dto.WeChat;
import com.iv.entity.dto.UserWechatEntityDto;

@Component
public class WechatServiceClientFallBack implements WechatServiceClient {

	@Override
	public ResponseDto getWechatLoginCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto wxLoginCallBack(HttpServletRequest request) {
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
	public ResponseDto SendWeChatInfo(TemplateMessageDto templateMessage) {
		// TODO Auto-generated method stub
		return null;		
	}

	@Override
	public boolean ifFocusWechat(int userId) {
		// TODO Auto-generated method stub
		return false;
	}

}
