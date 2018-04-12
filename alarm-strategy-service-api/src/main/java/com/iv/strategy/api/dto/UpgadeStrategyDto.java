package com.iv.strategy.api.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.iv.common.enumeration.Severity;

public class UpgadeStrategyDto {

	private List<String> idList;
	// 策略标签
	private String tag;
	// 告警等级
	private List<Severity> severities;
	//告警类型
	private List<String> itemTypes;
	// 升级间隔时间（分钟）
	private String upgradeTime;
	// 告警压缩时间（秒）
	private int delayTime;
	// 团队升级次序
	private List<Short> groupIds;
	private String noticeModel;
	//策略状态日志
	private Set<StrategyLogDto> logs = new HashSet<StrategyLogDto>();

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
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

	public List<Severity> getSeverities() {
		return severities;
	}

	public void setSeverities(List<Severity> severities) {
		this.severities = severities;
	}

	public List<String> getItemTypes() {
		return itemTypes;
	}

	public void setItemTypes(List<String> itemTypes) {
		this.itemTypes = itemTypes;
	}

	public String getNoticeModel() {
		return noticeModel;
	}

	public void setNoticeModel(String noticeModel) {
		this.noticeModel = noticeModel;
	}

	public List<Short> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Short> groupIds) {
		this.groupIds = groupIds;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public boolean no0check() {
		if (StringUtils.isEmpty(upgradeTime)) {
			return false;
		}
		return true;
	}

	public Set<StrategyLogDto> getLogs() {
		return logs;
	}

	public void setLogs(Set<StrategyLogDto> logs) {
		this.logs = logs;
	}
	
}
