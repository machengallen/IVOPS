package com.iv.permission.api.service;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.PersonRoleDto;
import com.iv.permission.api.dto.PageQueryDto;
import com.iv.permission.api.dto.PermissionDto;
import com.iv.permission.api.dto.FunctionDto;
import com.iv.permission.api.dto.GlobalRoleCreate;

/**
 * 运营权限管理api
 * @author zhangying
 * 2018年5月7日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public interface IPermissionService {				
	/**
	 * 从基础库获取有审批权限code的人员列表
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/get/approvalPersons", method = RequestMethod.GET)
	Set<LocalAuthDto> getApprovalPersons(@RequestParam("code") String code);
	
	/**
	 * 权限增加（基础库）
	 * @return
	 */
	@RequestMapping(value = "/ceate/permission", method = RequestMethod.POST)
	ResponseDto createPermission(@RequestBody PermissionDto permissionDto);
	
	/**
	 * 权限删除（基础库）
	 * @return
	 */
	@RequestMapping(value = "/delete/permission", method = RequestMethod.POST)
	ResponseDto deletePermission(@RequestParam("code") String code);
	
	/**
	 * 权限编辑（基础库）
	 * @return
	 */
	@RequestMapping(value = "/edit/permission", method = RequestMethod.POST)
	ResponseDto editPermission(@RequestParam("code") String code);
	
	/**
	 * 获取运营权限列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/get/permissionList", method = RequestMethod.POST)
	ResponseDto getPermissionPageList(@RequestBody PageQueryDto pageQueryDto); 
	
	/**
	 * 创建功能
	 * @param createRoleDto
	 * @return
	 */
	@RequestMapping(value = "/create/function", method = RequestMethod.POST)
	ResponseDto createFunction(@RequestBody FunctionDto functionDto);
	
	/**
	 * 获取权限列表，无分页
	 * @return
	 */
	@RequestMapping(value = "/get/permissions", method = RequestMethod.GET)
	ResponseDto getPermissionList(); 
	
	/**
	 * 删除功能
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete/function", method = RequestMethod.GET)
	ResponseDto deleteFunction(@RequestParam("id") String id);
	
	/**
	 * 编辑功能
	 * @param functionDto
	 * @return
	 */
	@RequestMapping(value = "/edit/function", method = RequestMethod.POST)
	ResponseDto editFunction(@RequestBody FunctionDto functionDto);
	
	/**
	 * 查看功能列表
	 * @return
	 */
	@RequestMapping(value = "/get/function", method = RequestMethod.GET)
	ResponseDto getFunction();
	
	/**
	 * 角色创建（基础库）
	 * @param globalRoleCreate
	 * @return
	 */
	@RequestMapping(value = "/creae/globalRole", method = RequestMethod.POST)
	ResponseDto createGlobalRole(@RequestBody GlobalRoleCreate globalRoleCreate);	
	
	/**
	 * 角色删除（基础库）
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/delete/globalRole", method = RequestMethod.GET)
	ResponseDto deleteGlobalRole(@RequestParam("roleId") int roleId);	
	
	/**
	 * 角色编辑（基础库）
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/edit/globalRole", method = RequestMethod.POST)
	ResponseDto editGlobalRole(@RequestBody GlobalRoleCreate goleCreate);
	
	/**
	 * 角色列表查询
	 * @param goleCreate
	 * @return
	 */
	@RequestMapping(value = "/get/globalRole", method = RequestMethod.GET)
	ResponseDto getGlobalRole();
	
	/**
	 * 赋予人员角色
	 * @param givePersonRole
	 * @return
	 */
	@RequestMapping(value = "/give/personRole", method = RequestMethod.POST)
	ResponseDto GivePersonRole(@RequestBody PersonRoleDto personRoleDto);			
	
	
}
