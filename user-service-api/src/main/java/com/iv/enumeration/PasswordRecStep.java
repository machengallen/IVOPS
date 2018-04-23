package com.iv.enumeration;

/**
 * 密码找回步骤
 * @author zhangying
 * 2018年4月19日
 * aggregation-1.4.0-SNAPSHOT
 */
public enum PasswordRecStep {
	FILLIN,/*填写账户*/
	AUTHENTICATION,/*验证身份*/
	SETNEWPASSWORD;/*设置新密码*/
}
