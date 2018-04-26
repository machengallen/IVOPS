package com.iv.aggregation.clean;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.iv.aggregation.api.constant.StrategyCycle;
import com.iv.aggregation.dao.IAlarmCleanStrategyDao;
import com.iv.aggregation.dao.IAlarmLifeDao;
import com.iv.aggregation.dao.impl.AlarmCleanStrategyDaoImpl;
import com.iv.aggregation.dao.impl.AlarmLifeDaoImpl;
import com.iv.aggregation.entity.AlarmCleanStrategyEntity;
import com.iv.aggregation.feign.clients.ITenantServiceClient;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;

@Component
@Configurable
@EnableScheduling
public class ScheduledTasks {

	private final static Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	private AlarmCleanStrategyDaoImpl strategyDao;
	@Autowired
	private ITenantServiceClient tenantServiceClient;
	private static final IAlarmLifeDao ALARM_LIFE_DAO = AlarmLifeDaoImpl.getInstance();

	public void work() throws SchedulerException {
		LOGGER.info("*******************告警数据定时清理*******************");

		try {
			// 获取所有项目组，执行对应清理策略
			List<SubEnterpriseInfoDto> subEnterpriseInfoDtos = tenantServiceClient.getSubEnterpriseAll();
			if(null != subEnterpriseInfoDtos) {
				for (SubEnterpriseInfoDto subEnterpriseInfoDto : subEnterpriseInfoDtos) {
					
					String tenantId = subEnterpriseInfoDto.getTenantId();
					LOGGER.info(
							"租户：" + subEnterpriseInfoDto.getEnterprise().getName() + " 项目组：" + subEnterpriseInfoDto.getName());
					Calendar calendar = Calendar.getInstance(Locale.CHINA);
					AlarmCleanStrategyEntity strategyEntity = strategyDao.selectById(IAlarmCleanStrategyDao.id, tenantId);
					if (null == strategyEntity) {
						strategyEntity = initStrategy(tenantId);
					}
					calendar.add(Calendar.MONTH, strategyEntity.getCycleType().getMonths());
					// System.out.println(dao.selectById(IAlarmCleanStrategyDao.id).getCycleType().getMonths());
					ALARM_LIFE_DAO.delBeforeTimestamp(calendar.getTimeInMillis(), tenantId);
				}
			}
		} catch (RuntimeException e) {
			LOGGER.error("定时任务执行失败", e);
		}
	}

	// @Scheduled(cron = "0/10 * * * * ?")
	public void doWork() {
		System.out.println("test");
	}

	@PostConstruct
	public void initConfig() {
		// System.out.println("*****************初始化告警数据清理规则*****************");
		// 对所有租户初始化告警数据清理策略
		try {
			List<SubEnterpriseInfoDto> subEnterpriseInfoDtos = tenantServiceClient.getSubEnterpriseAll();
			if(null != subEnterpriseInfoDtos) {
				for (SubEnterpriseInfoDto subEnterpriseInfoDto : subEnterpriseInfoDtos) {

					String tenantId = subEnterpriseInfoDto.getTenantId();
					// 初始化告警清理规则
					if (null == strategyDao.selectById(IAlarmCleanStrategyDao.id, tenantId)) {
						// 默认告警数据保留一年
						AlarmCleanStrategyEntity cleanStrategyEntity = new AlarmCleanStrategyEntity();
						cleanStrategyEntity.setCycleType(StrategyCycle.YEAR);
						cleanStrategyEntity.setId(IAlarmCleanStrategyDao.id);
						strategyDao.saveStrategy(cleanStrategyEntity, tenantId);
					}
				}
			}
		} catch (RuntimeException e) {
			LOGGER.error("定时任务初始化失败", e);
		}
	}

	public AlarmCleanStrategyEntity initStrategy(String tenantId) {
		AlarmCleanStrategyEntity cleanStrategyEntity = new AlarmCleanStrategyEntity();
		cleanStrategyEntity.setCycleType(StrategyCycle.THREE_MONTHS);
		cleanStrategyEntity.setId(IAlarmCleanStrategyDao.id);
		cleanStrategyEntity = strategyDao.saveStrategy(cleanStrategyEntity, tenantId);
		return cleanStrategyEntity;
	}
}
