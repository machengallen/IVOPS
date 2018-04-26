package com.iv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.iv.dto.AlarmInfoTemplate;
import com.iv.util.MailSenderUtil;

@Service
public class EmailService {
	@Autowired
	private MailSenderUtil mailSenderUtil;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	public void emailVCode(String email) {
		int vcode = (int) ((Math.random() * 9 + 1) * 10000);
		// 发送验证码到邮箱
		mailSenderUtil.toMail(new String[] { email }, String.valueOf(vcode));
		long valiDtime = System.currentTimeMillis() + 1000 * 60 * 5;
		stringRedisTemplate.opsForValue().set("vcode", vcode + "&" + valiDtime);
		//session.setAttribute("vcode", vcode + "&" + valiDtime);
	}
	
	public void sendToMail(AlarmInfoTemplate alarmInfoTemplate) {		
		mailSenderUtil.alarmToMail(alarmInfoTemplate.getToEmails(), alarmInfoTemplate.getEmailType(), alarmInfoTemplate.getAlarmLifeEntity());
	}
}
