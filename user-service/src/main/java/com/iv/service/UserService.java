package com.iv.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
	@Autowired
	private Md5PasswordEncoder md5PasswordEncoder;
	public UserOauthDto bindInfo(String unionid, String loginType) {
		UserOauth userOauth = userOauthDao.selectUserOauthByUnionid(unionid, loginType);
		UserOauthDto userOauthDto = new UserOauthDto();
		BeanCopier copy=BeanCopier.create(UserOauth.class, UserOauthDto.class, false);
		copy.copy(userOauth, userOauthDto, null);
		return userOauthDto;
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
	public List<LocalAuthDto> selectUserInfos(UsersWechatsQuery usersWechatsQuery){
		String loginType = usersWechatsQuery.getLoginType();
		List<Integer> userIds = usersWechatsQuery.getUserIds();
		LocalAuthDto localAuthDto = new LocalAuthDto();
		List<LocalAuthDto> UserInfos = new ArrayList<LocalAuthDto>();
		for (Integer userId : userIds) {
			LocalAuth localAuth = localAuthDao.selectLocalAuthById(userId);
			localAuthDto = convertLocalAuthDto(localAuth);
			if(!StringUtils.isEmpty(usersWechatsQuery.getLoginType())) {
				String unionid = userOauthDao.selectUserWechatUnionid(userId, loginType);
				UserWechatEntityDto userWechatEntityDto = wechatService.selectUserWechatByUnionid(unionid);
				localAuthDto.setHeadimgurl(userWechatEntityDto.getHeadimgurl());
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
			localAuth.setUserName(accountDto.getRealName());
		}
		//更改昵称
		if(!StringUtils.isEmpty(accountDto.getNickName())) {
			localAuth.setUserName(accountDto.getNickName());
		}		
		//更改电话
		if(!StringUtils.isEmpty(accountDto.getTel())) {
			localAuth.setUserName(accountDto.getTel());
		}
		//更改租户id
		if(!StringUtils.isEmpty(accountDto.getCurTenantId())) {
			localAuth.setUserName(accountDto.getCurTenantId());
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
		LocalAuthDto localAuthDto = new LocalAuthDto();		
		BeanCopier copy=BeanCopier.create(LocalAuth.class, LocalAuthDto.class, false);
		copy.copy(localAuth, localAuthDto, null);
		return localAuthDto;
	}
	
	/**
	 * 找回用户密码
	 * @param accountDto
	 * @return
	 */
	public ResponseDto findLocalAuthPassWord(AccountDto accountDto) {
		ResponseDto dto = new ResponseDto();/*
		LocalAuth localAuth = localAuthDao.selectLocalAuthByUserName(accountDto.getUserName());
		//用户不存在
		if(null == localAuth) {
			dto.setErrorMsg(ErrorMsg.USER_NOT_EXIST);
			return dto;
		}else {
			dto.setData(localAuth);
		}
		//
		if(StringUtils.isEmpty(accountDto.getPassWord()) || StringUtils.isEmpty(accountDto.getVcode())) {
			
		}
		if (StringUtils.isEmpty(accountDto.getPassWord())) {
			// 用户存在情况下，返回用户信息
			if (StringUtils.isEmpty(userInfo.getVcode())) {
				dto.setData(localAuth);
			} else {
				// 验证码校验
				if (null == (String) session.getAttribute("vcode")
						|| !vcodeCheck(userInfo.getVcode(), (String) session.getAttribute("vcode"))) {
					dto.setErrorMsg(ErrorMsg.EMAIL_VCODE_ERROR);
				} else {
					dto.setErrorMsg(ErrorMsg.OK);
				}
			}

		} else {
			// 新密码校验与保存
			Pattern p = Pattern.compile("(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{8,16}$");
			if (!p.matcher(userInfo.getPassWord()).matches() || !p.matcher(userInfo.getPassWord1()).matches()) {
				dto.setErrorMsg(ErrorMsg.PASSWORD_ILLEGAL);
			} else if (!userInfo.getPassWord().equals(userInfo.getPassWord1())) {
				dto.setErrorMsg(ErrorMsg.PASSWORD_DIFF);
			} else {
				localAuth.setPassWord(md5PasswordEncoder.encodePassword(userInfo.getPassWord(), null));
				// 更新用户密码信息
				USER_DAO.saveUserAuth(localAuth);
				dto.setErrorMsg(ErrorMsg.OK);
			}
		}*/
	
		return dto;
	}
}
