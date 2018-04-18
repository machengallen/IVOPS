package com.iv.aggregation.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.IUserServiceClient;
import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.AccountDto;
import com.iv.enter.dto.UsersWechatsQuery;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.outer.dto.UserInfosDto;
import com.iv.outer.dto.UserOauthDto;

@Component
public class UserServiceClientFallBack implements IUserServiceClient {

	@Override
	public ResponseDto getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserOauthDto bindInfo(String unionid, String loginType) {
		// TODO Auto-generated method stub
		return null;
	}

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
	public List<UserInfosDto> selectUserInfos(UsersWechatsQuery usersWechatsQuery) {
		// TODO Auto-generated method stub
		return null;
	}

}
