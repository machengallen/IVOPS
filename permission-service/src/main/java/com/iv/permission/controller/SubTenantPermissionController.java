package com.iv.permission.controller;

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

import com.iv.common.enumeration.ServiceType;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.IdsDto;
import com.iv.permission.api.dto.PageQueryDto;
import com.iv.permission.api.dto.PermissionDto;
import com.iv.permission.api.dto.SubTenantRoleDto;
import com.iv.permission.api.dto.SubTenantUserRoleDto;
import com.iv.permission.api.service.ISubTenantPermissionService;
import com.iv.permission.response.ErrorMsg;
import com.iv.permission.service.SubTenantPermissionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(description = "项目组权限管理接口api")
public class SubTenantPermissionController implements ISubTenantPermissionService{
	private static final Logger LOGGER = LoggerFactory.getLogger(SubTenantPermissionController.class);
	@Autowired
	private SubTenantPermissionService permissionService;
	
	@Override
	@ApiOperation(value = "预设项目组创建者")
	public ResponseDto createSubEnterpriseAdmin(String subTenantId, int userId) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			permissionService.createSubEnterpriseAdmin(subTenantId,userId);
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("预设项目组创建者失败");
			dto.setErrorMsg(ErrorMsg.CREATE_SUBTENANT_ADMIN_FAILED);
		}
		return dto;
	}
	
	/**
	 * 获取项目组下具有审批权限的人员列表
	 */
	@Override
	@ApiOperation(value = "获取项目组下具有审批权限的人员列表")
	public Set<LocalAuthDto> getSubEnterpriseApprovalPersons(String subTenantId, String code) {
		// TODO Auto-generated method stub
		try {
			return permissionService.getSubEnterpriseApprovalPersons(subTenantId,code);			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取项目组下具有审批权限的人员列表失败");			
		}
		return null;
	}
	
	/**
	 * 项目组权限列表：包括已订阅、未订阅标识符
	 */
	/*@Override	
	@ApiOperation(value = "查看权限列表", notes = "829100")
	public ResponseDto getAllPermissionStatus() {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			dto.setData(permissionService.getAllPermissionStatus());
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取权限列表失败");
			dto.setErrorMsg(ErrorMsg.GET_OPERATION_AUTHORITY);
		}
		return dto;
	}*/
	
	@Override
	@ApiOperation(value = "创建项目组角色", notes = "829101")
	public ResponseDto createSubTenantRole(@RequestBody SubTenantRoleDto createRoleDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			return permissionService.createSubTenantRole(createRoleDto,request);			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("角色创建失败");
			dto.setErrorMsg(ErrorMsg.CREATE_SUBTENANT_ROLE_FAILED);
		}
		return dto;
	}
	 
	@Override
	@ApiOperation(value = "项目组获取的权限列表", notes = "829102")	
	public ResponseDto getSubTenantPermissions() {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			dto.setData(permissionService.getSubTenantPermissions());
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取项目组已获取权限列表失败");
			dto.setErrorMsg(ErrorMsg.GET_OPERATION_AUTHORITY);
		}
		return dto;
	}
	
	@Override
	@ApiOperation(value = "项目组角色删除", notes = "829103")
	public ResponseDto deleteSubTenantRole(@RequestBody IdsDto ids) {
		// TODO Auto-generated method stub
		return permissionService.deleteSubTenantRole(ids);
	}
	@Override
	@ApiOperation(value = "项目组角色编辑", notes = "829104")
	public ResponseDto editSubTenantRole(@RequestBody SubTenantRoleDto createRoleDto) {
		// TODO Auto-generated method stub
		return permissionService.editSubTenantRole(createRoleDto);
	}
	@Override
	@ApiOperation(value = "项目组角色列表", notes = "829105")
	public ResponseDto getSubTenantRoles() {
		// TODO Auto-generated method stub
		return permissionService.getSubTenantRoles();
	}
	@Override
	@ApiOperation(value = "项目组人员权限赋予", notes = "829106")
	public ResponseDto editSubTenantPersonRole(@RequestBody SubTenantUserRoleDto subTenantUserRoleDto) {
		// TODO Auto-generated method stub
		return permissionService.editSubTenantPersonRole(subTenantUserRoleDto);
	}
	
	@ApiOperation(value = "子项目所拥有的角色", notes = "829107")
	@RequestMapping(value = "/subproject/Roles", method = RequestMethod.GET)
	public ResponseDto getSubprojectRoles(ServiceType serviceType, HttpServletRequest req) {
		try {
			return ResponseDto.builder(ErrorMsg.OK, permissionService.getSubprojectRoles(serviceType, req));
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("子项目所拥有的权限获取失败");
			return ResponseDto.builder(ErrorMsg.GET_OPERATION_AUTHORITY);
		}
	}

	@Override
	@ApiOperation(value = "人员角色信息")
	public Set<com.iv.outer.dto.SubTenantRoleDto> selectPersonRole(int userId, String tenantId) {
		// TODO Auto-generated method stub
		try {
			return permissionService.selectPersonRole(userId, tenantId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("人员角色信息获取失败");
			return null;
		}
	}

	@Override
	@ApiOperation(value = "人员api集合")
	@ApiIgnore
	public List<PermissionDto> getPersonPermissions(int userId, String tenantId) {
		// TODO Auto-generated method stub
		try {
			return permissionService.getPersonPermissions(userId, tenantId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("人员角色信息获取失败");
			return null;
		}
	}
		
	@ApiOperation(value = "权限集合-工单")
	@RequestMapping(value = "/formSys/permissions", method = RequestMethod.GET)
	public ResponseDto getFormSysPermissions(ServiceType serviceType, HttpServletRequest req) {
		// TODO Auto-generated method stub
		try {
			return ResponseDto.builder(ErrorMsg.OK, permissionService.getFormSysPermissions(serviceType,req));
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("子项目所拥有的权限获取失败");
			return ResponseDto.builder(ErrorMsg.GET_OPERATION_AUTHORITY);
		}
	}
	
	@ApiOperation(value = "人员api集合-前端权限")
	@RequestMapping(value = "/get/personApis", method = RequestMethod.GET)
	public ResponseDto getPersonApis(HttpServletRequest req) {
		// TODO Auto-generated method stub
		String curTenantId = JWTUtil.getJWtJson(req.getHeader("Authorization")).getString("curTenantId");
		int userId = Integer.parseInt(JWTUtil.getJWtJson(req.getHeader("Authorization")).getString("userId"));		
		try {
			return ResponseDto.builder(ErrorMsg.OK, permissionService.getPersonPermissions(userId, curTenantId));
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("人员api信息获取失败");
			return ResponseDto.builder(ErrorMsg.GET_OPERATION_AUTHORITY);
		}
	}

	@Override
	public ResponseDto getAllPermissionStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@ApiOperation(value = "查询组内拥有审批工单权限的人员列表-工单服务调用")
	public Set<LocalAuthDto> approveFormPerson(String code, String tenantId, short groupId) {
		// TODO Auto-generated method stub
		try {
			return permissionService.approveFormPerson(code, tenantId, groupId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("查询组内拥有审批工单权限的人员列表失败");
			return null;
		}
	}

}
