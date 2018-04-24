package com.iv.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iv.common.enumeration.CycleType;
import com.iv.common.enumeration.Severity;
import com.iv.report.api.dto.AnalysisQuery;
import com.iv.report.dao.impl.AlarmReportDaoImpl;
import com.iv.report.dto.AlarmAnalysisDataDto;
import com.iv.report.dto.AlarmAndHostNumDto;
import com.iv.report.dto.AlarmDateByRecType;
import com.iv.report.dto.GroupByHostDto;
import com.iv.report.dto.GroupByItemTypeDto;
import com.iv.report.dto.GroupBySeverityDto;
import com.iv.report.dto.ITrendData;
import com.iv.report.dto.MTTARTrendDto;
import com.iv.report.dto.OverviewDto;
import com.iv.report.util.AlarmAnalysisUtil;
import com.iv.report.util.CycleUtil;

@Service
public class AlarmReportService {
	
	@Autowired
	private AlarmAnalysisUtil alarmAnalysisUtil;
	@Autowired
	private AlarmReportDaoImpl alarmReportDao;
	/**
	 * 总体告警分析：不区分压缩以及人为处理
	 * @param analysisQuery
	 * @return
	 */
	public List<AlarmAnalysisDataDto> getAlarmAnalysisData(AnalysisQuery analysisQuery,int analysisType){
		Long startTime = Long.parseLong(analysisQuery.getStartTime());
		Long endTime = Long.parseLong(analysisQuery.getEndTime());	
		//Map<String,AlarmAnalysisDataDto> map = new HashMap<String,AlarmAnalysisDataDto>();
		List<AlarmAnalysisDataDto> alarmAnalysisDataDtos = new ArrayList<AlarmAnalysisDataDto>();
		if(startTime <= endTime) {			
			//计算告警故障总时长（totalFaultMap）、平均恢复时间（avgRecMap）、各类型告警数（itempAlarmsMap）、恢复告警数（recAlarmstMap）、平均响应时间（avgResMap）
			List<Object[]> alarms = new ArrayList<Object[]>();
			List<Object[]> pressAlarms = new ArrayList<Object[]>();
			if(analysisType == 0) {
				alarms = alarmReportDao.selectAlarmByItemType(startTime, endTime);
				//计算告警压缩量
				pressAlarms = alarmReportDao.countpressAlarmByItemType(startTime, endTime);
			}
			if(analysisType == 1) {
				alarms = alarmReportDao.alarmsSolvedByPerson(startTime, endTime);
			}			
			List<Map<String,Float>> alarmsList = alarmAnalysisUtil.getAlarmListByItemType(alarms);		
			//计算告警升级量
			List<Object[]> upAlarms = alarmReportDao.countUpAlarmByItemType(startTime, endTime);
			Map<String,Long> upAlarmsItempType = alarmAnalysisUtil.countAlarmsByItemType(upAlarms);				
			Map<String,Long> pressAlarmsItempType = alarmAnalysisUtil.countAlarmsByItemType(pressAlarms);
			// 初始化监控类型		
			List<String> keys = alarmAnalysisUtil.getItemTypeList();
			Iterator<String> it = keys.iterator();				
			while (it.hasNext()) {	
				String itempType = it.next();
				if(alarms.size() > 0) {					
					AlarmAnalysisDataDto alarmAnalysisDataDto = new AlarmAnalysisDataDto();		
					alarmAnalysisDataDto.setItemType(itempType);
					alarmAnalysisDataDto.setAlarmCounts(Math.round((alarmsList.get(2).get(itempType))));				
					alarmAnalysisDataDto.setFaultTimes(alarmAnalysisUtil.formatTime(new BigDecimal(alarmsList.get(0).get(itempType)).longValue() ));
					if(alarmsList.get(5).get("totalfaultTimes") != 0) {
						alarmAnalysisDataDto.setFaultDutyRatio((float)Math.round(alarmsList.get(0).get(itempType)*100*100/(alarmsList.get(5).get("totalfaultTimes")))/100);
					}				
					alarmAnalysisDataDto.setMtta(alarmAnalysisUtil.formatTime(new BigDecimal(alarmsList.get(4).get(itempType)).longValue()));
					alarmAnalysisDataDto.setMttr(alarmAnalysisUtil.formatTime(new BigDecimal(alarmsList.get(1).get(itempType)).longValue()));
					alarmAnalysisDataDto.setUpAlarms(upAlarmsItempType.get(itempType));
					if(upAlarmsItempType.get(itempType) != 0) {					
						alarmAnalysisDataDto.setEscalationRate((float)Math.round(upAlarmsItempType.get(itempType)*100*100/alarmsList.get(2).get(itempType))/100);					
					}	
					if(pressAlarmsItempType.get(itempType) != 0) {
						alarmAnalysisDataDto.setPressDutyRatio((float)Math.round(pressAlarmsItempType.get(itempType)*100*100/alarmsList.get(2).get(itempType))/100);
					}
					alarmAnalysisDataDto.setPressAlarms(pressAlarmsItempType.get(itempType));	
					if(alarmsList.get(2).get(itempType) != 0) {
						alarmAnalysisDataDto.setTotalDutyRatio((float)Math.round(alarmsList.get(2).get(itempType)*100*100/alarms.size())/100);
					}								
					alarmAnalysisDataDtos.add(alarmAnalysisDataDto);
				}			
			}	
			AlarmAnalysisDataDto totalAnalysisDataDto = new AlarmAnalysisDataDto();	
			int num = alarms.size();
			totalAnalysisDataDto.setItemType("totalAnalysisDataDto");
			totalAnalysisDataDto.setAlarmCounts(num);
			totalAnalysisDataDto.setEscalationRate((float)Math.round(upAlarmsItempType.get("count")*100*100/num)/100);
			totalAnalysisDataDto.setFaultDutyRatio((float)100);
			totalAnalysisDataDto.setFaultTimes(alarmAnalysisUtil.formatTime(new BigDecimal(alarmsList.get(5).get("totalfaultTimes")).longValue()));
			totalAnalysisDataDto.setMtta(alarmAnalysisUtil.formatTime(new BigDecimal(alarmsList.get(5).get("totalmtta")).longValue()));
			totalAnalysisDataDto.setMttr(alarmAnalysisUtil.formatTime(new BigDecimal(alarmsList.get(5).get("totalmttr")).longValue()));
			totalAnalysisDataDto.setPressAlarms(pressAlarmsItempType.get("count"));
			totalAnalysisDataDto.setPressDutyRatio((float)Math.round(pressAlarmsItempType.get("count")*100*100/num)/100);
			totalAnalysisDataDto.setUpAlarms(upAlarmsItempType.get("count"));	
			totalAnalysisDataDto.setTotalDutyRatio((float)100);
			alarmAnalysisDataDtos.add(totalAnalysisDataDto);
		}				
		return alarmAnalysisDataDtos;
	}
	/**
	 * 对特定时间段内被压缩告警分析
	 * @param analysisQuery
	 * @return
	 */
	public List<AlarmDateByRecType> pressAlarmAnalysisData(AnalysisQuery analysisQuery){
		Long startTime = Long.parseLong(analysisQuery.getStartTime());
		Long endTime = Long.parseLong(analysisQuery.getEndTime());
		//Map<String,AlarmDateByRecType> map = new HashMap<String,AlarmDateByRecType>();
		List<AlarmDateByRecType> alarmDateByRecTypes = new ArrayList<AlarmDateByRecType>();
		if(startTime <= endTime) {
			//分析压缩告警
			List<Object[]> pressAlarms = alarmReportDao.alarmsSolvedAuto(startTime, endTime);
			if(pressAlarms.size() > 0) {
				//计算告警故障总时长（totalFaultMap）、平均恢复时间（avgRecMap）、各类型告警数（itempAlarmsMap）、恢复告警数（recAlarmstMap）、平均响应时间（avgResMap）
				List<Object[]> alarms = alarmReportDao.selectAlarmByItemType(startTime, endTime);	
				List<Map<String,Float>> alarmsList = alarmAnalysisUtil.getAlarmListByItemType(alarms);	
				
				for (Object[] objects : pressAlarms) {
					String itemType = objects[0].toString();
					long countAlarms = (long) objects[1];
					long totalRecTime = (long)objects[2];
					double avgRecTime = (Double)objects[3];
					AlarmDateByRecType alarmDateByRecType = new AlarmDateByRecType();
					alarmDateByRecType.setAlarmCounts(new Long(countAlarms).intValue());
					alarmDateByRecType.setAlarmsDutyRatio((float)Math.round(countAlarms*100*100/alarmsList.get(2).get(itemType))/100);
					alarmDateByRecType.setFaultDutyRatio((float)Math.round(totalRecTime*100*100/alarmsList.get(0).get(itemType))/100);
					alarmDateByRecType.setFaultTimes(alarmAnalysisUtil.formatTime(new BigDecimal(totalRecTime).longValue()));
					alarmDateByRecType.setMttr(alarmAnalysisUtil.formatTime(new BigDecimal(avgRecTime).longValue()));
					//map.put(itemType, alarmDateByRecType);
					alarmDateByRecTypes.add(alarmDateByRecType);
				}					
				// 初始化监控类型
				/*for (String itemType : alarmAnalysisUtil.getItemTypeList()) {
					if(!map.containsKey(itemType)) {
						AlarmDateByRecType alarmDateByRecType = new AlarmDateByRecType();
						alarmDateByRecType.setFaultTimes("0");
						alarmDateByRecType.setMttr("0");
						map.put(itemType, alarmDateByRecType);
					}
				}*/
			}			
		}
		
		return alarmDateByRecTypes;
	}
	
