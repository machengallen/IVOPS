package com.iv.service;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iv.dto.AlarmLifeEntityDto;
import com.iv.dto.EmailType;
import com.iv.util.MailSenderUtil;

@Service
public class EmailService {
	@Autowired
	private MailSenderUtil mailSenderUtil;
	
	public void emailVCode(HttpSession session, String email) {
		int vcode = (int) ((Math.random() * 9 + 1) * 10000);
		// 发送验证码到邮箱
		mailSenderUtil.toMail(new String[] { email }, String.valueOf(vcode));
		long valiDtime = System.currentTimeMillis() + 1000 * 60 * 5;
		session.setAttribute("vcode", vcode + "&" + valiDtime);
	}
	
	public void sendToMail(String[] toEmails, EmailType emailType, Object object) {
		if(emailType.toString().toLowerCase().contains("alarm") ) {
			AlarmLifeEntityDto alarmLifeEntityDto = (AlarmLifeEntityDto)object;
			mailSenderUtil.alarmToMail(toEmails, emailType, alarmLifeEntityDto);
		}
	}
}
