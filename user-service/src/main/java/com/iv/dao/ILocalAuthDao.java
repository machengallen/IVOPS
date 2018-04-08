package com.iv.dao;

import java.util.List;

import com.iv.entity.LocalAuth;
import com.iv.entity.UserOauth;
import com.iv.entity.UserWechatEntity;
import com.iv.entity.dto.LocalAuthDto;

/**
 * 平台用户数据库工具类
 * @author zhangying
 * 2018年4月8日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface ILocalAuthDao {
	
	LocalAuth selectLocalauthInfoByName(String localAuthName) throws RuntimeException;
	
	LocalAuth saveOrUpdateLocalAuth(LocalAuth user) throws RuntimeException;
	
	LocalAuth selectLocalAuthById(int userId) throws RuntimeException;

	/*LocalAuth selectUserAuthByUserName(String userName) throws RuntimeException;
	
	List<LocalAuth> selectUserAuthByRealName(String realName) throws RuntimeException;
	
	
	
	List<LocalAuth> selectUserByRole(String roleName, String tenantId) throws RuntimeException;
	
	
	
	LocalAuth selectByWechatInfo(UserWechatEntity wechatInfo) throws RuntimeException;
	
	List<LocalAuth> selectAll() throws RuntimeException;
	
	List<LocalAuth> selectByIds(List<Integer> ids) throws RuntimeException;
	
	boolean updateOpenId(int userId, UserWechatEntity wechatEntity) throws RuntimeException;
	
	void unboundWechat(UserWechatEntity wechatEntity) throws RuntimeException;
	
	int wechatBindingAck(int userId) throws RuntimeException;
	
	List<LocalAuth> selectByCondition(String userName, String realName, String tel) throws RuntimeException;*/
	
}
