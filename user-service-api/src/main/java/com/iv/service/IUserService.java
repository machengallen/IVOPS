package com.iv.service;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.dto.ResponseDto;
import com.iv.entity.dto.LocalAuthDto;
import com.iv.entity.dto.UserOauthDto;


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
	@RequestMapping(value = "/user/info", method = RequestMethod.POST)
	ResponseDto getUserInfo();
	
	/**
	 * 查看用户是否已绑定三方登录
	 * @param unionid
	 * @return
	 */
	@RequestMapping(value = "/permit/user/bindInfo", method = RequestMethod.POST)
	UserOauthDto bindInfo(@RequestParam String unionid, @RequestParam String loginType);
	
	/**
	 * 根据用户名查询用户信息
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/permit/select/localauthInfoByName",method = RequestMethod.POST)
	LocalAuthDto selectLocalauthInfoByName(@RequestParam String userName);
	
	/**
	 * 保存用户信息
	 * @param localAuthDto
	 * @return
	 */
	@RequestMapping(value = "/permit/saveOrUpdate/localauth",method = RequestMethod.POST)
	LocalAuthDto saveOrUpdateLocalAuth(@RequestBody LocalAuthDto localAuthDto);
	
	/**
	 * 根据id查询用户信息
	 * @param userId
	 * @return
	 * @throws RuntimeException
	 */
	@RequestMapping(value = "/permit/select/localAuthById",method = RequestMethod.POST)
	LocalAuthDto selectLocalAuthById(@RequestParam int userId) throws RuntimeException;

	/**
	 * 用户信息修改
	 * @param modifyInfo
	 * @return
	 *//*
	@RequestMapping(value = "/user/modify", method = RequestMethod.POST)
	ResponseDto userOps(@RequestBody(required = true) UserSafeDto modifyInfo);

	*//**
	 * 查询当前用户所在组信息
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "/group/info", method = RequestMethod.GET)
	ResponseDto groupInfo();

	*//**
	 * 查询当前用户所在组名称与组id
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "/group/detailInfo", method = RequestMethod.GET)
	ResponseDto groupDetailInfo();

	*//**
	 * 租户管理系统：查询当前组内分页人员信息
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "/group/groupUserPageInfo", method = RequestMethod.POST)
	ResponseDto groupUserPageInfo(@RequestBody GroupUserQuery groupUserQuery);

	*//**
	 * 查询所有用户组列表
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "groups/info", method = RequestMethod.POST)
	ResponseDto groupsInfo();

	*//**
	 * 成员组操作
	 * @param groupDto
	 * @return
	 *//*
	@RequestMapping(value = "/group/ops", method = RequestMethod.POST)
	ResponseDto groupOps(@RequestBody GroupDto groupDto);

	*//**
	 * 获取微信带参二维码
	 * 
	 * @param session
	 * @return
	 *//*
	@RequestMapping(value = "/qrcode/create", method = RequestMethod.GET)
	ResponseDto qrcodeCreate();

	*//**
	 * 查询用户信息
	 * 
	 * @param session
	 * @return
	 *//*
	@RequestMapping(value = "/user/info", method = RequestMethod.POST)
	ResponseDto getUserInfo();

	*//**
	 * 获取所有用户列表
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "/user/info/list", method = RequestMethod.GET)
	ResponseDto getUsersInfo();

	*//**
	 * 获取所有用户分页列表
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "/user/info/pageList", method = RequestMethod.POST)
	ResponseDto getUsersPageInfo(@RequestBody UserQuery userQuery);

	*//**
	 * 获取所有用户分页列表
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "/user/info/fromEnter/pageList", method = RequestMethod.POST)
	ResponseDto getUsersPageFromParent(@RequestBody UserQuery userQuery);

	*//**
	 * 获取公众号微信用户列表
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "/user/wechat", method = RequestMethod.GET)
	public ResponseDto getUsersWechat();
	
	*//**
	 * 密码找回
	 * 
	 * @param session
	 * @param registerDto
	 * @return
	 *//*
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	ResponseDto resetPassword(@RequestBody UserSafeDto userInfo);

	*//**
	 * 给用户提升权限
	 * 
	 * @param groupId
	 * @param userId
	 * @return
	 *//*
	@RequestMapping(value = "/promoteAuthority", method = RequestMethod.POST)
	ResponseDto promoteAuthority(@RequestBody HireUserRoleDto hireUserRoleDto,
			SecurityContextHolderAwareRequestWrapper request);
	
	*//**
	 * 保存多用户微信信息体
	 * 
	 * @param session
	 * @param registerDto
	 * @return
	 *//*
	@RequestMapping(value = "/save/usersWechatInfo", method = RequestMethod.POST)
	void saveUsersInfo(@RequestBody UserWechatDto userWechatEntities);
	
	*//**
	 * 保存用户微信信息体
	 * 
	 * @param session
	 * @param registerDto
	 * @return
	 *//*
	@RequestMapping(value = "/save/userWechatInfo", method = RequestMethod.POST)
	void saveUserInfo(@RequestBody UserWechatEntityDto userWechatEntity);
	
	*//**
	 * 根据用户id查询用户信息
	 * @param eventKey
	 * @return
	 *//*
	@RequestMapping(value = "/select/userAuthById",method = RequestMethod.POST)
	LocalAuthDto selectUserAuthById(@RequestParam int eventKey);
	
	*//**
	 * 根据openId查询用户微信信息
	 * @param openId
	 * @return
	 *//*
	@RequestMapping(value = "/select/userWeChatById",method = RequestMethod.POST)
	UserWechatEntityDto selectUserWechatById(@RequestParam String openId);
	
	*//**
	 * 根据openId删除用户信息
	 * @param openId
	 *//*
	@RequestMapping(value = "/delete/userWeChatById",method = RequestMethod.POST)
	void deleteUserInfoById(@RequestParam String openId);
	
	*//**
	 * 解绑微信
	 * @param wechatEntity
	 *//*
	@RequestMapping(value = "/unbound/wechat",method = RequestMethod.POST)
	void unboundWechat(@RequestBody UserWechatEntityDto wechatEntity);
	
	*//**
	 * 更新用户openId
	 * @param weChatInfo
	 * @return
	 *//*
	@RequestMapping(value = "/update/openId",method = RequestMethod.POST)
	Boolean updateOpenId(@RequestBody UserWechatInfo weChatInfo);
	
	*//**
	 * 根据openId查询用户是否存在，判断微信绑定情况
	 * @param openId
	 * @return
	 *//*
	@RequestMapping(value = "/select/userInfoByopenId",method = RequestMethod.POST)
	LocalAuthDto selectUserByOpenId(@RequestParam String openId);
	
	
	
	*//**
	 * 保存用户信息
	 * @param localAuth
	 * @return
	 *//*
	@RequestMapping(value = "/save/localAuthInfo", method = RequestMethod.POST)
	ResponseDto saveUserAuth(LocalAuthDto localAuth);*/
}
