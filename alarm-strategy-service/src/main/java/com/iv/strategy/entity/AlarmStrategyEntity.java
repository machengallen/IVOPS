package com.iv.strategy.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.iv.common.enumeration.Severity;

@Entity
@Table(name = "alarm_strategy")
public class AlarmStrategyEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8163901047420527176L;
	private String id;
	// 策略标签
	private String tag;
	// 告警等级
	private Severity severity;
	// 升级间隔时间（分钟）
	private String upgradeTime;
	// 告警压缩时间（秒）
	private int delayTime;
	// 团队升级次序
	private List<Short> groupIds;
	// 通知方式
	private String noticeModel;
	// 监控类型
	private String itemType;
	
	//策略状态日志
	private Set<StrategyLogEntity> logs = new HashSet<StrategyLogEntity>();

	@Id
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	public List<Short> getGroupIds() {
		return groupIds;
	}
	
	public void setGroupIds(List<Short> groupIds) {
		this.groupIds = groupIds;
	}

	public String getUpgradeTime() {
		return upgradeTime;
	}

	public void setUpgradeTime(String upgradeTime) {
		this.upgradeTime = upgradeTime;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	@Column(nullable = false)
	public String getNoticeModel() {
		return noticeModel;
	}

	public void setNoticeModel(String noticeModel) {
		this.noticeModel = noticeModel;
	}

	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("date")
	public Set<StrategyLogEntity> getLogs() {
		return logs;
	}

	public void setLogs(Set<StrategyLogEntity> logs) {
		this.logs = logs;
	}

	@Override
	public String toString() {
		return "AlarmStrategyEntity [tag=" + tag + ", severity=" + severity + ", upgradeTime=" + upgradeTime
				+ ", delayTime=" + delayTime + ", groupIds=" + groupIds + ", noticeModel=" + noticeModel + ", itemType="
				+ itemType + "]";
	}
	

}