	/**
	 * 获取报文分析总览
	 * 
	 * @param date
	 * @return
	 */
	public OverviewDto getOverview(Long date) {
		Long alarmsByHostIp = alarmReportDao.countHostIps(date);
		List<Object[]> alarmInfo = alarmReportDao.selectAlarmInfo(date);
		OverviewDto dto = new OverviewDto();
		if (alarmInfo.size() > 0) {
			List<Float> alarmInfoList = alarmAnalysisUtil.getAlarmInfoList(alarmInfo);
			dto.setAlarmNum(new BigDecimal(alarmInfoList.get(0)).longValue());
			dto.setHostNum(alarmsByHostIp);
			dto.setMTTA((float) Math.round(alarmInfoList.get(1) * 100 / 1000 / 60) / 100);
			dto.setMTTR((float) Math.round(alarmInfoList.get(2) * 100 / 1000 / 60) / 100);
		}
		return dto;
	}
	
	/**
	 * 获取主机告警Top
	 * 
	 * @param date
	 */
	public Set<GroupByHostDto> getTopAlarm(Long date) {
		List<Object[]> list = alarmReportDao.groupByHostIp(date);
		List<Object[]> list1 = alarmReportDao.groupByHostIpItemType(date);
		Set<GroupByHostDto> byHostDtos = new TreeSet<GroupByHostDto>();
		for (Object[] object : list) {
			GroupByHostDto byHostDto = new GroupByHostDto();
			byHostDto.setHostIp(object[0].toString());
			byHostDto.setCount((long) object[1]);
			byHostDto.setHostName(object[2].toString());
			Map<String, Long> map = new HashMap<String, Long>();
			for (Object[] object2 : list1) {
				if (object2[0].toString().equals(object[0].toString())) {
					map.put(object2[1].toString(), (long) object2[2]);
				}
			}
			byHostDto.setItemTypeMap(map);
			byHostDtos.add(byHostDto);
		}

		return byHostDtos;
	}
	
