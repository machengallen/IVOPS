package com.iv.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.dto.ErrorMsg;
import com.iv.common.dto.ResponseDto;
import com.iv.entity.LocalAuth;
import com.iv.entity.dto.LocalAuthDto;
import com.iv.entity.dto.UserOauthDto;
import com.iv.service.UserService;
import com.iv.service.IUserService;

import io.swagger.annotations.Api;

@RestController
@Api(description = "用户与团队管理接口")
public class UserController implements IUserService {

	@Autowired
	private UserService userService;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	/**
	 * 例子
	 */
	@Override
	public ResponseDto getUserInfo() {
		ResponseDto dto = new ResponseDto();
		try {
			//Thread.sleep(3000);
			LocalAuth user = new LocalAuth();
			user.setUserName("mac@inno-view.cn");
			user.setRealName("马成");
			user.setId(1);
			dto.setData(user);
			dto.setErrorMsg(ErrorMsg.OK);
			return dto;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			LOGGER.error("系统内部错误", e);
			dto.setErrorMsg(ErrorMsg.UNKNOWN);
			return dto;
		}
	}
	
	@Override
	public UserOauthDto bindInfo(String unionid,String loginType) {
		// TODO Auto-generated method stub
		Jwt jwt = JwtHelper.decode("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTAxNzQ2MDYsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiNTJhOGM5MTUtMTE2OS00YzU5LWI0MmEtZGY4ZDM0Y2QwZWU0IiwiY2xpZW50X2lkIjoiY2xpZW50Iiwic2NvcGUiOlsiYXBwIl19.GecJM-FHApwznyYl-D3IjB0TpjhdhUXfYv782kfS9vdT0VZsu2HN-MGb-N-6Hf0efZ_mmz54IahJaq3KTw251v4L2O5A1r_iMuUP7GXs_qPHAGn3K1b4l-mNnpJdH5hhS5zYIRqOX2a8DXyI4zD7g8BQL-9PiR3kj9k_z9nW8vY9l2_x5Kyoc-sehxxQ5uQHM3xu6DzOwBpbbER7U_NnUwmcz5nS9YyAexSDnBbZAVpQavL2s1yYQVMJ5Dreq2asXHFbeQHXu5UqVbbTFuOgAylbFJ9K-3nsGAKT9NbzqBPRovI3s_X9HgjrzJHAuojBMeK0QMbvYSbUg2HB7MNNJw");
		try {
			return userService.bindInfo(unionid, loginType);			
		}catch(Exception e){			
			// TODO Auto-generated catch block
			LOGGER.error("获取用户绑定信息失败", e);						
		}
		return null;				 
	}
	
	@Override
	public LocalAuthDto selectLocalauthInfoByName(String localAuthName) {
		// TODO Auto-generated method stub
		try {
			return userService.selectLocalauthInfoByName(localAuthName);			
		}catch(Exception e){			
			// TODO Auto-generated catch block
			LOGGER.error("获取用户绑定信息失败", e);						
		}
		return null;			
	}

	@Override
	public LocalAuthDto saveOrUpdateLocalAuth(LocalAuthDto localAuthDto) {
		// TODO Auto-generated method stub
		try {
			return userService.saveOrUpdateLocalAuth(localAuthDto);
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("保存用户绑定登录信息失败", e);
			return null;
		}
	}

	@Override
	public LocalAuthDto selectLocalAuthById(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			return userService.selectLocalAuthById(userId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("根据id查询用户信息失败", e);
		}
		return null;
	}

	

	/*@Override
	public ResponseDto userOps(HttpSession session, UserSafeDto modifyInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupDetailInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupUserPageInfo(GroupUserQuery groupUserQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupsInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupOps(GroupDto groupDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto qrcodeCreate(HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public ResponseDto getUsersInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getUsersPageInfo(UserQuery userQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getUsersPageFromParent(UserQuery userQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getUsersWechat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto resetPassword(UserSafeDto userInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUsersInfo(UserWechatDto userWechatEntities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserInfo(UserWechatEntityDto userWechatEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocalAuthDto selectUserAuthById(int eventKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserWechatEntityDto selectUserWechatById(String openId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserInfoById(String openId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unboundWechat(UserWechatEntityDto wechatEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean updateOpenId(UserWechatInfo weChatInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalAuthDto selectUserByOpenId(String openId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalAuthDto selectUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto saveUserAuth(LocalAuthDto localAuth) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		return dto;
		
	}
*/

	

}
