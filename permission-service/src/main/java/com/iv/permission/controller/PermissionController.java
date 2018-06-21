package com.iv.permission.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.CodesDto;
import com.iv.permission.api.dto.FunctionDto;
import com.iv.permission.api.dto.FunctionInfoDto;
import com.iv.permission.api.dto.GlobalRoleCreate;
import com.iv.permission.api.dto.IdsDto;
import com.iv.permission.api.dto.PageQueryDto;
import com.iv.permission.api.dto.PermissionDto;
import com.iv.permission.api.dto.PersonRoleDto;
import com.iv.permission.api.service.IPermissionService;
import com.iv.permission.response.ErrorMsg;
import com.iv.permission.service.PermissionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "运营权限管理接口api")
//@Api(value="permission",tags={"运营权限管理接口api用户操作接口"})
public class PermissionController implements IPermissionService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);
	@Autowired
	private PermissionService permissionService;
		
	@Override
	@ApiOperation(value = "获取运营审批人员列表")
	public Set<LocalAuthDto> getApprovalPersons(String code) {
		// TODO Auto-generated method stub
		try {
			return permissionService.getApprovalPersons(code);			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取运营审批人员失败");			
		}
		return null;
	}
	
	@Override
	@ApiOperation(value = "api新增", notes = "829001")
	public ResponseDto createPermission(@RequestBody PermissionDto permissionDto,HttpServletRequest request) {
		// TODO Auto-generated method stub
		return permissionService.createPermission(permissionDto,request);
	}
	
	@Override
	@ApiOperation(value = "api删除", notes = "829002")
	public ResponseDto deletePermission(@RequestBody CodesDto codesDto) {
		// TODO Auto-generated method stub
		return permissionService.deletePermission(codesDto);
	}
	
	@Override
	@ApiOperation(value = "api编辑", notes = "829003")
	public ResponseDto editPermission(@RequestBody PermissionDto permissionDto) {
		// TODO Auto-generated method stub
		return permissionService.editPermission(permissionDto);
	}
	
	/**
	 * 
	 * 运维运营获取全部api列表
	 */
	@Override
	@ApiOperation(value = "运营api列表", notes = "829004")
	public ResponseDto getPermissionPageList(@RequestBody PageQueryDto pageQueryDto) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			dto.setData(permissionService.getPermissionList(pageQueryDto));
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取运营权限列表失败");
			dto.setErrorMsg(ErrorMsg.GET_OPERATION_AUTHORITY);
		}
		return dto;
	}
	
	
	@Override
	@ApiOperation(value = "权限新增", notes = "829005")
	public ResponseDto createFunction(@RequestBody FunctionDto createFunctionDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return permissionService.createFunction(createFunctionDto,request);
	}

	@Override
	@ApiOperation(value = "api列表（无分页）", notes = "829006")
	public ResponseDto getPermissionList() {
		// TODO Auto-generated method stub
		return permissionService.getPermissionList();
	}
	
	@Override
	@ApiOperation(value = "权限删除", notes = "829007")
	public ResponseDto deleteFunction(@RequestBody IdsDto idsDto) {
		// TODO Auto-generated method stub
		return permissionService.deleteFunction(idsDto);
	}


	@Override
	@ApiOperation(value = "权限编辑", notes = "829008")
	public ResponseDto editFunction(@RequestBody FunctionDto functionDto) {
		// TODO Auto-generated method stub
		return permissionService.editFunction(functionDto);
	}


	@Override
	@ApiOperation(value = "权限列表", notes = "829004")
	public ResponseDto getFunction() {
		// TODO Auto-generated method stub
		return permissionService.getFunction();
	}
	
	@Override
	@ApiOperation(value = "角色新增", notes = "829010")
	public ResponseDto createGlobalRole(@RequestBody GlobalRoleCreate globalRoleCreate,HttpServletRequest request) {
		// TODO Auto-generated method stub
		return permissionService.createGlobalRole(globalRoleCreate,request);
	}

	@Override
	@ApiOperation(value = "角色删除", notes = "829011")
	public ResponseDto deleteGlobalRole(@RequestBody IdsDto idsDto) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			permissionService.deleteGlobalRole(idsDto);
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("删除角色失败");
			dto.setErrorMsg(ErrorMsg.DELETE_SUBTENANT_ROLE_FAILED);
		}
		return dto;
	}
	
	@Override
	/**
	 * 角色编辑（基础库）
	 */
	@ApiOperation(value = "角色编辑", notes = "829012")
	public ResponseDto editGlobalRole(@RequestBody GlobalRoleCreate goleCreate) {
		// TODO Auto-generated method stub
		return permissionService.editGlobalRole(goleCreate);
	}	
	
	@Override
	/**
	 * 角色列表
	 */
	@ApiOperation(value = "角色列表查询", notes = "829013")
	public ResponseDto getGlobalRole() {
		// TODO Auto-generated method stub
		return permissionService.getGlobalRole();
	}

	@Override
	@ApiOperation(value = "角色赋予（基础库）", notes = "829003")
	public ResponseDto GivePersonRole(@RequestBody PersonRoleDto personRoleDto) {
		// TODO Auto-generated method stub
		return permissionService.GivePersonRole(personRoleDto);
	}
}
