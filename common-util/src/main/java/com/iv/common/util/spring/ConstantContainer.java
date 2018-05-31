package com.iv.common.util.spring;

/**
 * @author macheng
 * 2018年4月25日
 * common-util
 * 
 */
public interface ConstantContainer {

	/**
	 * 游客租户标识符
	 */
	String TOURIST = "tourist";
	
	/**
	 * 共享DB名
	 */
	String TENANT_SHARED_ID = "iv_ops_shared";
	
	/**
	 * 基础知识库DB名
	 */
	String KNOWLEDGE_DB_ID = "iv_ops_knowledge";
	
	/**
	 * 基础知识数据库初始化sql脚本名
	 */
	String KNOWLEDGE_SQL_SCRIPT_NAME = "iv_ops_knowledge.sql";
	
	/**
	 * 业务数据库初始化sql脚本名
	 */
	String BUS_SQL_SCRIPT_NAME = "init_table.sql";
	
	/**
	 * 各服务初始化数据库名
	 */
	String ALARM_AGGREGATION_DB = "alarm_aggregation_service";
	
	String MESSAGE_DB = "message_service";
	
	String ALARM_STRATEGY_DB = "alarm_strategy_service";
	
	String OPERATION_SCRIPT_DB = "operation_script_service";
}
