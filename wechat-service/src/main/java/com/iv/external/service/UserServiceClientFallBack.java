package com.iv.external.service;

import org.springframework.stereotype.Component;

import com.iv.common.dto.ResponseDto;
import com.iv.dto.AccountDto;
import com.iv.dto.GroupDto;
import com.iv.dto.GroupUserQuery;
import com.iv.dto.UserQuery;
import com.iv.dto.UserSafeDto;
import com.iv.entity.dto.LocalAuthDto;
import com.iv.entity.dto.UserOauthDto;

@Component
public class UserServiceClientFallBack implements UserServiceClient {

	@Override
	public com.iv.common.response.ResponseDto getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserOauthDto bindInfo(String unionid, String loginType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public com.iv.common.response.ResponseDto bindWechatInfo(AccountDto accountDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public com.iv.common.response.ResponseDto registerAccount(AccountDto accountDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalAuthDto selectLocalAuthById(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String selectUserWechatUnionid(int userId, String loginType) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
