package com.iv.aggregation.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.iv.aggregation.api.constant.OpsType;

/**
 * 告警日志实体类
 * @author macheng
 * 2018年4月2日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Entity
@Table(name = "alarm_log_info")
public class AlarmLogEntity implements Comparable<AlarmLogEntity> {

	private String id;
	// 操作日期
	private Date date;
	// 操作类型
	private OpsType opsType;
	// 认领人
	private String claimant;
	// 转让人
	private String transferee;
	//备注
	private String remark;
	//告警压缩时间
	private int delayTime;
	//策略升级时间
	private Short upgradeTime;

	public AlarmLogEntity(String id, Date date, OpsType opsType, String claimant, String transferee, //备注
			String remark,int delayTime,Short upgradeTime) {
		super();
		this.id = id;
		this.date = date;
		this.opsType = opsType;
		this.claimant = claimant;
		this.transferee = transferee;
		this.remark = remark;
		this.delayTime = delayTime;
		this.upgradeTime = upgradeTime;
	}

	public AlarmLogEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GenericGenerator(name = "idGen", strategy = "uuid")
	@GeneratedValue(generator = "idGen")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public OpsType getOpsType() {
		return opsType;
	}

	public void setOpsType(OpsType opsType) {
		this.opsType = opsType;
	}

	public String getClaimant() {
		return claimant;
	}

	public void setClaimant(String claimant) {
		this.claimant = claimant;
	}

	public String getTransferee() {
		return transferee;
	}

	public void setTransferee(String transferee) {
		this.transferee = transferee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	
	public Short getUpgradeTime() {
		return upgradeTime;
	}

	public void setUpgradeTime(Short upgradeTime) {
		this.upgradeTime = upgradeTime;
	}

	public static AlarmLogEntity builder(Date date, OpsType opsType, String staff, String remark, int delayTime) {
		AlarmLogEntity logEntity = new AlarmLogEntity();
		logEntity.setRemark(remark);
		logEntity.setDelayTime(delayTime);
		if (null == date) {
			logEntity.setDate(new Date());
		} else {
			logEntity.setDate(date);
		}
		logEntity.setOpsType(opsType);
		if (opsType.equals(OpsType.CLAIM)) {
			logEntity.setClaimant(staff);
		} else if (opsType.equals(OpsType.TRANSFER)) {
			logEntity.setTransferee(staff);
		} else if (opsType.equals(OpsType.ASSIGN)) {
			logEntity.setTransferee(staff);
		}
		return logEntity;
	}

	@Override
	public int compareTo(AlarmLogEntity arg0) {
		// TODO Auto-generated method stub
		return this.getDate().compareTo(arg0.getDate());
	}

}
