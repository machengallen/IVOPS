package com.iv.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.response.ResponseDto;
import com.iv.dto.EmailType;
import com.iv.dto.ErrorMsg;
import com.iv.service.EmailService;
import com.iv.service.IEmailService;
import com.iv.util.MailSenderUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "邮件管理相关接口")
public class IEmailController implements IEmailService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IEmailController.class);
	@Autowired
	private EmailService emailService;	

	@Override
	@ApiOperation("发送邮箱验证码")
	public ResponseDto emailVCode(HttpSession session, String email) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			emailService.emailVCode(session, email);
			dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
			return dto;
		} catch (Exception e) {
			LOGGER.error("系统错误：邮箱验证码发送失败", e);
			dto.setErrorMsg(ErrorMsg.EMAIL_VCODE_SEND_FAILED);
			return dto;
		}
	}

	@Override
	@ApiOperation("使用邮箱发送模板消息")
	public void alarmToMail(String[] toEmails, EmailType emailType, Object object) {
		// TODO Auto-generated method stub		
		try {
			emailService.sendToMail(toEmails, emailType,object);						
		} catch (Exception e) {
			LOGGER.error("系统错误：邮箱发送模板消息失败", e);
		}
	}

}
