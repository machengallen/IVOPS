package com.iv.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iv.common.response.ResponseDto;
import com.iv.dao.impl.LocalAuthDaoImpl;
import com.iv.dao.impl.UserOauthDaoImpl;
import com.iv.dto.ErrorMsg;
import com.iv.enter.dto.AccountDto;
import com.iv.enter.dto.UsersWechatsQuery;
import com.iv.entity.LocalAuth;
import com.iv.entity.UserOauth;
import com.iv.entity.dto.UserWechatEntityDto;
import com.iv.enumeration.LoginType;
import com.iv.external.service.WechatServiceClient;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.outer.dto.UserInfosDto;
import com.iv.outer.dto.UserOauthDto;

/**
 * 
 * @author zhangying
 * 2018年4月4日
 * aggregation-1.4.0-SNAPSHOT
 */
@Service
public class UserService {
	
	@Autowired
	private LocalAuthDaoImpl localAuthDao;
	@Autowired
	private UserOauthDaoImpl userOauthDao;
	@Autowired
	private WechatServiceClient wechatService;
	/*@Autowired
	private Md5PasswordEncoder md5PasswordEncoder;*/
	public UserOauthDto bindInfo(String unionid, String loginType) {
		UserOauth userOauth = userOauthDao.selectUserOauthByUnionid(unionid, loginType);
		UserOauthDto userOauthDto = new UserOauthDto();
		BeanCopier copy=BeanCopier.create(UserOauth.class, UserOauthDto.class, false);
		copy.copy(userOauth, userOauthDto, null);
		return userOauthDto;
	}
	
	public LocalAuthDto selectLocalAuthById(int userId) {
		LocalAuth localAuth = localAuthDao.selectLocalAuthById(userId);
		LocalAuthDto localAuthDto = new LocalAuthDto();
		BeanCopier copy=BeanCopier.create(LocalAuth.class, LocalAuthDto.class, false);
		copy.copy(localAuth, localAuthDto, null);
		return localAuthDto;
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
		if(ifLoginTypeExist(localAuth,LoginType.WECHAT.toString())) {
			//该账号已绑定其他微信
			dto.setErrorMsg(ErrorMsg.ACCOUNT_HAS_WECHAT_BOUNDED);
			return dto;
		}
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		if(StringUtils.isEmpty(accountDto.getPassWord()) || !encoder.encodePassword(accountDto.getPassWord(), 
				null).equals(accountDto.getPassWord())) {				
			//用户名或密码不正确
			dto.setErrorMsg(ErrorMsg.AUTH_ILLEGAL);
			return dto;
		}	
		//全部校验无误，实现微信账号绑定	
		userOauth = new UserOauth();
		userOauth.setUserId(localAuth.getId());
		userOauth.setLoginType(LoginType.WECHAT);
		userOauth.setUnionid(accountDto.getUnionid());
		localAuth.getUserOauthes().add(userOauth);
		localAuthDao.saveOrUpdateLocalAuth(localAuth);
		dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
		return dto;
	}
	
	public ResponseDto registerAccount(AccountDto accountDto) {

		ResponseDto dto = new ResponseDto();
		/*// 验证码校验
		if (!vcodeCheck(accountDto.getVcode(), (String) session.getAttribute("vcode"))) {
			dto.setErrorMsg(ErrorMsg.EMAIL_VCODE_ERROR);
			return dto;
		}*/
		//自动生成用户名：ivops_ + 注册时间戳 + 0~1000之间的随机数字		
		String userName = "ivops_" + System.currentTimeMillis() + randomNumber(1000);		
		// 密码验证并存储
		if (!StringUtils.isEmpty(accountDto.getPassWord()) && !StringUtils.isEmpty(accountDto.getPassWord1())) {
			if (accountDto.getPassWord().equals(accountDto.getPassWord1())) {
				// MD5加密				
				//String passWord = md5PasswordEncoder.encodePassword(accountDto.getPassword(), null);
				LocalAuth localAuth = new LocalAuth();				
				localAuth.setUserName(userName);
				//localAuth.setPassWord(passWord);
				localAuth.setTel(accountDto.getTel());
				localAuth.setBoundFlag((byte) 1);
				localAuth = localAuthDao.saveOrUpdateLocalAuth(localAuth);
				UserOauth userOauth = new UserOauth();
				userOauth.setUserId(localAuth.getId());
				userOauth.setLoginType(LoginType.WECHAT);
				userOauth.setUnionid(accountDto.getUnionid());
				localAuth.getUserOauthes().add(userOauth);
				localAuthDao.saveOrUpdateLocalAuth(localAuth);
				dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
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
	public boolean ifLoginTypeExist(LocalAuth localAuth, String loginType) {
		Set<UserOauth> userOauths = new HashSet<UserOauth>();
		userOauths = localAuth.getUserOauthes();
		if(userOauths.size() > 0) {
			for (UserOauth userOauth : userOauths) {
				if(userOauth.getLoginType().equals(loginType)) {
					return true;
				}
			}
			return false;
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
	public String selectUserWechatUnionid(int userId, String loginType) {
		return userOauthDao.selectUserWechatUnionid(userId, loginType);
	}
	
	/**
	 * 根据用户id集合，查询用户信息
	 * @param usersWechatsQuery
	 * @return
	 */
	public List<UserInfosDto> selectUserInfos(UsersWechatsQuery usersWechatsQuery){
		String loginType = usersWechatsQuery.getLoginType();
		List<Integer> userIds = usersWechatsQuery.getUserIds();
		UserInfosDto UserInfosDto = new UserInfosDto();
		List<UserInfosDto> UserInfos = new ArrayList<UserInfosDto>();
		for (Integer userId : userIds) {
			LocalAuth localAuth = localAuthDao.selectLocalAuthById(userId);
			String unionid = userOauthDao.selectUserWechatUnionid(userId, loginType);
			UserWechatEntityDto userWechatEntityDto = wechatService.selectUserWechatByUnionid(unionid);
			UserInfosDto.setId(userId);
			UserInfosDto.setRealName(localAuth.getRealName());
			UserInfosDto.setUserName(localAuth.getUserName());
			UserInfosDto.setHeadimgurl(userWechatEntityDto.getHeadimgurl());
			UserInfosDto.setTel(localAuth.getTel());	
			UserInfos.add(UserInfosDto);
		}
		return UserInfos;
	}
	
	
}
