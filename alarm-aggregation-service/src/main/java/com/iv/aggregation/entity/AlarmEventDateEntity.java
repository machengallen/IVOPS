package com.iv.aggregation.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * @author macheng
 * 2018年4月2日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Entity
@Table(name= "Event_Date")
public class AlarmEventDateEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	//平台收到告警触发时间
	private long triDate;
	//平台收到告警恢复时间
	private long recDate;
	//告警被响应时间
	private long resDate;	
	public AlarmEventDateEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AlarmEventDateEntity(String id, long triDate, long recDate, long resDate) {
		super();
		this.id = id;
		this.triDate = triDate;
		this.recDate = recDate;
		this.resDate = resDate;		
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
}
