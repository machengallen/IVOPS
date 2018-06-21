package com.iv.aggregation.feign.fallback;

import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.IEmailServiceClient;
import com.iv.common.enumeration.SendType;
import com.iv.common.response.ResponseDto;
import com.iv.dto.AlarmInfoTemplate;
import com.iv.dto.FormInfoTemplate;

@Component
public class EmailServiceClientFallBack implements IEmailServiceClient {

	@Override
	public ResponseDto emailVCode(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto alarmToMail(AlarmInfoTemplate alarmInfoTemplate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto formToMail(FormInfoTemplate formInfoTemplate) {
		// TODO Auto-generated method stub
		return null;
	}

}
