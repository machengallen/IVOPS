package com.iv.report.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.enumeration.CycleType;
import com.iv.common.response.ResponseDto;
import com.iv.report.api.dto.AnalysisQuery;

/**
 * 告警报表服务
 * @author zhangying
 * 2018年4月23日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface IAlarmReportService {
	
	/**
	 * 获取某一时间段内，告警的数据分析（以告警类型为主）
	 * @param analysisQuery
	 * @return
	 */
	@RequestMapping(value = "/alarm/report/all", method = RequestMethod.POST)
	ResponseDto getAlarmAnalysisData(@RequestBody AnalysisQuery analysisQuery);

	/**
	 * 获取某一时间段内，压缩告警分析
	 * @param analysisQuery
	 * @return
	 */
	@RequestMapping(value = "/alarm/report/press", method = RequestMethod.POST)
	ResponseDto getPressAlarmAnalysisData(@RequestBody AnalysisQuery analysisQuery);

	/**
	 * 获取某一时间段内，人为处理告警分析
	 * @param analysisQuery
	 * @return
	 */
	@RequestMapping(value = "/alarm/report/person", method = RequestMethod.POST)
	ResponseDto getPersonAlarmAnalysisData(@RequestBody AnalysisQuery analysisQuery);
	
	/**
	 * 查询报表总览
	 * @param date
	 * @return
	 */
	@RequestMapping(value = "/report/overview", method = RequestMethod.GET)
	ResponseDto getOverview(@RequestParam("date") String date);
	
	/**
	 * 查询报表总览1
	 * @param date
	 * @return
	 */
	@RequestMapping(value = "/report/overview1", method = RequestMethod.GET)
	ResponseDto getOverview1(@RequestParam("date") String date);
	
	/**
	 * 查询报表告警数量趋势:告警量、平均响应时间、平均恢复时间
	 * @param cycle
	 * @return
	 */
	@RequestMapping(value = "/report/alarmTrend", method = RequestMethod.GET)
	ResponseDto getAlarmTrend(@RequestParam CycleType cycle);
}