	/**
	 * 获取告警级别占比
	 * 
	 * @param date
	 */
	public Set<GroupBySeverityDto> getSevProportion(Long date) {
		List<Object[]> list = alarmReportDao.groupBySeverity(date);
		Set<GroupBySeverityDto> bySeverityDtos = new TreeSet<GroupBySeverityDto>();
		for (Object[] objects : list) {
			GroupBySeverityDto bySeverityDto = new GroupBySeverityDto();
			bySeverityDto.setSeverity((Severity) objects[0]);
			bySeverityDto.setCount((Long) objects[1]);
			bySeverityDto
					.setProportion((float) (Math.round((float) ((Long) objects[1]) / ((Long) objects[2]) * 1000)) / 10);
			bySeverityDtos.add(bySeverityDto);
		}
		return bySeverityDtos;
	}
	
	/**
	 * 获取告警类型：告警总量、平均恢复时间、平均响应时间
	 * 
	 * @param date
	 */
	public Map<String, GroupByItemTypeDto> getItemProportion(Long date) {
		List<Object[]> list = alarmReportDao.groupByItemType(date);
		Map<String, GroupByItemTypeDto> byItemDtos = new HashMap<String, GroupByItemTypeDto>();
		List<Map<String, Float>> mapList = alarmAnalysisUtil.getAlarmListByItemType(list);
		List<String> itemTypeList = alarmAnalysisUtil.getItemTypeList();
		for (String itemType : itemTypeList) {
			GroupByItemTypeDto dto = new GroupByItemTypeDto();
			dto.setCount(mapList.get(2).get(itemType));
			dto.setItemType(itemType);
			dto.setMtta((float) Math.round(mapList.get(4).get(itemType) * 100 / 1000 / 60) / 100);
			dto.setMttr((float) Math.round(mapList.get(1).get(itemType) * 100 / 1000 / 60) / 100);
			dto.setProportion((float) Math.round(mapList.get(2).get(itemType) * 100 * 100 / list.size()) / 100);
			byItemDtos.put(itemType, dto);
		}
		return byItemDtos;
	}

