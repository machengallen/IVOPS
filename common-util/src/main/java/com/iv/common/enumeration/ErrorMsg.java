package com.iv.common.enumeration;

@Deprecated
public enum ErrorMsg implements com.iv.common.dto.ErrorMsg{

	OK(0,"ok"),
	
	UNKNOWN(4001,"系统未知错误"),
	
	AUTH_ILLEGAL(4002,"用户名或密码错误"),
	
	USERNAME_EXIST(4003,"用户名已存在"),
	
	PASSWORD_DIFF(4004,"密码确认失败"),
	
	USER_NOT_EXIST(4005,"用户名不存在"),
	
	INCOMPLETE_INFO(4006,"请输入完整信息"),
	
	CLAIM_FAILED(4007,"告警认领失败"),
	
	TRANSFER_FAILED(4008,"告警转让失败"),
	
	GET_ALARM_LIFE_FAILED(4009,"告警数据获取失败"),
	
	VISIT_ILLEGAL(4010,"请先登录"),
	
	CONFIG_STRATEGY_FAILED(4011,"配置策略失败"),
	
	QRCODE_CREATE_FAILED(4012,"二维码加载失败"),
	
	WECHAT_UNBOUNDED(4013,"请绑定微信账号"),
	
	WECHAT_BINDING_FAILED(4014,"绑定失败"),
	
	WECHAT_BINDING_ILLEGAL(4015,"该微信号已绑定其他账户"),
	
	PASSWORD_ILLEGAL(4016,"密码非法"),
	
	GROUP_OPS_FAILED(4017,"用户组操作失败"),
	
	LOGOUT_FAILED(4018,"注销失败"),
	
	GET_REPORT_FAILED(4019,"获取报表失败"), 
	
	GET_DISPATCH_FAILED(4020,"获取分派策略失败"),
	
	GET_STRATEGY_FAILED(4021,"获取升级策略失败"),
	
	SPIDER_USER_SAVE_FAILED(4022,"存储用户信息失败"),
	
	SPIDER_USER_GET_FAILED(4023,"获取用户信息失败"),
	
	SPIDER_CONFIG_FAILED(4024,"爬虫任务配置失败"),
	
	SPIDER_CONFIG_GET_FAILED(4025,"获取爬虫任务信息失败"),
	
	SPIDER_USER_DEl_FAILED(4026,"删除用户信息失败"),
	
	SPIDER_ON_FAILED(4027,"开启失败"),
	
	SPIDER_OFF_FAILED(4028,"关闭失败"),
	
	GET_EVENT_FAILED(4029,"获取安全事件失败"),
	
	GET_TEMP_EVENT_FAILED(4030,"获取最新安全事件失败"),
	
	SPIDER_UPLOAD_FAILED(4031,"更新爬虫脚本失败"),
	
	SPIDER_FILE_EMPTY(4032,"上传文件不能为空"),
	
	SPIDER_NAME_REPEAT(4033,"爬虫文件名重复"),
	
	ONLINE_TEST_FAILED(4034,"在线测试异常"),
	
	KEYWORD_ADD_FAILED(4035,"新增关键词失败"),
	
	KEYWORD_DEL_FAILED(4036,"删除关键词失败"),
	
	KEYWORD_GET_FAILED(4037,"查询关键词失败"),
	
	SPIDER_DEL_FAILED(4038,"删除失败"),
	
	SPIDER_DEL_NOFILE_FAILED(4039,"爬虫文件不存在"),
	
	LIBRARY_PAGEING_FAILED(4040,"列表查询失败"),
	
	LIBRARY_QUERY_FAILED(4041,"漏洞查询失败"),
	
	LIBRARY_ADD_FAILED(4042,"漏洞信息录入失败"),
	
	SPIDERS_INFO_GET_FAILED(4043,"爬虫文件查询失败"),
	
	ALARM_CLEAN_SET_FAILED(4044,"更新配置失败"),
	
	GROUP_INFO_FAILED(4045,"用户组获取失败"),
	
