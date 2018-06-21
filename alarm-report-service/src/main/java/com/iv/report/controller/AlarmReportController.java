package com.iv.report.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.enumeration.CycleType;
import com.iv.common.response.ResponseDto;
import com.iv.report.api.dto.AnalysisQuery;
import com.iv.report.api.service.IAlarmReportService;
import com.iv.report.dto.AlarmCountsAndMTTARDto;
import com.iv.report.dto.ErrorMsg;
import com.iv.report.dto.Overview1Dto;
import com.iv.report.dto.OverviewDto;
import com.iv.report.enumeration.AnalysisType;
import com.iv.report.service.AlarmReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "告警报表与dashboard管理接口")
public class AlarmReportController implements IAlarmReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmReportController.class);
	@Autowired
	private AlarmReportService alarmReportService;
	
	@Override	
	@ApiOperation(value = "告警整体分析数据", notes = "84500")
	public ResponseDto getAlarmAnalysisData(@RequestBody AnalysisQuery analysisQuery) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		try {
			responseDto.setData(alarmReportService.getAlarmAnalysisData(analysisQuery,AnalysisType.COMMONALARM.ordinal()));
			responseDto.setErrorMsg(ErrorMsg.OK);			
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：获取告警数据报表失败", e);
			responseDto.setErrorMsg(ErrorMsg.GET_REPORT_FAILED);
			return responseDto;
		}
	}

	@Override
	@ApiOperation(value = "压缩告警分析数据", notes = "84501")
	public ResponseDto getPressAlarmAnalysisData(@RequestBody AnalysisQuery analysisQuery) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		try {
			responseDto.setData(alarmReportService.pressAlarmAnalysisData(analysisQuery));
			responseDto.setErrorMsg(ErrorMsg.OK);		
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：获取压缩告警数据报表失败", e);
			responseDto.setErrorMsg(ErrorMsg.GET_REPORT_FAILED);
			return responseDto;
		}
	}

	@Override
	@ApiOperation(value = "人为处理告警分析数据", notes = "84502")
	public ResponseDto getPersonAlarmAnalysisData(@RequestBody AnalysisQuery analysisQuery) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		try {	
			responseDto.setData(alarmReportService.getAlarmAnalysisData(analysisQuery,AnalysisType.RESTOREDALARM.ordinal()));
			responseDto.setErrorMsg(ErrorMsg.OK);			
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：获取压缩告警数据报表失败", e);
			responseDto.setErrorMsg(ErrorMsg.GET_REPORT_FAILED);
			return responseDto;
		}
	}

	@Override
	@ApiOperation(value = "Dashboard:查询报表总览", notes = "84503")
	public ResponseDto getOverview(String date) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		try {
			OverviewDto dto = alarmReportService.getOverview(Long.parseLong(date));
			responseDto.setData(dto);
			responseDto.setErrorMsg(ErrorMsg.OK);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：获取报表失败", e);
			responseDto.setErrorMsg(ErrorMsg.GET_REPORT_FAILED);
			return responseDto;
		}
	}

	@Override
	@ApiOperation(value = "Dashboard:查询报表总览1", notes = "84504")
	public ResponseDto getOverview1(String date) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		try {
			Overview1Dto dto = new Overview1Dto();
			Long date1 = Long.parseLong(date);
			dto.setHostsTop(alarmReportService.getTopAlarm(date1));
			dto.setSeverityRatio(alarmReportService.getSevProportion(date1));
			dto.setItemTypeRatio(alarmReportService.getItemProportion(date1));
			dto.setUnrecovered(alarmReportService.getOpenAlarmNum(date1));
			responseDto.setErrorMsg(ErrorMsg.OK);
			responseDto.setData(dto);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：获取报表失败", e);
			responseDto.setErrorMsg(ErrorMsg.GET_REPORT_FAILED);
			return responseDto;
		}
	}

	@Override
	@ApiOperation(value = "Dashboard:查询报表告警数量趋势", notes = "84505")
	public ResponseDto getAlarmTrend(CycleType cycle) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		try {
			AlarmCountsAndMTTARDto dto = new AlarmCountsAndMTTARDto();
			dto.setiTrendCounts(alarmReportService.getAlarmTrend(cycle));
			dto.setiTrendMttAR(alarmReportService.getMTTARTrend(cycle));
			responseDto.setData(dto);
			responseDto.setErrorMsg(ErrorMsg.OK);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：获取报表失败", e);
			responseDto.setErrorMsg(ErrorMsg.GET_REPORT_FAILED);
			return responseDto;
		}
	}

}
