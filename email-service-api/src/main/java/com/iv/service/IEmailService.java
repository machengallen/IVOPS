package com.iv.service;


import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.enumeration.SendType;
import com.iv.common.response.ResponseDto;


public interface IEmailService {

	/**
	 * 发送邮箱验证码
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/register/vcode", method = RequestMethod.GET)
	ResponseDto emailVCode(HttpSession session, @RequestParam String email);
	
	/**
	 * 邮箱发送告警信息
	 */
	@RequestMapping(value = "/send/templateMessage", method = RequestMethod.GET)
	void alarmToMail(String[] toEmails, SendType emailType, Object object);
}
