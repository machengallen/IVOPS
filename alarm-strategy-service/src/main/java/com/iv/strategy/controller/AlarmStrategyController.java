package com.iv.strategy.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.dto.IdListDto;
import com.iv.common.enumeration.ItemType;
import com.iv.common.enumeration.Severity;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.strategy.api.constant.ErrorMsg;
import com.iv.strategy.api.constant.OpsStrategy;
import com.iv.strategy.api.constant.StrategyCycle;
import com.iv.strategy.api.dto.AlarmStrategyDto;
import com.iv.strategy.api.dto.StrategyLogDto;
import com.iv.strategy.api.dto.StrategyQueryDto;
import com.iv.strategy.api.dto.UpgadeStrategyDto;
import com.iv.strategy.api.service.IAlarmStrategyService;
import com.iv.strategy.dao.IAlarmStrategyDao;
import com.iv.strategy.dao.StrategyPaging;
import com.iv.strategy.dao.impl.AlarmStrategyDaoImpl;
import com.iv.strategy.entity.AlarmStrategyEntity;
import com.iv.strategy.entity.StrategyLogEntity;
import com.iv.strategy.feign.client.IAlarmAggregationClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "告警策略管理系列接口")
public class AlarmStrategyController implements IAlarmStrategyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmStrategyController.class);
	private static final IAlarmStrategyDao ALARM_STRATEGY_DAO = AlarmStrategyDaoImpl.getInstance();

	@Autowired
	private IAlarmAggregationClient alarmAggregationClient;
	@ApiOperation("查询指定分派策略")
	@Override
	public ResponseDto getStrategy(StrategyQueryDto queryDto) {
		try {
			AlarmStrategyEntity entity = ALARM_STRATEGY_DAO.selectStrategy(queryDto.getSeverity(), queryDto.getItemType(), queryDto.getTenantId());
			AlarmStrategyDto dto = new AlarmStrategyDto();
			BeanUtils.copyProperties(entity, dto, "logs");
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(dto);
			return responseDto;
		} catch (RuntimeException e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.CONFIG_STRATEGY_FAILED);
		}
		
	}

	/**
	 * 升级提醒策略存储
	 * 
	 * @param pengdingTimes
	 * @param pendingAgain
	 * @param prosessingUpgrade
	 * @return
	 */
	@RequestMapping(value = "/config/strategy", method = RequestMethod.POST)
	@ApiOperation("《管理员》分派策略配置")
	public ResponseDto configStrategy(HttpServletRequest request, @RequestBody UpgadeStrategyDto upgadeStrategy) {
		try {
			if (!upgadeStrategy.no0check()) {
				return ResponseDto.builder(ErrorMsg.NOT_ZERO_ERROR);
			}

			String realName = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("realName");
			Set<StrategyLogEntity> strategyLogs = new HashSet<StrategyLogEntity>();
			AlarmStrategyEntity lifeStrategyEntity = null;
			if (CollectionUtils.isEmpty(upgadeStrategy.getIdList())) {
				// 新添加
				lifeStrategyEntity = new AlarmStrategyEntity();
				strategyLogs.add(new StrategyLogEntity(new Date(), OpsStrategy.CREATE, realName, null));
			} else {
				// 编辑
				lifeStrategyEntity = ALARM_STRATEGY_DAO.selectById(upgadeStrategy.getIdList().get(0));
				strategyLogs = lifeStrategyEntity.getLogs();
				strategyLogs.add(new StrategyLogEntity(new Date(), OpsStrategy.EDIT, realName, null));
			}
			lifeStrategyEntity.setTag(upgadeStrategy.getTag());
			lifeStrategyEntity.setDelayTime(upgadeStrategy.getDelayTime());
			lifeStrategyEntity.setUpgradeTime(upgadeStrategy.getUpgradeTime());
			lifeStrategyEntity.setSeverity(upgadeStrategy.getSeverities().get(0));
			lifeStrategyEntity.setItemType(upgadeStrategy.getItemTypes().get(0));
			List<Short> groupIds = new ArrayList<Short>();
			for (Short groupId : upgadeStrategy.getGroupIds()) {
				groupIds.add(groupId);
			}
			lifeStrategyEntity.setGroupIds(groupIds);
			lifeStrategyEntity.setNoticeModel(upgadeStrategy.getNoticeModel());
			lifeStrategyEntity.setLogs(strategyLogs);
			ALARM_STRATEGY_DAO.saveStrategy(lifeStrategyEntity);

			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.CONFIG_STRATEGY_FAILED);
		}
	}

	/**
	 * 查询升级策略(分页)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/strategy", method = RequestMethod.POST)
	@ApiOperation("查询分派策略(分页)")
	public ResponseDto getStrategyPaging(@RequestBody StrategyQueryDto query) {

		try {
			StrategyPaging strategyPaging = ALARM_STRATEGY_DAO
					.selectByCurPage((query.getCurPage() - 1) * query.getItems(), query.getItems(), query);
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(strategyPaging);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.GET_STRATEGY_FAILED);
		}
	}

	/**
	 * 查询升级策略(全部)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/allStrategy", method = RequestMethod.GET)
	@ApiOperation("查询分派策略(全部)")
	public ResponseDto getAllStrategy(HttpServletRequest request) {

		try {
			int netCount = ALARM_STRATEGY_DAO.selectCountsByType("network");
			int systemCount = ALARM_STRATEGY_DAO.selectCountsByType("system");
			int databaseCount = ALARM_STRATEGY_DAO.selectCountsByType("database");
			int virtualCount = ALARM_STRATEGY_DAO.selectCountsByType("virtual");
			int softwareCount = ALARM_STRATEGY_DAO.selectCountsByType("software");
			int undefinedCount = ALARM_STRATEGY_DAO.selectCountsByType("undefined");
			int severities = Severity.values().length - 1;
			int itempTypes = ItemType.values().length;
			int totlalCount = severities * itempTypes;
			Map<String, Object> map = new HashMap<>();
			Set roles = ((Map)JWTUtil.getJWtJson(request.getHeader("Authorization")).get("permissions")).keySet();
			map.put("curHireRoles", roles);
			map.put("netPercentage", getPercentage(netCount, severities));
			map.put("systemPercentage", getPercentage(systemCount, severities));
			map.put("databasePercentage", getPercentage(databaseCount, severities));
			map.put("virtualPercentage", getPercentage(virtualCount, severities));
			map.put("softwarePercentage", getPercentage(softwareCount, severities));
			map.put("undefinedPercentage", getPercentage(undefinedCount, severities));
			map.put("totalPercentage", getPercentage(ALARM_STRATEGY_DAO.selectAll().getCount(), totlalCount));
			map.put("strategies", ALARM_STRATEGY_DAO.selectAll().getStrategies());
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(map);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.GET_STRATEGY_FAILED);
		}
	}
	
	private String getPercentage(int itemTypeCount, int severities) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		String result = numberFormat.format((float) itemTypeCount / (float) severities * 100);
		return result;
	}

	/**
	 * 查询指定升级策略
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/Onestrategy", method = RequestMethod.POST)
	@ApiOperation("查询指定升级策略")
	public ResponseDto getTargetStrategy(@RequestBody IdListDto ids) {

		try {
			List<AlarmStrategyEntity> strategyEntities = ALARM_STRATEGY_DAO.selectBatchStrategies(ids.getIds());
			UpgadeStrategyDto strategy = new UpgadeStrategyDto();
			strategy.setNoticeModel(strategyEntities.get(0).getNoticeModel());
			strategy.setTag(strategyEntities.get(0).getTag());
			strategy.setDelayTime(strategyEntities.get(0).getDelayTime());
			strategy.setUpgradeTime(strategyEntities.get(0).getUpgradeTime());
			strategy.setGroupIds(strategyEntities.get(0).getGroupIds());
			Set<StrategyLogDto> logDtos = new HashSet<StrategyLogDto>();
			for (StrategyLogEntity alarmStrategyEntity : strategyEntities.get(0).getLogs()) {
				StrategyLogDto logDto = new StrategyLogDto();
				BeanUtils.copyProperties(alarmStrategyEntity, logDto);
				logDtos.add(logDto);
			}
			strategy.setLogs(logDtos);
			// 包装id/告警级别/告警类型
			List<Severity> severities = new ArrayList<Severity>();
			List<String> itemTypes = new ArrayList<String>();
			List<String> idList = new ArrayList<String>();
			for (AlarmStrategyEntity entity : strategyEntities) {
				if (!severities.contains(entity.getSeverity())) {
					severities.add(entity.getSeverity());
				}
				if (!itemTypes.contains(entity.getItemType()) && !StringUtils.isEmpty(entity.getItemType())) {
					itemTypes.add(transItemType(entity.getItemType()));
				}
				idList.add(entity.getId());
			}
			strategy.setIdList(idList);
			strategy.setSeverities(severities);
			strategy.setItemTypes(itemTypes);
			
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(strategy);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			return ResponseDto.builder(ErrorMsg.GET_STRATEGY_FAILED);
		}
	}
	
	/**
	 * 将告警类型转换为中文描述
	 * 
	 * @param itemType
	 * @return
	 */
	private String transItemType(String itemType) {
		if ("network".equals(itemType)) {
			return "网络";
		}
		if ("system".equals(itemType)) {
			return "系统";
		}
		if ("database".equals(itemType)) {
			return "数据库";
		}
		if ("virtual".equals(itemType)) {
			return "虚拟机";
		}
		if ("software".equals(itemType)) {
			return "软件";
		}
		return "未定义";

	}

	/**
	 * 删除升级策略
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del/strategy", method = RequestMethod.POST)
	@ApiOperation("《管理员》删除升级策略")
	public ResponseDto delStrategies(@RequestBody IdListDto list) {
		ResponseDto responseDto = new ResponseDto();
		try {
			ALARM_STRATEGY_DAO.delStrategies(list.getIds());
			responseDto.setErrorMsg(ErrorMsg.OK);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			responseDto.setErrorMsg(ErrorMsg.DELETE_STRATRGY_FAILED);
			return responseDto;
		}

	}

	@RequestMapping(value = "/config/clean/quartz", method = RequestMethod.GET)
	@ApiOperation("《管理员》配置告警数据清理频率")
	public ResponseDto setAlarmCleanQuartz(@RequestParam String exp) {
		ResponseDto responseDto = new ResponseDto();
		try {
			alarmAggregationClient.updateAlarmCleanQuartz(exp);
			responseDto.setErrorMsg(ErrorMsg.OK);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			responseDto.setErrorMsg(ErrorMsg.ALARM_CLEAN_SET_FAILED);
			return responseDto;
		}
	}

	@RequestMapping(value = "/config/clean", method = RequestMethod.GET)
	@ApiOperation("《管理员》配置告警数据保留周期")
	public ResponseDto setAlarmCleanCycle(@RequestParam StrategyCycle cycle) {
		ResponseDto responseDto = new ResponseDto();
		try {
			alarmAggregationClient.updateAlarmCleanCycle(com.iv.aggregation.api.constant.StrategyCycle.valueOf(cycle.name()));
			responseDto.setErrorMsg(ErrorMsg.OK);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误：", e);
			responseDto.setErrorMsg(ErrorMsg.ALARM_CLEAN_SET_FAILED);
			return responseDto;
		}
	}

	@Override
	public boolean strategyExist(short groupId) {
		int count = ALARM_STRATEGY_DAO.countByGroupId(groupId);
		if(count > 0) {
			return true;
		}
		return false;
	}

}