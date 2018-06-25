package com.iv.permission.feign.fallback;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.AccountDto;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.enumeration.LoginType;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.outer.dto.UserOauthDto;
import com.iv.permission.feign.client.UserServiceClient;

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
	public ResponseDto findLocalAuthPassWord(AccountDto accountDto) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Set<String> selectUsersWechatUnionid(UsersQueryDto UsersQueryDto) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public UserOauthDto selectUserWechatUnionid(int userId, LoginType loginType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserOauthDto bindInfo(String unionid, LoginType loginType) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResponseDto getUserInfo(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalAuthDto> selectUserInfos(UsersQueryDto usersWechatsQuery, String tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalAuthDto> selectUserInfos(UsersQueryDto usersWechatsQuery) {
		// TODO Auto-generated method stub
		return null;
	}
}
