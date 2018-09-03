package com.iv.aggregation.feign.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.IEmailServiceClient;
import com.iv.common.response.ResponseDto;
import com.iv.dto.AlarmInfoTemplate;
import com.iv.dto.FormInfoTemplate;

@Component
public class EmailServiceClientFallBack implements IEmailServiceClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceClientFallBack.class);
	@Override
	public ResponseDto emailVCode(String email) {
		// TODO Auto-generated method stub
		LOGGER.error("邮箱服务调用失败");
		return null;
	}

	@Override
	public ResponseDto alarmToMail(AlarmInfoTemplate alarmInfoTemplate) {
		// TODO Auto-generated method stub
		LOGGER.error("邮箱服务调用失败");
		return null;
	}

	@Override
	public ResponseDto formToMail(FormInfoTemplate formInfoTemplate) {
		// TODO Auto-generated method stub
		LOGGER.error("邮箱服务调用失败");
		return null;
	}

}
