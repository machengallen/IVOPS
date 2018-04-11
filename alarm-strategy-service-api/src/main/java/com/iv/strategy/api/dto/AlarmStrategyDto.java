package com.iv.strategy.api.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.iv.common.enumeration.Severity;


public class AlarmStrategyDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4972965453019330201L;
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
	private Set<StrategyLogDto> logs = new HashSet<StrategyLogDto>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

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
	public Set<StrategyLogDto> getLogs() {
		return logs;
	}

	public void setLogs(Set<StrategyLogDto> logs) {
		this.logs = logs;
	}

	@Override
	public String toString() {
		return "AlarmStrategyEntity [tag=" + tag + ", severity=" + severity + ", upgradeTime=" + upgradeTime
				+ ", delayTime=" + delayTime + ", groupIds=" + groupIds + ", noticeModel=" + noticeModel + ", itemType="
				+ itemType + "]";
	}
	

}
