package com.iv.wechat.dao;

import java.util.List;
import java.util.Set;

import com.iv.wechat.entity.UserWechatEntity;

/**
 * 微信信息数据库工具类
 * @author zhangying
 * 2018年4月8日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface UserWechatDao {

	void saveOrUpdateUserWechat(UserWechatEntity userWechatEntity) throws RuntimeException; 
	
	UserWechatEntity selectUserWechatByUnionid(String unionid) throws RuntimeException;
	
	List<UserWechatEntity> selectUserWechatsByUnionids(Set<String> unionids) throws RuntimeException;
	
	void saveOrUpdateUserWechats(List<UserWechatEntity> userWechatEntitys) throws RuntimeException;
}
