package com.iv.aggregation.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iv.aggregation.api.constant.StrategyCycle;


@Entity
@Table(name = "alarm_clean_strategy")
public class AlarmCleanStrategyEntity {

	private int id;
	
	private StrategyCycle cycleType;

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	public StrategyCycle getCycleType() {
		return cycleType;
	}

	public void setCycleType(StrategyCycle cycleType) {
		this.cycleType = cycleType;
	}

}
