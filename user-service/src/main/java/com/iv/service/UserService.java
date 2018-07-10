package com.iv.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.iv.common.enumeration.YesOrNo;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.Constants;
import com.iv.common.util.spring.JWTUtil;
import com.iv.dao.impl.LocalAuthDaoImpl;
import com.iv.dao.impl.UserOauthDaoImpl;
import com.iv.dto.ErrorMsg;
import com.iv.enter.dto.AccountDto;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.entity.LocalAuth;
import com.iv.entity.UserOauth;
import com.iv.entity.dto.UserWechatEntityDto;
import com.iv.enumeration.LoginType;
import com.iv.external.service.IAuthenticationServiceClient;
import com.iv.external.service.SubTenantPermissionServiceClient;
import com.iv.external.service.WechatServiceClient;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.outer.dto.SubTenantRoleDto;
import com.iv.outer.dto.UserOauthDto;
import com.iv.outer.dto.UserWechatInfoDto;

/**
 * 
 * @author zhangying
 * 2018年4月4日
 * aggregation-1.4.0-SNAPSHOT
 */
@Service
public class UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private LocalAuthDaoImpl localAuthDao;
	@Autowired
	private UserOauthDaoImpl userOauthDao;
	@Autowired
	private WechatServiceClient wechatService;
	@Autowired
	private StringRedisTemplate stringtemplate;
	/*@Autowired
	private Md5PasswordEncoder md5PasswordEncoder;*/
	private Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder(); 
	@Autowired
	private SubTenantPermissionServiceClient subTenantPermissionService;
	@Autowired
	private IAuthenticationServiceClient authenticationServiceClient;
	
	/**
	 * 获取用户详细信息：微信信息等
	 * @param request
	 * @return
	 *//*
	public LocalAuthDto getUserDetailInfo(HttpServletRequest request) {
		int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
		LocalAuthDto localAuthDto =  selectLocalAuthById(userId);
		UserOauth userOauth = userOauthDao.selectUserWechatUnionid(userId, LoginType.WECHAT);	
		boolean ifFocusWechat = wechatService.ifFocusWechat(userId);
		localAuthDto.setIfFocusWechat(ifFocusWechat);
		if(null != userOauth) {
			UserWechatEntityDto userWechatEntityDto = wechatService.selectUserWechatByUnionid(userOauth.getUnionid());
			UserWechatInfoDto WechatInfo= new UserWechatInfoDto();
			BeanCopier copy=BeanCopier.create(UserWechatEntityDto.class, UserWechatInfoDto.class, false);
			copy.copy(userWechatEntityDto, WechatInfo, null);
			localAuthDto.setUserWechatInfo(WechatInfo);				
		}
		return localAuthDto;
	}*/
	
	/**
	 * 获取用户信息
	 * @param request
	 * @return
	 */
	public LocalAuthDto getUserInfo(HttpServletRequest request) {
		int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
		LocalAuthDto localAuthDto =  selectLocalAuthById(userId);
		UserOauth userOauth = userOauthDao.selectUserWechatUnionid(userId, LoginType.WECHAT);	
		boolean ifFocusWechat = wechatService.ifFocusWechat(userId);
		localAuthDto.setIfFocusWechat(ifFocusWechat);
		if(null != userOauth) {
			UserWechatEntityDto userWechatEntityDto = wechatService.selectUserWechatByUnionid(userOauth.getUnionid());
			UserWechatInfoDto WechatInfo= new UserWechatInfoDto();
			BeanCopier copy=BeanCopier.create(UserWechatEntityDto.class, UserWechatInfoDto.class, false);
			copy.copy(userWechatEntityDto, WechatInfo, null);
			localAuthDto.setUserWechatInfo(WechatInfo);
		}
		return localAuthDto;
	}
	public UserOauthDto bindInfo(String unionid, LoginType loginType) {
		UserOauth userOauth = userOauthDao.selectUserOauthByUnionid(unionid, loginType);		
		return convertUserOauthDto(userOauth);
	}
	
	public LocalAuthDto selectLocalAuthById(int userId) {
		LocalAuth localAuth = localAuthDao.selectLocalAuthById(userId);		
		return convertLocalAuthDto(localAuth);
	}
	
	public ResponseDto bindWechatInfo(AccountDto accountDto) {
		ResponseDto dto = new ResponseDto();
		UserOauth userOauth = userOauthDao.selectUserOauthByUnionid(accountDto.getUnionid(), accountDto.getLoginType());
		if(null != userOauth) {
			//该微信号已绑定其他账号
			dto.setErrorMsg(ErrorMsg.WECHAT_BINDING_ILLEGAL);
			return dto;
		}
		LocalAuth localAuth = localAuthDao.selectLocalauthInfoByName(accountDto.getUserName());
		if(null == localAuth) {
			//用户不存在
			dto.setErrorMsg(ErrorMsg.USER_NOT_EXIST);
			return dto;
		}
		if(ifLoginTypeExist(localAuth.getId(),LoginType.WECHAT)) {
			//该账号已绑定其他微信
			dto.setErrorMsg(ErrorMsg.ACCOUNT_HAS_WECHAT_BOUNDED);
			return dto;
		}
		//Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		if(StringUtils.isEmpty(accountDto.getPassWord()) || !md5PasswordEncoder.encodePassword(accountDto.getPassWord(), 
				null).equals(localAuth.getPassWord())) {				
			//用户名或密码不正确
			dto.setErrorMsg(ErrorMsg.AUTH_ILLEGAL);
			return dto;
		}	
		//全部校验无误，实现微信账号绑定	
		userOauth = new UserOauth();
		userOauth.setUserId(localAuth.getId());
		userOauth.setLoginType(LoginType.WECHAT);
		userOauth.setUnionid(accountDto.getUnionid());
		/*UserOauth userOauthNew = */
		userOauthDao.saveOrUpdateUserOauth(userOauth);		
		dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
		LOGGER.info(authenticationServiceClient.token(Constants.OAUTH2_CLIENT_BASIC, "password", localAuth.getUserName() + Constants.THREE_PARTY_LOGIN, localAuth.getPassWord()) + "");
		dto.setData(authenticationServiceClient.token(Constants.OAUTH2_CLIENT_BASIC, "password", localAuth.getUserName() + Constants.THREE_PARTY_LOGIN, localAuth.getPassWord()));
		return dto;
	}
	
	public ResponseDto registerAccount(AccountDto accountDto) {

		ResponseDto dto = new ResponseDto();
		// 验证码校验
		if (!vcodeCheck(accountDto.getVcode(), (String) stringtemplate.opsForValue().get("vcode"))) {
			dto.setErrorMsg(ErrorMsg.EMAIL_VCODE_ERROR);
			return dto;
		}
		//自动生成用户名：ivops_ + 注册时间戳 + 0~1000之间的随机数字
		String userName = accountDto.getUserName();
		if(StringUtils.isEmpty(userName)) {
			userName = "ivops_" + System.currentTimeMillis() + randomNumber(1000);
		}	
		// 账号校验
		if (!accountUniqCheck(userName)) {
			dto.setErrorMsg(ErrorMsg.USERNAME_EXIST);
			return dto;
		}		
		// 密码验证并存储
		if (!StringUtils.isEmpty(accountDto.getPassWord()) && !StringUtils.isEmpty(accountDto.getPassWord1())) {
			if (accountDto.getPassWord().equals(accountDto.getPassWord1())) {
				// MD5加密				
				String passWord = md5PasswordEncoder.encodePassword(accountDto.getPassWord(), null);
				LocalAuth localAuth = new LocalAuth();				
				localAuth.setUserName(userName);
				localAuth.setPassWord(passWord);
				localAuth.setTel(accountDto.getTel());
				localAuth.setEmail(accountDto.getEmail());
				localAuth.setNickName(accountDto.getNickName());
				localAuth.setRealName(accountDto.getRealName());
				localAuth = localAuthDao.saveOrUpdateLocalAuth(localAuth);
				if(!StringUtils.isEmpty(accountDto.getUnionid())) {
					UserOauth userOauth = new UserOauth();
					userOauth.setUserId(localAuth.getId());
					userOauth.setLoginType(LoginType.WECHAT);
					userOauth.setUnionid(accountDto.getUnionid());
					userOauthDao.saveOrUpdateUserOauth(userOauth);
				}				
				dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
				//dto.setData(localAuth);
				LOGGER.info(authenticationServiceClient.token(Constants.OAUTH2_CLIENT_BASIC, "password", localAuth.getUserName() + Constants.THREE_PARTY_LOGIN, localAuth.getPassWord()) + "");
				dto.setData(authenticationServiceClient.token(Constants.OAUTH2_CLIENT_BASIC, "password", localAuth.getUserName() + Constants.THREE_PARTY_LOGIN, localAuth.getPassWord()));
				return dto;
			} else {
				dto.setErrorMsg(ErrorMsg.PASSWORD_DIFF);
				return dto;
			}
		} else {
			dto.setErrorMsg(ErrorMsg.INCOMPLETE_INFO);
			return dto;
		}	
	}
	
	/**
	 * 账号唯一性校验
	 * 
	 * @param userName
	 * @return
	 */
	public boolean accountUniqCheck(String userName) {

		if (null != localAuthDao.selectLocalAuthByUserName(userName)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 生成【0,1000)之间的随机数
	 * @param max
	 * @return
	 */
	public int randomNumber(int max) {
		Random ra = new Random();
		int randomNum = ra.nextInt(max);
		return randomNum;
	}
	
	/**
	 * 判断用户是否已绑定某种登录方式
	 * @param localAuthDto
	 * @param loginType
	 * @return
	 */
	@SuppressWarnings("unlikely-arg-type")
	public boolean ifLoginTypeExist(int userId, LoginType loginType) {
		UserOauth userOauth = userOauthDao.selectUserWechatUnionid(userId, loginType);
		if(null != userOauth) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 获取用户unionid
	 * @param userId
	 * @param loginType
	 * @return
	 */
	public UserOauthDto selectUserWechatUnionid(int userId, LoginType loginType) {
		UserOauth userOauth = userOauthDao.selectUserWechatUnionid(userId, loginType);
		return convertUserOauthDto(userOauth);
	}
	
	/**
	 * 根据用户id集合，查询用户信息
	 * @param usersWechatsQuery
	 * @return
	 */
	public List<LocalAuthDto> selectUserInfos(UsersQueryDto usersWechatsQuery,String tenantId){
		LoginType loginType = usersWechatsQuery.getLoginType();
		List<Integer> userIds = usersWechatsQuery.getUserIds();
		LocalAuthDto localAuthDto = new LocalAuthDto();
		List<LocalAuthDto> UserInfos = new ArrayList<LocalAuthDto>();
		for (Integer userId : userIds) {
			LocalAuth localAuth = localAuthDao.selectLocalAuthById(userId);
			localAuthDto = convertLocalAuthDto(localAuth);
			//查询用户微信头像信息
			if(!StringUtils.isEmpty(usersWechatsQuery.getLoginType())) {
				UserOauth userOauth = userOauthDao.selectUserWechatUnionid(userId, loginType);
				if(null != userOauth) {
					UserWechatEntityDto userWechatEntityDto = wechatService.selectUserWechatByUnionid(userOauth.getUnionid());					
					localAuthDto.setHeadimgurl(userWechatEntityDto.getHeadimgurl());
				}				
			}	
			//查询用户角色名称列表信息（为匹配用户角色使用）
			Set<SubTenantRoleDto> subTenantRoleDtos = subTenantPermissionService.selectPersonRole(userId,tenantId);
			if(!CollectionUtils.isEmpty(subTenantRoleDtos)) {
				localAuthDto.setRoles(subTenantRoleDtos);
			}
			UserInfos.add(localAuthDto);
		}
		return UserInfos;
	}
	
	/**
	 * 根据用户名查询用户信息
	 * @param userName
	 * @return
	 */
	public LocalAuthDto selectLocalauthInfoByName(String userName) {
		LocalAuth localAuth = localAuthDao.selectLocalauthInfoByName(userName);
		return convertLocalAuthDto(localAuth);
	}
	
	/**
	 * 更改用户信息（除密码）
	 */
	public ResponseDto saveOrUpdateLocalAuth(AccountDto accountDto) {
		ResponseDto dto = new ResponseDto();
		LocalAuth localAuth = localAuthDao.selectLocalAuthById(accountDto.getUserId());
		//更改真实姓名
		if(!StringUtils.isEmpty(accountDto.getRealName())) {
			localAuth.setRealName(accountDto.getRealName());
		}
		//更改昵称
		if(!StringUtils.isEmpty(accountDto.getNickName())) {
			localAuth.setNickName(accountDto.getNickName());
		}		
		//更改电话
		if(!StringUtils.isEmpty(accountDto.getTel())) {
			localAuth.setTel(accountDto.getTel());
		}
		//更改租户id
		if(!StringUtils.isEmpty(accountDto.getCurTenantId())) {
			localAuth.setCurTenantId(accountDto.getCurTenantId());
		}
		//更改密码
		String passWord = accountDto.getPassWord();
		String passWord1 = accountDto.getPassWord1();
		String passWord2 = accountDto.getPassWord2();
		if (!StringUtils.isEmpty(passWord) && !StringUtils.isEmpty(passWord1) && !StringUtils.isEmpty(passWord2)) {
			// 检验原密码
			if (!md5PasswordEncoder.encodePassword(passWord, null).equals(localAuth.getPassWord())) {
				dto.setErrorMsg(ErrorMsg.AUTH_ILLEGAL);
				return dto;
			}
			if (passWord1.equals(passWord2)) {
				passWord1 = md5PasswordEncoder.encodePassword(passWord1, null);
				localAuth.setPassWord(passWord1);				
			} else {
				dto.setErrorMsg(ErrorMsg.PASSWORD_DIFF);
				return dto;
			}
		}
		localAuth = localAuthDao.saveOrUpdateLocalAuth(localAuth);
		dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
		return dto;
	}
	
	/**
	 * 将localAuth转为localAuthDto
	 * @param localAuth
	 * @return
	 */
	public LocalAuthDto convertLocalAuthDto(LocalAuth localAuth) {
		LocalAuthDto localAuthDto = null;	
		if(null !=localAuth) {
			localAuthDto = new LocalAuthDto();
			BeanCopier copy=BeanCopier.create(LocalAuth.class, LocalAuthDto.class, false);
			copy.copy(localAuth, localAuthDto, null);
		}		
		return localAuthDto;
	}
	
	/**
	 * 将UserOauth转为UserOauthDto
	 * @param localAuth
	 * @return
	 */
	public UserOauthDto convertUserOauthDto(UserOauth userOAuth) {
		UserOauthDto userOAuthDto = null;	
		if(null != userOAuth) {		
			userOAuthDto = new UserOauthDto();
			BeanCopier copy=BeanCopier.create(UserOauth.class, UserOauthDto.class, false);
			copy.copy(userOAuth, userOAuthDto, null);
		}		
		return userOAuthDto;
	}
	
	/**
	 * 找回用户密码
	 * @param accountDto
	 * @return
	 */
	public ResponseDto findLocalAuthPassWord(AccountDto accountDto) {
		LocalAuth localAuth = null;
		switch (accountDto.getPasswordRecStep().ordinal()) {
		case 0://验证账号（用户名或者邮箱或者已验证的手机）			
			localAuth = localAuthDao.selectLocalAuthByUserBaseInfo(accountDto.getUserName());					
			if(null == localAuth) {
				return ResponseDto.builder(ErrorMsg.USER_NOT_EXIST);
			}			
			return ResponseDto.builder(ErrorMsg.OK, localAuth);	
		case 1://验证身份			
			if (StringUtils.isEmpty((String) stringtemplate.opsForValue().get("vcode")) 
					|| StringUtils.isEmpty(accountDto.getVcode()) || !vcodeCheck(accountDto.getVcode(), 
					(String) stringtemplate.opsForValue().get("vcode"))) {
				return ResponseDto.builder(ErrorMsg.EMAIL_VCODE_ERROR);
			} else {
				return ResponseDto.builder(ErrorMsg.OK);
			}
		case 2://新密码校验与保存
			Pattern p = Pattern.compile("(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{8,16}$");
			if (!p.matcher(accountDto.getPassWord()).matches() || !p.matcher(accountDto.getPassWord1()).matches()) {
				return ResponseDto.builder(ErrorMsg.PASSWORD_ILLEGAL);
			} 
			if (!accountDto.getPassWord().equals(accountDto.getPassWord1())) {
				return ResponseDto.builder(ErrorMsg.PASSWORD_DIFF);
			} else {
				localAuth = localAuthDao.selectLocalAuthByUserBaseInfo(accountDto.getUserName());
				localAuth.setPassWord(md5PasswordEncoder.encodePassword(accountDto.getPassWord(), null));
				// 更新用户密码信息
				localAuthDao.saveOrUpdateLocalAuth(localAuth);
				return ResponseDto.builder(ErrorMsg.OK);
			}

		default:
			break;
		}
		return null;
	}
	
	/**
	 * 校验邮箱验证码
	 * @param vcode
	 * @param cache
	 * @return
	 */
	private boolean vcodeCheck(String vcode, String cache) {
		String[] strs = cache.split("&");
		String valiDvcode = strs[0];
		String valiDtime = strs[1];

		if (!vcode.equals(valiDvcode)) {
			return false;
		}
		if (System.currentTimeMillis() > Long.parseLong(valiDtime)) {
			return false;
		}
		return true;
	}
	/**
	 * 根据用户列表id、登录方式查找联合主键
	 * @param UsersQueryDto
	 * @return
	 */
	public Set<String> selectUsersWechatUnionid(UsersQueryDto UsersQueryDto){
		List<UserOauth> userOauths = userOauthDao.selectUsersWechatUnionid(UsersQueryDto.getUserIds(),UsersQueryDto.getLoginType());
		Set<String> unionds = new HashSet<String>();
		for (UserOauth userOauth : userOauths) {
			unionds.add(userOauth.getUnionid());
		}
		return unionds;
	}
	
	/**
	 * 根据用户id集合，查询用户信息
	 * @param usersWechatsQuery
	 * @return
	 */
	public List<LocalAuthDto> selectUserInfos(UsersQueryDto usersWechatsQuery){
		LoginType loginType = usersWechatsQuery.getLoginType();
		List<Integer> userIds = usersWechatsQuery.getUserIds();
		LocalAuthDto localAuthDto = new LocalAuthDto();
		List<LocalAuthDto> UserInfos = new ArrayList<LocalAuthDto>();
		if(!CollectionUtils.isEmpty(userIds)) {
			for (Integer userId : userIds) {
				LocalAuth localAuth = localAuthDao.selectLocalAuthById(userId);
				localAuthDto = convertLocalAuthDto(localAuth);
				//查询用户微信头像信息
				if(!StringUtils.isEmpty(usersWechatsQuery.getLoginType())) {
					UserOauth userOauth = userOauthDao.selectUserWechatUnionid(userId, loginType);
					if(null != userOauth) {
						UserWechatEntityDto userWechatEntityDto = wechatService.selectUserWechatByUnionid(userOauth.getUnionid());					
						localAuthDto.setHeadimgurl(userWechatEntityDto.getHeadimgurl());
					}				
				}	
				/*//查询用户角色名称列表信息（为匹配用户角色使用）
				Set<SubTenantRoleDto> subTenantRoleDtos = subTenantPermissionService.selectPersonRole(userId,tenantId);
				if(!CollectionUtils.isEmpty(subTenantRoleDtos)) {
					localAuthDto.setRoles(subTenantRoleDtos);
				}*/
				UserInfos.add(localAuthDto);
			}
		}else {
			LOGGER.info("*****无用户id*****");
		}
		
		return UserInfos;
	}
	
	/**
	 * 用户自动登录获取token
	 * @param code
	 * @return
	 */
	public ResponseDto getToken(String code) {		
		if(StringUtils.isEmpty(code)) {
			return ResponseDto.builder(ErrorMsg.CODE_NULL);
		}
		try {
			String unionid = wechatService.getUnionid(code);		
			UserOauth userOauth = userOauthDao.selectUserOauthByUnionid(unionid, LoginType.WECHAT);       
			LocalAuth localAuth = localAuthDao.selectLocalAuthById(userOauth.getUserId());    
			Object object = authenticationServiceClient.token(Constants.OAUTH2_CLIENT_BASIC, "password", localAuth.getUserName() + Constants.THREE_PARTY_LOGIN, localAuth.getPassWord());
			return ResponseDto.builder(ErrorMsg.OK, object);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseDto.builder(ErrorMsg.CODE_ILLEGAL);
		}
		
	}
	
	/**
	 * 解绑微信
	 * @param userId
	 */
	public void unbindWechat(int userId) {
		UserOauth userOauth = userOauthDao.selectUserWechatUnionid(userId, LoginType.WECHAT); 
		userOauthDao.deleteUserOauthById(userOauth.getId());
	}
	
	public void focusBindWechat(Integer userId, String unionid) {
		UserOauth userOauth = new UserOauth();
		userOauth.setLoginType(LoginType.WECHAT);
		userOauth.setUnionid(unionid);
		userOauth.setUserId(userId);
		userOauthDao.saveOrUpdateUserOauth(userOauth);
	}
}
