package com.iv.common.enumeration;

/**
 * 权限类型
 * @author zhangying
 * 2018年5月4日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public enum ServiceType {
	/*ALARM, ALARMANALYSIS, STRATEGY, MESSAGE, SCRIPT, SUBTENANT, USER, GROUP, WECHAT, EMAIL, OPERATE;*/
	USER/*用户服务*/, GROUP/*团队服务*/, PERMISSION/*权限服务*/, ALARM/*告警服务*/, SCRIPT/*脚本服务*/, FORM/*工单服务*/, OPERATE/*运营平台*/, SUBTENANT/*租户服务*/, JOBPLATFORM/*作业平台*/;
}
