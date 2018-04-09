package com.iv.dao;

import java.util.List;

import com.iv.entity.UserWechatEntity;

public interface IUserDao {

public void saveUsersInfo(List<UserWechatEntity> usersInfo) throws RuntimeException;
	
	public void saveUserInfo(UserWechatEntity userInfo) throws RuntimeException;
	
	public List<UserWechatEntity> selectAllUserWechatInfo() throws RuntimeException;
	
	public void deleteUserInfoById(String openId) throws RuntimeException;
	 
	public UserWechatEntity selectUserWechatById(String openId) throws RuntimeException;
	
	public List<UserWechatEntity> selectBatchUserWechatInfo(List<String> openIds) throws RuntimeException;
}
