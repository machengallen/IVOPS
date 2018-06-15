package com.iv.operation.script.quartz.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.iv.common.util.spring.SpringContextUtil;
import com.iv.operation.script.dto.TargetHostsDto;
import com.iv.operation.script.service.OperationScriptService;
import com.iv.operation.script.util.TenantIdHolder;

import net.sf.json.JSONArray;

public class SingleTaskQuartzJob implements Job {

	public SingleTaskQuartzJob() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getMergedJobDataMap();// 任务自定义数据
		TenantIdHolder.set(dataMap.getString("groupName"));// 保存租户id至线程变量
		JSONArray array = JSONArray.fromObject(dataMap.get("targetHosts"));
		TargetHostsDto targetHostsDto = JSONObject.parseObject(array.get(0).toString(), TargetHostsDto.class);
		SpringContextUtil.getBean(OperationScriptService.class).singleTaskExec(targetHostsDto);

	}

}
