package com.iv.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.dto.ErrorMsg;
import com.iv.enter.dto.GroupQuery;
import com.iv.enter.dto.OpsGroupDto;
import com.iv.enter.dto.GroupIdsDto;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.service.GroupService;
import com.iv.service.IGroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(description = "团队管理接口api")
public class GroupController implements IGroupService{

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
	@Autowired
	private GroupService groupService;
	
	@Override
	@ApiOperation("根据组id、租户id查询组信息")	
	@ApiIgnore
	public GroupEntityDto selectGroupInfo(String subTenantId, short groupId) {
		// TODO Auto-generated method stub
		try {
			return groupService.selectGroupInfo(subTenantId,groupId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取用户组信息失败");
			return null;
		}		
	}

	@Override
	@ApiOperation(value = "用户所在组信息（人员非分页）", notes = "82800")
	public ResponseDto groupUsersInfo(HttpServletRequest request) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));	
		try {
			dto.setData(groupService.groupUsersInfo(userId));
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取用户组信息失败");
			dto.setErrorMsg(ErrorMsg.GROUP_INFO_FAILED);
		}
		return dto;
	}
	
	@Override
	@ApiOperation(value = "用户所在组信息（人员分页）", notes = "82801")
	public ResponseDto groupUsersPageInfo(HttpServletRequest request,@RequestBody GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));		
		try {
			dto.setData(groupService.groupUsersPageInfo(groupQuery,userId));
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取用户组信息失败");
			dto.setErrorMsg(ErrorMsg.GROUP_INFO_FAILED);
		}
		return dto;
	}	
		
	@Override
	@ApiOperation(value = "租户下人员列表", notes = "82802")
	public ResponseDto selectUsersInfoByTenantId(HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			return ResponseDto.builder(ErrorMsg.OK, groupService.selectUsersInfoByTenantId(request));
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("系统错误:获取项目组人员信息失败", e);			
			return ResponseDto.builder(ErrorMsg.GET_TENANTUSERS_FAILED);
		}
		
	}

	@Override
	@ApiOperation(value = "租户下所有组信息（人员非分页）", notes = "82803")
	public ResponseDto groupsUsersInfo() {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			dto.setData(groupService.groupsUsersInfo());
			dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exceptions
			LOGGER.info("获取用户组信息失败");
			dto.setErrorMsg(ErrorMsg.GROUP_INFO_FAILED);
		}
		return dto;
	}

	@Override
	@ApiOperation("根据组Ids获取组信息（组名称、组id）")
	@ApiIgnore
	public List<GroupEntityDto> groupsInfo(@RequestBody GroupIdsDto groupIds) {
		// TODO Auto-generated method stub
		try {
			return groupService.groupsInfo(groupIds.getGroupIds());
		} catch (Exception e) {
			// TODO: handle exceptions
			LOGGER.info("获取团队组信息失败");
			return null;
		}
	}

	@Override
	@ApiOperation(value = "创建团队", notes = "82804")
	public ResponseDto createGroup(@RequestBody OpsGroupDto opsGroupDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			if(StringUtils.isEmpty(opsGroupDto.getGroupName())) {
				return ResponseDto.builder(ErrorMsg.GROUP_NAME_ISEMPTY);
			}
			Object groupEntity = groupService.createGroup(opsGroupDto,request);						
			return ResponseDto.builder(ErrorMsg.OK, groupEntity);
		} catch (Exception e) {			
			LOGGER.error("系统错误:创建用户组失败", e);
			return ResponseDto.builder(ErrorMsg.GROUP_CREATE_FAILED);
		}
	}

	@Override
	@ApiOperation(value = "删除团队", notes = "82805")
	public ResponseDto deleteGroup(short groupId) {
		// TODO Auto-generated method stub
		try {
			groupService.deleteGroup(groupId);						
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {			
			LOGGER.error("系统错误:删除用户组失败", e);
			return ResponseDto.builder(ErrorMsg.GROUP_DELETE_FAILED);
		}
	}

	@Override
	@ApiOperation(value = "增加组成员", notes = "82806")
	public ResponseDto memberInGroup(@RequestBody OpsGroupDto opsGroupDto) {
		// TODO Auto-generated method stub
		try {
			Object groupEntity = groupService.memberInGroup(opsGroupDto);						
			return ResponseDto.builder(ErrorMsg.OK, groupEntity);
		} catch (Exception e) {			
			LOGGER.error("系统错误:用户组增加成员失败", e);
			return ResponseDto.builder(ErrorMsg.GROUP_MEMBERIN_FAILED);
		}
	}

	@Override
	@ApiOperation(value = "删除组成员", notes = "82807")
	public ResponseDto memberOutGroup(@RequestBody OpsGroupDto opsGroupDto) {
		// TODO Auto-generated method stub
		try {
			Object groupEntity = groupService.memberOutGroup(opsGroupDto);						
			return ResponseDto.builder(ErrorMsg.OK, groupEntity);
		} catch (Exception e) {			
			LOGGER.error("系统错误:用户组删除成员失败", e);
			return ResponseDto.builder(ErrorMsg.GROUP_MEMBEROUT_FAILED);
		}
	}

	@Override
	@ApiOperation(value = "编辑组名称", notes = "82808")
	public ResponseDto groupNameMod(short groupId, String groupName) {
		// TODO Auto-generated method stub
		try {
			Object groupEntity = groupService.groupNameMod(groupId, groupName);						
			return ResponseDto.builder(ErrorMsg.OK, groupEntity);
		} catch (Exception e) {			
			LOGGER.error("系统错误:修改组名称失败", e);
			return ResponseDto.builder(ErrorMsg.GROUP_NAMEMOD_FAILED);
		}
	}

	@Override
	@ApiOperation(value = "项目组人员分页查询", notes = "82809")
	public ResponseDto selectTenantUserPageInfo(HttpServletRequest request, @RequestBody GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			dto.setData(groupService.selectTenantUserPageInfo(request, groupQuery));
			dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exceptions
			LOGGER.info("获取项目组下人员分页信息失败");
			dto.setErrorMsg(ErrorMsg.GET_TENANTUSERS_FAILED);
		}
		return dto;
	}

	@Override
	@ApiOperation(value = "查询组内人员id集合")
	public List<Integer> selectGroupUserIds(String subTenantId, short groupId) {
		// TODO Auto-generated method stub
		try {								
			return groupService.selectGroupUserIds(subTenantId,groupId);
		} catch (Exception e) {			
			LOGGER.error("系统错误:查询组内人员id集合失败", e);
			return null;
		}
	}

	
}
