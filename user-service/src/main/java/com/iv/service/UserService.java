package com.iv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import com.iv.dao.LocalAuthDaoImpl;
import com.iv.dao.impl.UserOauthDaoImpl;
import com.iv.entity.LocalAuth;
import com.iv.entity.UserOauth;
import com.iv.entity.dto.LocalAuthDto;
import com.iv.entity.dto.UserOauthDto;

/**
 * 
 * @author zhangying
 * 2018年4月4日
 * aggregation-1.4.0-SNAPSHOT
 */
@Service
public class UserService {
	
	private Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private LocalAuthDaoImpl localAuthDao;
	@Autowired
	private UserOauthDaoImpl userOauthDao;
	
	public UserOauthDto bindInfo(String unionid, String loginType) {
		UserOauth userOauth = userOauthDao.selectUserOauthByUnionid(unionid, loginType);
		UserOauthDto userOauthDto = new UserOauthDto();
		BeanCopier copy=BeanCopier.create(UserOauth.class, UserOauthDto.class, false);
		copy.copy(userOauth, userOauthDto, null);
		return userOauthDto;
	}
	
	public LocalAuthDto selectLocalauthInfoByName(String localAuthName) {
		LocalAuth localAuth = localAuthDao.selectLocalauthInfoByName(localAuthName);
		LocalAuthDto localAuthDto = new LocalAuthDto();
		BeanCopier copy=BeanCopier.create(LocalAuth.class, LocalAuthDto.class, false);
		copy.copy(localAuth, localAuthDto, null);
		return localAuthDto;
	}
	
	public LocalAuthDto saveOrUpdateLocalAuth(LocalAuthDto localAuthDto) {
		LocalAuth localAuth = new LocalAuth();
		BeanCopier copy=BeanCopier.create(LocalAuthDto.class, LocalAuth.class, false);
		copy.copy(localAuthDto, localAuth, null);
		localAuth = localAuthDao.saveOrUpdateLocalAuth(localAuth);
		localAuthDto = new LocalAuthDto();
		copy=BeanCopier.create(LocalAuth.class, LocalAuthDto.class, false);
		copy.copy(localAuth, localAuthDto, null);
		return localAuthDto;
	}
	
	public LocalAuthDto selectLocalAuthById(int userId) {
		LocalAuth localAuth = localAuthDao.selectLocalAuthById(userId);
		LocalAuthDto localAuthDto = new LocalAuthDto();
		BeanCopier copy=BeanCopier.create(LocalAuth.class, LocalAuthDto.class, false);
		copy.copy(localAuth, localAuthDto, null);
		return localAuthDto;
	}
	
}
