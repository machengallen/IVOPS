package com.iv.facade.dto;
import java.util.Set;
import java.util.TreeSet;

import com.iv.facade.constant.AlarmStatus;

/**
 * 
 * @author zhangying
 *
 */
public class AlarmLifeDto {
	private String id;
	//首次推送人列表
	//private Set<HireUser> toHandlers = new HashSet<HireUser>(1);
	//当前处理人
	private int handlerCurrent;
	//最终处理人
	private int handlerLast;
	//平台收到告警触发时间
	private long triDate;
	//平台收到告警恢复时间
	private long recDate;
	//告警信息
	private AlarmSourceDto alarm;
	//告警信息
	private AlarmRecoveryDto recovery;
	//告警状态
	private AlarmStatus alarmStatus;
	//告警升级记录
	private byte upgrade;
	//告警状态日志
	private Set<AlarmLogDto> logs = new TreeSet<AlarmLogDto>();
	//监控类型
	private String itemType;
	//相关历史数据
	private int hostAlarmNum;
	
	
	public AlarmLifeDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getHandlerCurrent() {
		return handlerCurrent;
	}
	public void setHandlerCurrent(int handlerCurrent) {
		this.handlerCurrent = handlerCurrent;
	}
	public int getHandlerLast() {
		return handlerLast;
	}
	public void setHandlerLast(int handlerLast) {
		this.handlerLast = handlerLast;
	}
	public long getTriDate() {
		return triDate;
	}
	public void setTriDate(long triDate) {
		this.triDate = triDate;
	}
	public long getRecDate() {
		return recDate;
	}
	public void setRecDate(long recDate) {
		this.recDate = recDate;
	}
	public AlarmSourceDto getAlarm() {
		return alarm;
	}
	public void setAlarm(AlarmSourceDto alarm) {
		this.alarm = alarm;
	}
	public AlarmRecoveryDto getRecovery() {
		return recovery;
	}
	public void setRecovery(AlarmRecoveryDto recovery) {
		this.recovery = recovery;
	}
	public AlarmStatus getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(AlarmStatus alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	public byte getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(byte upgrade) {
		this.upgrade = upgrade;
	}
	public Set<AlarmLogDto> getLogs() {
		return logs;
	}
	public void setLogs(Set<AlarmLogDto> logs) {
		this.logs = logs;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public int getHostAlarmNum() {
		return hostAlarmNum;
	}
	public void setHostAlarmNum(int hostAlarmNum) {
		this.hostAlarmNum = hostAlarmNum;
	}
	
	
}
