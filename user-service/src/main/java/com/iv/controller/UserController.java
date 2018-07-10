package com.iv.controller;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.response.ResponseDto;
import com.iv.dto.ErrorMsg;
import com.iv.enter.dto.AccountDto;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.enumeration.LoginType;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.outer.dto.UserOauthDto;
import com.iv.service.IUserService;
import com.iv.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(description = "用户管理接口")
public class UserController implements IUserService {

	@Autowired
	private UserService userService;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	/*@ApiOperation(value = "获取用户信息-前端", notes = "82204")
	@GetMapping("/user/info")
	public ResponseDto userInfo(HttpServletRequest request) {
		ResponseDto dto = new ResponseDto();
		try {
			LocalAuthDto localAuthDto = userService.getUserDetailInfo(request);
			dto.setData(localAuthDto);
			dto.setErrorMsg(ErrorMsg.OK);
			return dto;
		} catch (Exception e) {
			// TODO Auto-generated catch block			
			LOGGER.error("系统内部错误", e);
			dto.setErrorMsg(com.iv.common.response.ErrorMsg.UNKNOWN);
			return dto;
		}
	}*/
	
	/**
	 * 例子
	 */
	@Override
	public ResponseDto getUserInfo(HttpServletRequest request) {
		ResponseDto dto = new ResponseDto();
		try {
			LocalAuthDto localAuthDto = userService.getUserInfo(request);
			dto.setData(localAuthDto);
			dto.setErrorMsg(ErrorMsg.OK);
			return dto;
		} catch (Exception e) {
			// TODO Auto-generated catch block			
			LOGGER.error("系统内部错误", e);
			dto.setErrorMsg(com.iv.common.response.ErrorMsg.UNKNOWN);
			return dto;
		}		
	}
	
	@Override
	@ApiOperation("查看用户是否已绑定三方登录")
	@ApiIgnore
	public UserOauthDto bindInfo(String unionid,LoginType loginType) {
		// TODO Auto-generated method stub
		try {
			return userService.bindInfo(unionid, loginType);			
		}catch(Exception e){			
			// TODO Auto-generated catch block
			LOGGER.error("获取用户绑定信息失败", e);						
		}
		return null;				 
	}	

	@Override
	@ApiOperation("根据id查询用户信息")
	public LocalAuthDto selectLocalAuthById(int userId) {
		// TODO Auto-generated method stub
		try {
			return userService.selectLocalAuthById(userId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("根据id查询用户信息失败", e);
		}
		return null;
	}

	@Override
	@ApiOperation(value = "微信、QQ等绑定平台账号", notes = "82200")
	public ResponseDto bindWechatInfo(@RequestBody AccountDto accountDto) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			return userService.bindWechatInfo(accountDto);			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：微信账号绑定失败", e);
			dto.setErrorMsg(ErrorMsg.WECHAT_BINDING_FAILED);
			return dto;						
		}
	}

	@Override
	@ApiOperation(value = "注册平台账号", notes = "82201")
	public ResponseDto registerAccount(@RequestBody AccountDto accountDto) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto(); 
		try {
			return userService.registerAccount(accountDto);			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：微信注册平台账号失败", e);
			dto.setErrorMsg(ErrorMsg.ACCOUNT_REGISTER_FAILED);
			return dto;						
		}
	}

	@Override
	@ApiOperation("获取用户unionid")
	@ApiIgnore
	public UserOauthDto selectUserWechatUnionid(int userId, LoginType loginType) {
		// TODO Auto-generated method stub
		try {
			return userService.selectUserWechatUnionid(userId, loginType);			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：获取用户登录联合主键失败", e);	
			return null;
		}		
	}

	@Override
	@ApiOperation("根据用户id集合，查询用户信息")
	@ApiIgnore
	public List<LocalAuthDto> selectUserInfos(@RequestBody UsersQueryDto usersWechatsQuery, String tenantId) {
		// TODO Auto-generated method stub
		try {
			return userService.selectUserInfos(usersWechatsQuery,tenantId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：获取用户信息失败", e);	
		}
		return null;
	}

	@Override
	@ApiOperation("根据用户名称查询用户信息")
	@ApiIgnore
	public LocalAuthDto selectLocalauthInfoByName(String userName) {
		// TODO Auto-generated method stub
		try {
			return userService.selectLocalauthInfoByName(userName);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：获取用户信息失败", e);	
		}
		return null;
	}

	@Override
	@ApiOperation(value = "编辑个人信息", notes = "82202")
	public ResponseDto saveOrUpdateUserAuth(@RequestBody AccountDto accountDto){
		// TODO Auto-generated method stub		
		try {			
			return userService.saveOrUpdateLocalAuth(accountDto);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：获取用户信息失败", e);
			ResponseDto dto = new ResponseDto();
			dto.setErrorMsg(ErrorMsg.UPDATE_USERINFO_FAILED);
			return dto;
		}		
	}	

	@Override
	@ApiOperation(value = "找回密码", notes = "82203")
	public ResponseDto findLocalAuthPassWord(@RequestBody AccountDto accountDto) {
		// TODO Auto-generated method stub
		ResponseDto dto = null;
		try {
			return userService.findLocalAuthPassWord(accountDto);
		} catch (Exception e) {						
			dto = new ResponseDto();
			LOGGER.error("系统错误:用户找回密码失败", e);
			dto.setErrorMsg(com.iv.common.response.ErrorMsg.UNKNOWN);
			return dto;
		}
	}

	@Override
	@ApiOperation("根据用户列表id、登录方式查找联合主键")
	@ApiIgnore
	public Set<String> selectUsersWechatUnionid(@RequestBody UsersQueryDto UsersQueryDto) {
		// TODO Auto-generated method stub
		try {
			return userService.selectUsersWechatUnionid(UsersQueryDto);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误:获取联合主键集失败", e);
		}
		return null;
	}

	@ApiOperation("根据用户列表id集合查询用户信息")
	@ApiIgnore
	public List<LocalAuthDto> selectUserInfos(UsersQueryDto usersWechatsQuery) {		
		// TODO Auto-generated method stub
		try {
			return userService.selectUserInfos(usersWechatsQuery);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误：获取用户信息失败", e);	
		}
		return null;		
	}
	
	/**
	 * 用户自动登录
	 */
	@ApiOperation("用户自动登录")
	@RequestMapping(value = "/get/token",method = RequestMethod.GET)
	public ResponseDto getToken(@RequestParam("code") String code) {
		// TODO Auto-generated method stub
		return userService.getToken(code);
	}
	
	/**
	 * 账号解绑微信
	 * @param userId
	 * @return
	 */
	@ApiOperation("微信账号解绑")
	@RequestMapping(value = "/unbind/wechat",method = RequestMethod.GET)
	public ResponseDto unbindWechat(@RequestParam("userId") int userId) {
		try {
			userService.unbindWechat(userId);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("微信解绑失败");
			return ResponseDto.builder(ErrorMsg.WECHAT_UNBINDING_FAILED);
		}
	}

	
}