	/**
	 * 获取未恢复告警及主机数量
	 * 
	 * @param date
	 */
	public AlarmAndHostNumDto getOpenAlarmNum(Long date) {
		AlarmAndHostNumDto dto = new AlarmAndHostNumDto();
		dto.setHostNum(alarmReportDao.groupByHostIpOpen(date).size());
		dto.setAlarmNum(alarmReportDao.getAlarmNumOpen(date));
		return dto;
	}
	
	/**
	 * 获取指定周期内告警数量趋势
	 * 
	 * @param cycle
	 * @return
	 */
	public Set<ITrendData> getAlarmTrend(CycleType cycle) {
		Long timePoint = CycleType.getTimePoint(cycle);
		List<Object[]> list = alarmReportDao.groupByCycle(cycle, timePoint);
		return CycleUtil.padCycle(cycle, list, timePoint);
	}
	
	/**
	 * 获取指定周期内MTTA、MTTR的走势
	 * 
	 * @param cycle
	 * @return
	 */
	public Set<ITrendData> getMTTARTrend(CycleType cycle) {
		Long timePoint = CycleType.getTimePoint(cycle);
		List<Object[]> list = alarmReportDao.getAlarmByCycle(cycle, timePoint);
		Set<ITrendData> objects = CycleUtil.padCycle(cycle, list, timePoint);
		for (ITrendData iTrendData : objects) {
			if (!(iTrendData.getData() instanceof Long) && !StringUtils.isEmpty(iTrendData.getData())) {
				List<Object[]> alarmLifeEntities = alarmReportDao
						.selectBatchByIds(((String) iTrendData.getData()).split(","));
				if (alarmLifeEntities.size() > 0) {
					List<Float> alarmInfoList = alarmAnalysisUtil.getAlarmInfoList(alarmLifeEntities);
					MTTARTrendDto mttarTrendDto = new MTTARTrendDto();
					mttarTrendDto.setMTTA(Math.round(alarmInfoList.get(1) * 100 / 1000 / 60) / 100);
					mttarTrendDto.setMTTR(Math.round(alarmInfoList.get(2) * 100 / 1000 / 60) / 100);
					iTrendData.setData(mttarTrendDto);
				}
			} else {
				iTrendData.setData(null);
			}
		}
		return objects;
	}

}
