package com.iv.dao;

import com.iv.entity.UserOauth;
import com.iv.enumeration.LoginType;

/**
 * 用户绑定信息数据库工具类
 * @author zhangying
 * 2018年4月8日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface UserOauthDao {

	UserOauth selectUserOauthByUnionid(String unionid, LoginType loginType) throws RuntimeException;
	
	String selectUserWechatUnionid(int userId, String loginType) throws RuntimeException;
}
