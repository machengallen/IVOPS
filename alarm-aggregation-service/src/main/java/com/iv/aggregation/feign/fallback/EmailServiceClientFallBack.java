package com.iv.aggregation.feign.fallback;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.IEmailServiceClient;
import com.iv.common.enumeration.SendType;
import com.iv.common.response.ResponseDto;

@Component
public class EmailServiceClientFallBack implements IEmailServiceClient {

	@Override
	public ResponseDto emailVCode(HttpSession session, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void alarmToMail(String[] toEmails, SendType emailType, Object object) {
		// TODO Auto-generated method stub
		
	}


}
