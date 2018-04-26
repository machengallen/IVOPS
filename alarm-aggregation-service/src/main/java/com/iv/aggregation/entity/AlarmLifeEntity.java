package com.iv.aggregation.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import com.iv.aggregation.api.constant.OpsType;
import com.iv.common.enumeration.AlarmStatus;

/**
 * 告警生命周期信息体
 * @author macheng
 * 2018年4月2日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Entity
@Table(name = "Alarm_Life")
public class AlarmLifeEntity implements Serializable{

	/**
	 *    
	 */
	private static final long serialVersionUID = -5590798499540987761L;
	private String id;
	//首次推送人列表
	private Set<Integer> toHireUserIds = new HashSet<Integer>(1);
	//当前处理人
	private int handlerCurrent;
	//最终处理人
	private int handlerLast;
	//平台收到告警触发时间
	private long triDate;
	//平台收到告警恢复时间
	private long recDate;
	//告警被响应时间
	private long resDate;
	//告警信息
	private AlarmSourceEntity alarm;
	//告警信息
	private AlarmRecoveryEntity recovery;
	//告警状态
	private AlarmStatus alarmStatus;
	//告警升级记录
	private byte upgrade;
	//告警状态日志
	private Set<AlarmLogEntity> logs = new TreeSet<AlarmLogEntity>();
	//监控类型
	private String itemType;
	//相关历史数据
	private int hostAlarmNum;
	//告警事件触发时间
	//private AlarmEventDateEntity alarmEvent;
	public AlarmLifeEntity(Set<Integer> toHireUserIds, int handlerCurrent,
			int handlerLast, long triDate, long recDate, AlarmSourceEntity alarm,
			AlarmRecoveryEntity recovery, AlarmStatus alarmStatus, byte upgrade, Set<AlarmLogEntity> logs,
			String itemType, int hostAlarmNum) {
		super();
		this.toHireUserIds = toHireUserIds;
		this.handlerCurrent = handlerCurrent;
		this.handlerLast = handlerLast;
		this.triDate = triDate;
		this.recDate = recDate;
		this.alarm = alarm;
		this.recovery = recovery;
		this.alarmStatus = alarmStatus;
		this.upgrade = upgrade;
		this.logs = logs;
		this.itemType = itemType;
		this.hostAlarmNum = hostAlarmNum;
	}
	
	public AlarmLifeEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Id
	@GenericGenerator(name = "alarmGen", strategy = "com.iv.aggregation.util.AlarmGenerator")
	@GeneratedValue(generator = "alarmGen")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@OneToOne//(orphanRemoval = true)
	//@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name = "alarmId")
	public AlarmSourceEntity getAlarm() {
		return alarm;
	}
	public void setAlarm(AlarmSourceEntity alarm) {
		this.alarm = alarm;
	}
	@OneToOne(orphanRemoval = true)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name = "recoveryId")
	public AlarmRecoveryEntity getRecovery() {
		return recovery;
	}
	public void setRecovery(AlarmRecoveryEntity recovery) {
		this.recovery = recovery;
	}
	public byte getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(byte upgrade) {
		this.upgrade = upgrade;
	}
	public int getHostAlarmNum() {
		return hostAlarmNum;
	}
	public void setHostAlarmNum(int hostAlarmNum) {
		this.hostAlarmNum = hostAlarmNum;
	}
	@ElementCollection(fetch = FetchType.EAGER)
	public Set<Integer> getToHireUserIds() {
		return toHireUserIds;
	}
	
	public void setToHireUserIds(Set<Integer> toHireUserIds) {
		this.toHireUserIds = toHireUserIds;
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
	@Enumerated(EnumType.STRING)
	public AlarmStatus getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(AlarmStatus alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("date")
	public Set<AlarmLogEntity> getLogs() {
		return logs;
	}
	public void setLogs(Set<AlarmLogEntity> logs) {
		this.logs = logs;
	}
	public void upgrade(String dispatcher,Short upgradeTime){
		this.logs.add(new AlarmLogEntity(null, new Date(), OpsType.UPGRADE, null, dispatcher, null, 0, upgradeTime));
	}
	/*public long getTriDate() {
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
	}*/

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
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

	public long getResDate() {
		return resDate;
	}

	public void setResDate(long resDate) {
		this.resDate = resDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;		
		AlarmLifeEntity other = (AlarmLifeEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
