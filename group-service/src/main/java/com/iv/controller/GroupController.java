package com.iv.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.dto.ErrorMsg;
import com.iv.enter.dto.GroupQuery;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.service.GroupService;
import com.iv.service.IGroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "团队管理接口api")
public class GroupController implements IGroupService{

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
	@Autowired
	private GroupService groupService;
	
	@Override
	@ApiOperation("根据组id、租户id/或根据组id查询组信息")
	public GroupEntityDto selectGroupInfo(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		try {
			return groupService.selectGroupInfo(groupQuery);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取用户组信息失败");
			return null;
		}		
	}

	@Override
	@ApiOperation("查询当前用户所在组信息（人员非分页）")
	public ResponseDto groupUsersInfo(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		int userId = Integer.parseInt(JWTUtil.getJWtJson("").getString("userId"));		
		try {
			dto.setData(groupService.groupUsersInfo(groupQuery,userId));
			dto.setData(com.iv.common.response.ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取用户组信息失败");
			dto.setErrorMsg(ErrorMsg.GROUP_INFO_FAILED);
		}
		return dto;
	}
	
	@Override
	@ApiOperation("查询当前用户所在组信息（人员分页）")
	public ResponseDto groupUsersPageInfo(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		int userId = Integer.parseInt(JWTUtil.getJWtJson("").getString("userId"));		
		try {
			dto.setData(groupService.groupUsersPageInfo(groupQuery,userId));
			dto.setData(com.iv.common.response.ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取用户组信息失败");
			dto.setErrorMsg(ErrorMsg.GROUP_INFO_FAILED);
		}
		return dto;
	}	
		
	@Override
	@ApiOperation("组操作")
	public ResponseDto groupOps(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		ResponseDto dto = new ResponseDto();
		try {
			Object groupEntity = groupService.groupOps(groupQuery);
			if (groupEntity instanceof String) {
				dto.setErrorMsg(ErrorMsg.GROUP_IN_STRATEGY);
			} else {
				dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
				dto.setData(groupEntity);
			}
			return dto;
		} catch (Exception e) {
			
			LOGGER.error("系统错误:用户组操作失败", e);
			dto.setErrorMsg(ErrorMsg.GROUP_OPS_FAILED);
			return dto;
		}
	}

	@Override
	@ApiOperation("根据租户id，提供人员信息列表")
	public ResponseDto selectUsersInfoByTenantId(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
