package com.iv.service;


import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.AccountDto;
import com.iv.enter.dto.UsersWechatsQuery;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.outer.dto.UserInfosDto;
import com.iv.outer.dto.UserOauthDto;


/**
 * 用户与团队管理api
 * @author zhangying
 * 2018年4月4日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface IUserService {
	
	/**
	 * 例子：查询用户信息
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/user/info", method = RequestMethod.GET)
	ResponseDto getUserInfo();
	
	/**
	 * 查看用户是否已绑定三方登录
	 * @param unionid
	 * @return
	 */
	@RequestMapping(value = "/permit/user/bindInfo", method = RequestMethod.POST)
	UserOauthDto bindInfo(@RequestParam String unionid, @RequestParam String loginType);
	
	/**
	 * 用户绑定微信信息
	 * @param accountDto
	 * @return
	 */
	@RequestMapping(value = "/bind/wechatInfo", method = RequestMethod.POST)
	ResponseDto bindWechatInfo(@RequestBody AccountDto accountDto);
	
	/**
	 * 注册用户信息
	 * @param accountDto
	 * @return
	 */
	@RequestMapping(value = "/register/account", method = RequestMethod.POST)
	ResponseDto registerAccount(@RequestBody AccountDto accountDto);
	/**
	 * 根据id查询用户信息
	 * @param userId
	 * @return
	 * @throws RuntimeException
	 */
	@RequestMapping(value = "/permit/select/localAuthById",method = RequestMethod.POST)
	LocalAuthDto selectLocalAuthById(@RequestParam int userId) throws RuntimeException;
	
	/**
	 * 根据用户id  登录方式  查询unionid
	 * @param userId
	 * @param loginType
	 * @return
	 * @throws RuntimeException
	 */
	@RequestMapping(value = "/select/UserWechat",method = RequestMethod.POST)
	String selectUserWechatUnionid(@RequestParam int userId, @RequestParam String loginType) throws RuntimeException;
	
	/**
	 * 根据用户id集合，查询用户信息
	 * @param usersWechatsQuery
	 * @return
	 */
	@RequestMapping(value = "/select/userInfos",method = RequestMethod.POST)
	List<UserInfosDto> selectUserInfos(@RequestBody UsersWechatsQuery usersWechatsQuery);
	
	
}
