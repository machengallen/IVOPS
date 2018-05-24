package com.iv.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.enter.dto.OpsGroupDto;
import com.iv.enter.dto.GroupIdsDto;
import com.iv.outer.dto.GroupEntityDto;

/**
 * 团队管理api
 * @author zhangying
 * 2018年4月9日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface IGroupService {
	
	/**
	 * 根据组id、租户id(告警轮询推送使用)
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/select/groupInfoById", method = RequestMethod.GET)
	GroupEntityDto selectGroupInfo(@RequestParam("subTenantId") String subTenantId
			, @RequestParam("groupId") short groupId);
		
	/**
	 * 查询当前用户所在组信息（人员非分页）
	 * @param groupUserQuery
	 * @return
	 */
	@RequestMapping(value = "/select/groupUsersInfo", method = RequestMethod.GET)
	ResponseDto groupUsersInfo(@RequestParam("request") HttpServletRequest request);
	
	/**
	 * 查询当前用户所在组信息（人员分页）
	 * @param groupUserQuery
	 * @return
	 */
	@RequestMapping(value = "/select/groupUsersPageInfo", method = RequestMethod.POST)
	ResponseDto groupUsersPageInfo(@RequestParam("request") HttpServletRequest request,@RequestBody GroupQuery groupQuery);
	
	/**
	 * 创建组
	 * @param groupQuery
	 * @return
	 */
	@RequestMapping(value = "/group/create", method = RequestMethod.POST)
	ResponseDto createGroup(@RequestBody OpsGroupDto opsGroupDto, @RequestParam("request") HttpServletRequest request);
	
	/**
	 * 删除组
	 * @param groupQuery
	 * @return
	 */
	@RequestMapping(value = "/group/delete", method = RequestMethod.GET)
	ResponseDto deleteGroup(@RequestParam("groupId") short groupId);
	
	
	/**
	 * 增加成员
	 * @param groupQuery
	 * @return
	 */
	@RequestMapping(value = "/group/memberIn", method = RequestMethod.POST)
	ResponseDto memberInGroup(@RequestBody OpsGroupDto opsGroupDto);
	
	/**
	 * 删除成员
	 * @param groupQuery
	 * @return
	 */
	@RequestMapping(value = "/group/memberOut", method = RequestMethod.POST)
	ResponseDto memberOutGroup(@RequestBody OpsGroupDto opsGroupDto);
	
	/**
	 * 修改组名称
	 * @param groupQuery
	 * @return
	 */
	@RequestMapping(value = "/group/nameMod", method = RequestMethod.GET)
	ResponseDto groupNameMod(@RequestParam("groupId") short groupId, @RequestParam("groupName") String groupName);
	
	/**
	 * 根据租户id，提供人员信息列表
	 * @param groupQuery
	 * @return
	 */
	@RequestMapping(value = "/tenant/userInfos", method = RequestMethod.GET)
	ResponseDto selectUsersInfoByTenantId(@RequestParam("request") HttpServletRequest request);
	
	/**
	 * 查询所有组信息（人员非分页）
	 * @param groupUserQuery
	 * @return
	 */
	@RequestMapping(value = "/select/groupsUsersInfo", method = RequestMethod.GET)
	ResponseDto groupsUsersInfo();
	
	/**
	 * 根据组Ids获取组信息（组名称、组id）
	 * @return
	 */
	@RequestMapping(value = "/select/groupsInfo", method = RequestMethod.POST)
	List<GroupEntityDto> groupsInfo(@RequestBody GroupIdsDto groupIds);
	
	/**
	 * 项目组下人员信息分页
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/select/tenantUserPageInfo", method = RequestMethod.POST)
	ResponseDto selectTenantUserPageInfo(@RequestParam("request") HttpServletRequest request, @RequestBody GroupQuery groupQuery);
}	
