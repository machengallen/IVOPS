package com.iv.common.util.spring;

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
}
