package com.iv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.iv.dto.AlarmInfoTemplate;
import com.iv.dto.AlarmLifeEntityDto;
import com.iv.dto.FormInfoEntityDto;
import com.iv.dto.FormInfoTemplate;
import com.iv.external.service.UserServiceClient;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.util.MailSenderUtil;

@Service
public class EmailService {
	@Value("${iv.mail.subjectAlarm}")
	private String subjectAlarm;
	@Value("${iv.mail.subjectRecovery}")
	private String subjectRecovery;	
	@Value("${iv.mail.fromName}")// 发件人昵称
	private String fromName;	
	@Value("${iv.mail.context}")// 正文
	private String context;
	@Value("${iv.mail.form.fromName}")// 邮件主题
	private String formFromName;
	@Value("${iv.mail.form.subjectToBeTreated}")// 待处理工单
	private String subjectToBeTreated;
	@Value("${iv.mail.form.subjectToBeEvaluated}")// 待评价工单
	private String subjectToBeEvaluated;
	@Value("${iv.mail.form.context}")// 工单内容
	private String formContext;
	@Autowired
	private UserServiceClient userService;
	@Autowired
	private MailSenderUtil mailSenderUtil;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	/**
	 * 发送邮箱验证码
	 * @param email
	 */
	public void emailVCode(String email) {
		int vcode = (int) ((Math.random() * 9 + 1) * 10000);
		// 发送验证码到邮箱
		mailSenderUtil.toMail(new String[] { email }, String.valueOf(vcode));
		long valiDtime = System.currentTimeMillis() + 1000 * 60 * 5;
		stringRedisTemplate.opsForValue().set("vcode", vcode + "&" + valiDtime);
		//session.setAttribute("vcode", vcode + "&" + valiDtime);
	}
	
	/**
	 * 邮件发送告警消息
	 * @param alarmInfoTemplate
	 */
	public void sendToMail(AlarmInfoTemplate alarmInfoTemplate) {
		String subject = alarmInfoTemplate.getEmailType().ordinal() == 0 ? this.subjectRecovery : this.subjectAlarm;		
		mailSenderUtil.alarmToMail(alarmInfoTemplate.getToEmails(), subject, this.fromName, toContent(alarmInfoTemplate.getAlarmLifeEntity()));
	}
	
	/**
	 * 邮件发送告警消息体
	 * @param alarmLifeEntityDto
	 * @return
	 */
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
				.replace("DATE",alarmLifeEntityDto.getTime());
		
		if(null != localAuthDto) {
			result = result.replace("CURRENT",
					localAuthDto.getRealName() == null? localAuthDto.getUserName(): localAuthDto.getRealName());	
		} else {
			result = result.replace("CURRENT","");
		}
		return result;
	}
	
	/**
	 * 邮件发送工单消息
	 * @param alarmInfoTemplate
	 */
	public void sendFormInfoToMail(FormInfoTemplate formInfoTemplate) {
		String subject = formInfoTemplate.getFormSendType().ordinal() == 0 ? this.subjectToBeTreated : this.subjectToBeEvaluated;		
		mailSenderUtil.alarmToMail(formInfoTemplate.getToEmails(), subject, this.formFromName, formtoContent(formInfoTemplate.getFormInfoEntityDto()));
	}
	
	/**
	 * 工单邮件消息主题
	 */
	private String formtoContent(FormInfoEntityDto formInfoEntityDto) {
		String result = this.formContext.replace("TITLE", formInfoEntityDto.getDemandContent())
				.replace("FORMSTATE", formInfoEntityDto.getFormState())
				.replace("UNITCODE", formInfoEntityDto.getUnitCode())
				.replace("TEL", formInfoEntityDto.getTel())
				.replace("DEMANDTYPECODE", formInfoEntityDto.getDemandTypeCode())
				.replace("ID", formInfoEntityDto.getId())
				.replace("PRIORITY", formInfoEntityDto.getPriority())
				.replace("HANDLER", formInfoEntityDto.getHandler())
				.replace("FORMEXPECTENDTIME", formInfoEntityDto.getFormExpectEndTime());
				//.replace("FORMAPPLYTIME",formInfoEntityDto.getFormApplyTime());
		return result;
	}
}
