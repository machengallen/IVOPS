package com.iv.external.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.AccountDto;
<<<<<<< HEAD
import com.iv.enter.dto.UsersWechatsQuery;
import com.iv.enumeration.LoginType;
=======
import com.iv.enter.dto.UsersQueryDto;
>>>>>>> 34d307b460c662969ff838ffa630338c27886e1d
import com.iv.outer.dto.LocalAuthDto;
import com.iv.outer.dto.UserOauthDto;

@Component
public class UserServiceClientFallBack implements UserServiceClient {
	@Override
	public ResponseDto bindWechatInfo(AccountDto accountDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto registerAccount(AccountDto accountDto) {
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

	@Override
	public List<LocalAuthDto> selectUserInfos(UsersWechatsQuery usersWechatsQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalAuthDto selectLocalauthInfoByName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto saveOrUpdateUserAuth(AccountDto accountDto) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
<<<<<<< HEAD
	public ResponseDto findLocalAuthPassWord(AccountDto accountDto) {
=======
	public List<LocalAuthDto> selectUserInfos(UsersQueryDto usersWechatsQuery) {
>>>>>>> 34d307b460c662969ff838ffa630338c27886e1d
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalAuthDto getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserOauthDto bindInfo(String unionid, LoginType loginType) {
		// TODO Auto-generated method stub
		return null;
	}
}