	EMAIL_VCODE_SEND_FAILED(4046,"验证码发送失败"),
	
	USERS_LIST_GET_FAILED(4047,"获取用户列表失败"),
	
	EMAIL_VCODE_ERROR(4048,"验证码错误或已失效"),
	
	NOT_ZERO_ERROR(4049,"验证码错误或已失效"),
	
	DISPATCH_DEL_FAILED(4050,"删除策略失败"),
	
	ENABLE_STRATRGY_FAILED(4051,"启用失败"),
	
	DELETE_STRATRGY_FAILED(4052,"删除失败"),
	
	TENANT_CREATE_FAILED(4053,"申请租户失败"),
	
	TENANT_USERADD_FAILED(4054,"添加用户失败"),
	
	TENANT_INFO_GET_FAILED(4055,"获取租户信息失败"),
	
	TENANT_APPROVE_FAILED(4056,"租户审批失败"),
	
	WORKFLOW_USERTASK_GET_FAILED(4057,"获取待审批任务失败"),
	
	WORKFLOW_USERTASK_HIS_GET_FAILED(4058,"获取历史审批任务失败"),
	
	HAVE_NO_PERMISSIONS(4059,"没有访问权限"),
	
	ENTER_LIST_GET_FAILED(4060,"获取企业信息失败"),
	
	PROMOTE_AUTHORITY_FAILED(4061,"权限操作失败"),
	
	SWITCH_TENANT_FAILED(4062,"切换租户失败"),
	
	ALREADY_IN_TENANT(4063,"您已加入该租户，请勿重复申请"),
	
	TENANT_EXIST(4064,"租户已存在"),
	
	TENANT_APPLY_EXIST(4065,"该租户的申请已在审批中"),
	
	CREATE_SUBTENANT_FAILED(4066,"创建项目组失败"),
	
	GET_SUBTENANT_INFO_FAILED(4067,"获取项目组信息失败"),
	
	DEL_SUBTENANT_FAILED(4068,"删除项目组失败"),
	
	GROUP_IN_STRATEGY(4069,"请先删除关联策略"),
	
	TENANT_NOT_EXIST(4070,"租户/项目组不存在或已删除"),
	
	HAVE_NO_PERMISSION(4071,"无相关权限"),
	
	SUB_ENTERPRISE_EXIST(4072,"该项目组已存在"),
	
	FILE_UPLOAD_FAILED(4073,"上传文件失败"),
	
	FILE_EXIST(4074,"文件已存在"),
	
	FILE_EDITOR_FAILED(4075,"文件已存在"),
	
	FILE_LIST_GET_FAILED(4076,"获取文件列表失败"),
	
	FILE_DOWNLOAD_FAILED(4077,"下载文件失败"),
	
	FILE_DELETE_FAILED(4078,"删除文件失败"),
	
	FILE_NOT_EXIST(4079,"文件不存在"),
	
	APPLY_EXIST(4080,"请勿重复提交申请"),
	
	UPLOAD_PARAM_ILLEGAL(4081,"传入文本参数有误"),
	
	TENANTID_UNKNOW(4082,"未知的租户"),
	
	GET_ALARM_ANALYSIS_FAILED(4083,"获取告警分析数据失败"),
	
	SAVE_FAULT_DESCRIPTION_FAILED(4084,"保存故障描述失败"),
	
	GET_FAULT_DESCRIPTIONS_FAILED(4085,"获取故障描述列表失败"),
	
	GET_FAULT_DETAILS_FAILED(4086,"获取故障分析与方案失败"),
	
	GET_SYS_MESSAGE_FAILED(4087,"获取消息失败"),
	
	APPLY_JOIN_ENTER_FAILED(4088,"申请加入租户失败"),
	
	CLEAR_MSG_FAILED(4089,"清除消息失败"),
	
	KEY_WORDS_ALREADY_EXIST(4090,"关键词已存在");
	
	private int code;
    private String msg;
	
	ErrorMsg(int code , String msg){
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
