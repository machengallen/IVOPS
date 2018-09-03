package com.iv.aggregation.util;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;

import com.iv.aggregation.api.constant.AgentType;
import com.iv.aggregation.api.dto.AlarmMessageInput;
import com.iv.aggregation.api.dto.NagiosMessageInput;
import com.iv.aggregation.dao.IAlarmLifeDao;
import com.iv.aggregation.dao.impl.AlarmLifeDaoImpl;
import com.iv.aggregation.entity.AlarmRecoveryEntity;
import com.iv.aggregation.entity.AlarmSourceEntity;
import com.iv.common.enumeration.Severity;
import com.iv.dto.AlarmContent;
import com.iv.dto.Note;
import com.iv.dto.TemplateMessageDto;

/**
 * 告警数据转换工具类
 * @author macheng
 * 2018年4月3日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public class DataConvert {

	// 数据库工具实例化
	private static IAlarmLifeDao alarmLifeDao = AlarmLifeDaoImpl.getInstance();

	/**
	 * zabbix告警事件
	 * 
	 * @param ami
	 * @return
	 */
	public static AlarmSourceEntity zabbixAlarmConvert(AlarmMessageInput ami, String tenantId) {

		AlarmSourceEntity alarmSourceEntity = new AlarmSourceEntity();
		alarmSourceEntity.setAgentType(AgentType.Zabbix);
		alarmSourceEntity.setMonitorIp(ami.getMonitorIp());
		alarmSourceEntity.setEventId(Long.parseLong(ami.getEventId()));
		alarmSourceEntity.setTitle(ami.getTitle());
		alarmSourceEntity.setContent(ami.getContent());
		alarmSourceEntity.setHostName(ami.getHostName());
		alarmSourceEntity.setHostIp(ami.getHostIp());
		alarmSourceEntity.setSeverity(Severity.values()[Byte.parseByte(ami.getSeverity())]);
		alarmSourceEntity.setItemKey(ami.getItemKey());
		alarmSourceEntity.setDetails(ami.getDetails());
		alarmSourceEntity.setTenantId(tenantId);
		// alarmSourceEntity.setDetails(new SerialClob(ami.getDetails().toCharArray()));
		// 设置日期
		String date = ami.getEventData();
		Date eventDate = null;
		Time eventTime = null;
		try {
			if (!StringUtils.isEmpty(date)) {
				if (date.matches("[0-9]{4}.[0-9]{2}.[0-9]{2}")) {
					eventDate = Date.valueOf(date.replaceAll("[.]", "-"));
				} else if (date.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")) {
					eventDate = Date.valueOf(date.replaceAll("[/]", "-"));
				}
			}
			if (!StringUtils.isEmpty(ami.getEventTime())) {
				eventTime = Time.valueOf(ami.getEventTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
			eventDate = new Date(System.currentTimeMillis());
			eventTime = new Time(System.currentTimeMillis());
		}
		alarmSourceEntity.setEventData(eventDate);
		alarmSourceEntity.setEventTime(eventTime);

		return alarmSourceEntity;
	}

	/**
	 * zabbix恢复事件
	 * 
	 * @param ami
	 * @return
	 */
	public static AlarmRecoveryEntity zabbixRecoveryConvert(AlarmMessageInput ami, String tenantId) {

		TenantIdHolder.set(tenantId);
		AlarmRecoveryEntity alarmRecoveryEntity = new AlarmRecoveryEntity();
		alarmRecoveryEntity.setAlarmSourceEntity(alarmLifeDao.selectAlarmSourceByEventId(ami.getEventId(),
				ami.getMonitorIp(), AgentType.Zabbix));
		alarmRecoveryEntity.setMonitorIp(ami.getMonitorIp());
		alarmRecoveryEntity.setEventAge(ami.getEventAge());
		alarmRecoveryEntity.setEventRecoveryId(ami.getEventRecoveryId());
		// 设置日期
		String date = ami.getRecoveryData();
		Date recoveryDate = null;
		Time recoveryTime = null;
		try {
			if (!StringUtils.isEmpty(date)) {
				if (date.matches("[0-9]{4}.[0-9]{2}.[0-9]{2}")) {
					recoveryDate = Date.valueOf(date.replaceAll("[.]", "-"));
				} else if (date.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")) {
					recoveryDate = Date.valueOf(date.replaceAll("[/]", "-"));
				}
			}
			if (!StringUtils.isEmpty(ami.getRecoveryTime())) {
				recoveryTime = Time.valueOf(ami.getRecoveryTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
			recoveryDate = new Date(System.currentTimeMillis());
			recoveryTime = new Time(System.currentTimeMillis());
		}
		alarmRecoveryEntity.setRecoveryData(recoveryDate);
		alarmRecoveryEntity.setRecoveryTime(recoveryTime);

		return alarmRecoveryEntity;
	}

	/**
	 * 翻译告警事件微信模板
	 * 
	 * @param alarmSourceEntity
	 * @return
	 */
	public static TemplateMessageDto AlarmTempConvert(AlarmSourceEntity alarmSourceEntity) {

		TemplateMessageDto templateMessageDto = new TemplateMessageDto();
		// alarmTempMessage.setTouser(ami.getTouser());
		// alarmTempMessage.setTemplate_id(ami.getTemplate_id());
		// alarmTempMessage.setUrl(ami.getUrl());

		AlarmContent alarmContent = new AlarmContent();
		alarmContent.setFirst(new Note(alarmSourceEntity.getTitle(), "#FF0000"));
		alarmContent.setKeyword1(new Note(alarmSourceEntity.getHostName(), "#173177"));
		alarmContent.setKeyword2(new Note(alarmSourceEntity.getHostIp(), "#173177"));
		alarmContent.setKeyword3(new Note(alarmSourceEntity.getContent(), "#173177"));
		alarmContent.setKeyword4(new Note(alarmSourceEntity.getSeverity().name(),
				Severity.getColor(alarmSourceEntity.getSeverity().ordinal())));
		alarmContent.setKeyword5(
				new Note(alarmSourceEntity.getEventData() + " " + alarmSourceEntity.getEventTime(), "#173177"));
		alarmContent.setRemark(new Note("请相关运维人员及时关注！", "#0000CD"));

		templateMessageDto.setData(alarmContent);
		return templateMessageDto;
	}

	/**
	 * 翻译恢复事件微信模板
	 * 
	 * @param alarmRecoveryEntity
	 * @return
	 */
	public static TemplateMessageDto recoveryTempConvert(AlarmRecoveryEntity alarmRecoveryEntity) {

		TemplateMessageDto templateMessageDto = new TemplateMessageDto();
		/*
		 * alarmTempMessage.setTouser(ami.getTouser());
		 * alarmTempMessage.setTemplate_id(ami.getTemplate_id());
		 * alarmTempMessage.setUrl(ami.getUrl());
		 */

		AlarmContent alarmContent = new AlarmContent();
		alarmContent.setFirst(new Note(alarmRecoveryEntity.getAlarmSourceEntity().getTitle(), "#32CD32"));
		alarmContent.setKeyword1(new Note(alarmRecoveryEntity.getAlarmSourceEntity().getHostName(), "#173177"));
		alarmContent.setKeyword2(new Note(alarmRecoveryEntity.getAlarmSourceEntity().getHostIp(), "#173177"));
		alarmContent.setKeyword3(new Note(alarmRecoveryEntity.getAlarmSourceEntity().getContent(), "#173177"));
		alarmContent.setKeyword4(new Note(alarmRecoveryEntity.getAlarmSourceEntity().getSeverity().name(),
				Severity.getColor(alarmRecoveryEntity.getAlarmSourceEntity().getSeverity().ordinal())));
		alarmContent.setKeyword5(new Note(alarmRecoveryEntity.getAlarmSourceEntity().getEventData() + " "
				+ alarmRecoveryEntity.getAlarmSourceEntity().getEventTime(), "#173177"));
		alarmContent.setRemark(new Note("告警已解除！", "#32CD32"));

		templateMessageDto.setData(alarmContent);
		return templateMessageDto;
	}

	/**
	 * nagios告警事件
	 * 
	 * @param nmi
	 * @return
	 */
	public static AlarmSourceEntity nagiosAlarmConvert(NagiosMessageInput nmi, String tenantId) {
		AlarmSourceEntity alarmSourceEntity = new AlarmSourceEntity();
		alarmSourceEntity.setAgentType(AgentType.Nagios);
		if (Integer.parseInt(nmi.getHostState()) != 0) {
			// 主机状态故障
			alarmSourceEntity.setEventId(Long.parseLong(nmi.getHostProblemId()));
			alarmSourceEntity.setTitle(nmi.getHostOutput());
			alarmSourceEntity.setContent(nmi.getHostOutput() + " ; " + nmi.getLongHostOutput());
			alarmSourceEntity.setSeverity(Severity.严重);
			alarmSourceEntity.setItemKey(nmi.getHostCheckCommand());
		} else {
			// 服务故障
			alarmSourceEntity.setEventId(Long.parseLong(nmi.getServiceProblemId()));
			alarmSourceEntity.setTitle(nmi.getServiceOutput());
			alarmSourceEntity.setContent(nmi.getServiceOutput() + " ; " + nmi.getLongServiceOutput());
			alarmSourceEntity.setSeverity(convertSeverity(nmi.getServiceState()));
			alarmSourceEntity.setItemKey(nmi.getServiceCheckCommand());
		}

		alarmSourceEntity.setTenantId(tenantId);
		alarmSourceEntity.setMonitorIp(nmi.getMonitorIp());
		alarmSourceEntity.setHostIp(nmi.getHostIp());
		alarmSourceEntity.setHostName(nmi.getHostName());
		alarmSourceEntity.setDetails(nmi.getDetails());
		Date eventDate = new Date(System.currentTimeMillis());
		Time eventTime = new Time(System.currentTimeMillis());
		/*try {
			if (!StringUtils.isEmpty(nmi.getDate())) {
				String[] strs = date.split("-");
				eventDate = Date.valueOf(strs[2] + "-" + strs[1] + "-" + strs[0]);
			}
			if (!StringUtils.isEmpty(nmi.getTime())) {
				eventTime = Time.valueOf(nmi.getTime());
			}
		} catch (Exception e) {
			eventDate = new Date(System.currentTimeMillis());
			eventTime = new Time(System.currentTimeMillis());
			e.printStackTrace();
		}*/
		alarmSourceEntity.setEventData(eventDate);
		alarmSourceEntity.setEventTime(eventTime);
		return alarmSourceEntity;
	}

	/**
	 * nagios恢复事件
	 * 
	 * @param nmi
	 * @return
	 * @throws ParseException
	 */
	public static AlarmRecoveryEntity nagiosRecoveryConvert(NagiosMessageInput nmi, String tenantId) throws ParseException {
		TenantIdHolder.set(tenantId);
		AlarmRecoveryEntity alarmRecoveryEntity = new AlarmRecoveryEntity();
		if (Integer.parseInt(nmi.getHostState()) != 0) {
			// 主机故障
			alarmRecoveryEntity.setAlarmSourceEntity(alarmLifeDao.selectAlarmSourceByEventId(nmi.getLastHostProblemId(),
					nmi.getMonitorIp(), AgentType.Nagios));
			// alarmRecoveryEntity.setEventAge(nmi.getHostDuration());
			alarmRecoveryEntity.setEventRecoveryId(nmi.getHostEventId());
		} else {
			// 服务故障
			alarmRecoveryEntity.setAlarmSourceEntity(alarmLifeDao.selectAlarmSourceByEventId(
					nmi.getLastServiceProblemId(), nmi.getMonitorIp(), AgentType.Nagios));
			// alarmRecoveryEntity.setEventAge(nmi.getServiceDuration());
			alarmRecoveryEntity.setEventRecoveryId(nmi.getServiceEventId());
		}
		alarmRecoveryEntity.setMonitorIp(nmi.getMonitorIp());
		Date recoveryDate = new Date(System.currentTimeMillis());
		Time recoveryTime = new Time(System.currentTimeMillis());
		/*try {
			if (!StringUtils.isEmpty(nmi.getDate())) {
				String[] strs = date.split("-");
				recoveryDate = Date.valueOf(strs[2] + "-" + strs[1] + "-" + strs[0]);
			}
			if (!StringUtils.isEmpty(nmi.getTime())) {
				recoveryTime = Time.valueOf(nmi.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
			recoveryDate = new Date(System.currentTimeMillis());
			recoveryTime = new Time(System.currentTimeMillis());
		}*/
		alarmRecoveryEntity.setRecoveryData(recoveryDate);
		alarmRecoveryEntity.setRecoveryTime(recoveryTime);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long duration = dateFormat.parse(recoveryDate + " " + recoveryTime).getTime()
				- dateFormat.parse(alarmRecoveryEntity.getAlarmSourceEntity().getEventData() + " "
						+ alarmRecoveryEntity.getAlarmSourceEntity().getEventTime()).getTime();
		alarmRecoveryEntity.setEventAge(String.valueOf(duration / 1000 / 60) + "m");
		return alarmRecoveryEntity;
	}

	private static Severity convertSeverity(String severity) {
		int sev = Integer.parseInt(severity);
		switch (sev) {
		case 0:
			sev += 1;
			break;
		case 1:
			sev += 1;
			break;
		case 2:
			sev += 2;
			break;
		case 3:
			sev = 0;
			break;
		default:
			break;
		}

		return Severity.values()[sev];
	}
	
}
