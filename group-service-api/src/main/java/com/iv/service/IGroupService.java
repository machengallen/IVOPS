package com.iv.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.outer.dto.GroupEntityDto;

/**
 * 团队管理api
 * @author zhangying
 * 2018年4月9日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface IGroupService {
	
	/**
	 * 根据组id、租户id/或根据组id查询组信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/select/groupInfoById", method = RequestMethod.POST)
	GroupEntityDto selectGroupInfo(@RequestBody GroupQuery groupQuery);
		
	/**
	 * 查询当前用户所在组信息（人员分页/非分页）
	 * @param groupUserQuery
	 * @return
	 */
	@RequestMapping(value = "/select/groupUserPageInfo", method = RequestMethod.GET)
	ResponseDto groupUserPageInfo(@RequestBody GroupQuery groupQuery);
	
	/**
	 * 组操作
	 * @param groupDto
	 * @return
	 */
	@RequestMapping(value = "/group/ops", method = RequestMethod.POST)
	ResponseDto groupOps(@RequestBody GroupQuery groupQuery);
}
