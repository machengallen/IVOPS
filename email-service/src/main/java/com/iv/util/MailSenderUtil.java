package com.iv.util;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.iv.common.enumeration.SendType;
import com.iv.dto.AlarmLifeEntityDto;
import com.iv.external.service.UserServiceClient;
import com.iv.outer.dto.LocalAuthDto;

@Component
public class MailSenderUtil {


	private static final Logger LOGGER = LoggerFactory.getLogger(MailSenderUtil.class);

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private UserServiceClient userService;
	// 发件人地址
	@Value("${iv.mail.from}")
	private String from;
	// 发件人昵称
	@Value("${iv.mail.fromName}")
	private String fromName;
	// 正文
	@Value("${iv.mail.context}")
	private String context;
	// 标题
	@Value("${iv.mail.subjectAlarm}")
	private String subjectAlarm;
	@Value("${iv.mail.subjectRecovery}")
	private String subjectRecovery;
	@Value("${iv.mail.subjectVCode}")
	private String subjectVCode;
	@Value("${iv.mail.contextVCode}")
	private String contextVCode;

	public void alarmToMail(String[] toEmails, SendType emailType, AlarmLifeEntityDto alarmLifeEntityDto) {
		// 创建邮件消息体
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			messageHelper.setFrom(new InternetAddress(this.from, this.fromName));
			messageHelper.setTo(toEmails);
			messageHelper.setSubject(emailType.ordinal() == 0 ? this.subjectRecovery : this.subjectAlarm);
			messageHelper.setText(toContent(alarmLifeEntityDto), true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			LOGGER.error("邮件消息体创建失败", e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("邮件发送失败：不支持的编码错误", e);
		} catch (Exception e) {
			LOGGER.error("邮件发送异常", e);
		}
	}
	public void toMail(String[] toEmails, String text) {
		// 创建邮件消息体
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			messageHelper.setFrom(new InternetAddress(this.from, this.fromName));
			messageHelper.setTo(toEmails);
			messageHelper.setSubject(subjectVCode);
			messageHelper.setText(contextVCode.replace("VCODE", text), true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("邮件消息体创建失败", e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("邮件发送失败：不支持的编码错误", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("邮件发送异常", e);
		}
	}

	private String toContent(AlarmLifeEntityDto alarmLifeEntityDto) {
		LocalAuthDto localAuthDto = null;
		if(alarmLifeEntityDto.getCurrentHandlerId() != 0) {
			localAuthDto = userService.selectLocalAuthById(alarmLifeEntityDto.getCurrentHandlerId());
		}

		@SuppressWarnings("unlikely-arg-type")
		String result = this.context.replace("TITLE", alarmLifeEntityDto.getTitle())
				.replace("STATUS", "CLOSED".equals(alarmLifeEntityDto.getAlarmStatus())? "已恢复" : "已触发")
				.replace("HOSTNAME", alarmLifeEntityDto.getHostName())
				.replace("HOSTIP", alarmLifeEntityDto.getHostIp())
				.replace("ALARMID", alarmLifeEntityDto.getId())
				.replace("CONTENT", alarmLifeEntityDto.getContent())
				.replace("SEVERITY", alarmLifeEntityDto.getSeverity().toString())
				.replace("UPGRADE", String.valueOf(alarmLifeEntityDto.getUpgrade()))
				.replace("DATE",
						alarmLifeEntityDto.getEventData().toString() + " "
								+ alarmLifeEntityDto.getEventTime().toString());
		
		if(null != localAuthDto) {
			result = result.replace("CURRENT",
					localAuthDto.getRealName() == null? localAuthDto.getUserName(): localAuthDto.getRealName());	
		} else {
			result = result.replace("CURRENT","");
		}
		return result;
	}


}
