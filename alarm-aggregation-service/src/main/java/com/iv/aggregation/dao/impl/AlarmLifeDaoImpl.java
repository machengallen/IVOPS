package com.iv.aggregation.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import com.iv.aggregation.api.constant.AgentType;
import com.iv.aggregation.api.dto.AlarmQueryDto;
import com.iv.aggregation.dao.AlarmPaging;
import com.iv.aggregation.dao.IAlarmLifeDao;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmRecoveryEntity;
import com.iv.aggregation.entity.AlarmSourceEntity;
import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.CycleType;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;

/**
 * 统一告警数据dao实现
 * @author macheng
 * 2018年4月2日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public class AlarmLifeDaoImpl implements IAlarmLifeDao {

	private static final AlarmLifeDaoImpl DAO = new AlarmLifeDaoImpl();

	private AlarmLifeDaoImpl() {

	}

	public static AlarmLifeDaoImpl getInstance() {
		return DAO;
	}

	@Override
	public AlarmLifeEntity saveAlarmLife(final AlarmLifeEntity alarmLifeEntity) throws RuntimeException {
		return (AlarmLifeEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.merge(alarmLifeEntity);
				// ses.saveOrUpdate(alarmLifeEntity);
				return alarmLifeEntity;
			}
		}, alarmLifeEntity.getAlarm().getTenantId());
	}

	@Override
	public AlarmLifeEntity saveOrUpdateAlarmLife(final AlarmLifeEntity alarmLifeEntity) throws RuntimeException {
		return (AlarmLifeEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// ses.merge(alarmLifeEntity);
				ses.saveOrUpdate(alarmLifeEntity);
				return alarmLifeEntity;
			}
		}, alarmLifeEntity.getAlarm().getTenantId());
	}

	@Override
	public AlarmLifeEntity selectAlarmLifeByAlarmSrc(final AlarmSourceEntity sourceEntity) throws RuntimeException {
		return (AlarmLifeEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				AlarmLifeEntity alarmLifeEntity = (AlarmLifeEntity) ses.createCriteria(AlarmLifeEntity.class, "a")
						.add(Restrictions.eq("a.alarm", sourceEntity)).uniqueResult();
				return alarmLifeEntity;
			}
		}, sourceEntity.getTenantId());
	}

	@Override
	public void updateAlarmStatus(final String alarmId, final Integer hireHandlerCurrent, final Integer hireHandlerLast,
			final AlarmRecoveryEntity recovery, final AlarmStatus alarmStatus, final Byte upgrade)
			throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				AlarmLifeEntity lifeEntity = (AlarmLifeEntity) ses.get(AlarmLifeEntity.class, alarmId);
				if (null != hireHandlerCurrent) {
					lifeEntity.setHandlerCurrent(hireHandlerCurrent);
				}
				if (null != hireHandlerLast) {
					lifeEntity.setHandlerLast(hireHandlerLast);
				}
				if (null != recovery) {
					lifeEntity.setRecovery(recovery);
				}
				if (null != alarmStatus) {
					lifeEntity.setAlarmStatus(alarmStatus);
				}
				if (null != upgrade) {
					lifeEntity.setUpgrade(upgrade);
				}
				return null;
			}
		});
	}

	@Override
	public AlarmLifeEntity selectAlarmLifeById(final String id, final String tenantId) throws RuntimeException {
		return (AlarmLifeEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(AlarmLifeEntity.class, id);
			}
		}, tenantId);
	}

	@Override
	public AlarmLifeEntity selectAlarmLifeById(final String id) throws RuntimeException {
		return (AlarmLifeEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(AlarmLifeEntity.class, id);
			}
		});
	}

	@Override
	public void updateAlarmLife(final AlarmLifeEntity alarmLifeEntity) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.update(alarmLifeEntity);
				return null;
			}
		});

	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public AlarmPaging selectPaging(final int index, final int nums,
	 * final AlarmQuery query) throws RuntimeException { return (AlarmPaging)
	 * HibernateTemplate.execute(new HibernateCallBack() {
	 * 
	 * @Override public Object doInHibernate(Session ses) throws HibernateException
	 * { AlarmPaging paging = new AlarmPaging(); StringBuilder hql = new
	 * StringBuilder("from AlarmLifeEntity a where 1=1"); boolean flag =
	 * true;//新增1=1，恒为true if (!StringUtils.isEmpty(query.getAlarmStatus())) {
	 * hql.append(" and a.alarmStatus=").append("'").append(query.getAlarmStatus()).
	 * append("'"); flag = true; }
	 * 
	 * if (!StringUtils.isEmpty(query.getId())) { if (flag) { hql.append(" and "); }
	 * else { flag = true; }
	 * hql.append("a.id like ").append("'%").append(query.getId()).append("%'"); }
	 * 
	 * if (!StringUtils.isEmpty(query.getHostIp())) { if (flag) {
	 * hql.append(" and "); } else { flag = true; }
	 * hql.append("a.alarm.hostIp like ").append("'%").append(query.getHostIp()).
	 * append("%'"); }
	 * 
	 * if (!StringUtils.isEmpty(query.getHostName())) { if (flag) {
	 * hql.append(" and "); } else { flag = true; }
	 * hql.append("a.alarm.hostName like ").append("'%").append(query.getHostName())
	 * .append("%'"); }
	 * 
	 * if (!StringUtils.isEmpty(query.getSeverity())) { if (flag) {
	 * hql.append(" and "); } else { flag = true; }
	 * hql.append("a.alarm.severity=").append(query.getSeverity().ordinal()); }
	 * 
	 * if (!StringUtils.isEmpty(query.getItemType())) { if (flag) {
	 * hql.append(" and "); } else { flag = true; }
	 * hql.append("a.itemType=").append("'").append(query.getItemType()).append("'")
	 * ; } paging.setTotalCount(ses.createQuery(hql.toString()).list().size());
	 * paging.setEntries(ses.createQuery(hql.append(" order by a.triDate desc").
	 * toString()) .setFirstResult(index).setMaxResults(nums).list()); return
	 * paging; } }); }
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupByHostIp(final Long date) throws RuntimeException {
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// return ses.createCriteria(AlarmLifeEntity.class,
				// "a").setProjection(Projections.projectionList().add(Projections.groupProperty("a.alarm.hostIp"))).list();
				return ses.createQuery(
						"select a.alarm.hostIp,count(1),a.alarm.hostName from AlarmLifeEntity a where a.triDate>? group by a.alarm.hostIp")
						.setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupBySeverity(final Long date) throws RuntimeException {
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery(
						"select b.alarm.severity,count(1),(select count(1) from AlarmLifeEntity a where a.triDate>?) "
								+ "from AlarmLifeEntity b where b.triDate>? group by b.alarm.severity")
						.setParameter(0, date).setParameter(1, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> groupByHostIpOpen(final Long date) throws RuntimeException {
		return (List<Object>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery(
						"select count(1) from AlarmLifeEntity a where a.triDate>? and a.alarmStatus<>? group by a.alarm.hostIp")
						.setParameter(0, date).setParameter(1, AlarmStatus.CLOSED).list();
			}
		});
	}

	@Override
	public Long getAlarmNumOpen(final Long date) throws RuntimeException {
		return (Long) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select count(1) from AlarmLifeEntity a where a.triDate>? and a.alarmStatus<>?")
						.setParameter(0, date).setParameter(1, AlarmStatus.CLOSED).uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupByCycle(final CycleType cycle, final Long date) throws RuntimeException {
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				String hql = null;
				switch (cycle) {
				case DAY:
					hql = "select DAY(FROM_UNIXTIME(a.triDate/1000)),HOUR(FROM_UNIXTIME(a.triDate/1000)),count(1) "
							+ "from AlarmLifeEntity a where a.triDate>? "
							+ "group by DAY(FROM_UNIXTIME(a.triDate/1000)),HOUR(FROM_UNIXTIME(a.triDate/1000))";
					break;
				case WEEK:
				case TWO_WEEKS:
				case MONTH:
					hql = "select MONTH(FROM_UNIXTIME(a.triDate/1000)),DAY(FROM_UNIXTIME(a.triDate/1000)),count(1) "
							+ "from AlarmLifeEntity a where a.triDate>? "
							+ "group by MONTH(FROM_UNIXTIME(a.triDate/1000)),DAY(FROM_UNIXTIME(a.triDate/1000))";
					break;
				case TWO_MONTHS:
					hql = "select MONTH(FROM_UNIXTIME(a.triDate/1000)),WEEK(FROM_UNIXTIME(a.triDate/1000)),count(1) "
							+ "from AlarmLifeEntity a where a.triDate>? "
							+ "group by MONTH(FROM_UNIXTIME(a.triDate/1000)),WEEK(FROM_UNIXTIME(a.triDate/1000))";
					break;
				case HALF_YEAR:
					hql = "select YEAR(FROM_UNIXTIME(a.triDate/1000)),MONTH(FROM_UNIXTIME(a.triDate/1000)),WEEK(FROM_UNIXTIME(a.triDate/1000)),count(1) "
							+ "from AlarmLifeEntity a where a.triDate>? "
							+ "group by YEAR(FROM_UNIXTIME(a.triDate/1000)),MONTH(FROM_UNIXTIME(a.triDate/1000)),WEEK(FROM_UNIXTIME(a.triDate/1000))";
					break;
				case YEAR:
					hql = "select YEAR(FROM_UNIXTIME(a.triDate/1000)),MONTH(FROM_UNIXTIME(a.triDate/1000)),count(1) "
							+ "from AlarmLifeEntity a where a.triDate>? "
							+ "group by YEAR(FROM_UNIXTIME(a.triDate/1000)),MONTH(FROM_UNIXTIME(a.triDate/1000))";
					break;
				default:
					break;
				}

				return ses.createQuery(hql).setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAlarmByCycle(final CycleType cycle, final Long date) throws RuntimeException {
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				String sql = null;
				switch (cycle) {
				case DAY:
					sql = "select DAY(FROM_UNIXTIME(a.tri_date/1000)),HOUR(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from Alarm_Life a where a.tri_date>? "
							+ "group by DAY(FROM_UNIXTIME(a.tri_date/1000)),HOUR(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case WEEK:
				case TWO_WEEKS:
				case MONTH:
					sql = "select MONTH(FROM_UNIXTIME(a.tri_date/1000)),DAY(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from Alarm_Life a where a.tri_date>? "
							+ "group by MONTH(FROM_UNIXTIME(a.tri_date/1000)),DAY(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case TWO_MONTHS:
					sql = "select MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from Alarm_Life a where a.tri_date>? "
							+ "group by MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case HALF_YEAR:
					sql = "select YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from Alarm_Life a where a.tri_date>? "
							+ "group by YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case YEAR:
					sql = "select YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from Alarm_Life a where a.tri_date>? "
							+ "group by YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				default:
					break;
				}

				return ses.createSQLQuery(sql).setParameter(0, date).list();
			}
		});
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<AlarmLifeEntity> selectBatchByIds(final String[] ids) {
		return (List<AlarmLifeEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				List<AlarmLifeEntity> entities = new ArrayList<AlarmLifeEntity>();
				for (String id : ids) {
					entities.add((AlarmLifeEntity) ses.get(AlarmLifeEntity.class, id));
				}
				return entities;
			}
		});
	}*/

	@Override
	public void delBeforeTimestamp(long timestamp, String tenantId) throws RuntimeException {
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				List list = ses.createQuery("from AlarmLifeEntity a where a.triDate<?")
						.setParameter(0, timestamp).list();
				for (Object alarmLifeEntity : list) {
					ses.delete(alarmLifeEntity);
				}
				// ses.createQuery("delete AlarmLifeEntity a where a.triDate<?").setParameter(0,
				// timestamp).executeUpdate();
				return null;
			}
		}, tenantId);
	}

	@Override
	public AlarmPaging selectPaging(int index, int nums, AlarmQueryDto query, Integer localAuthId) throws RuntimeException {

		return (AlarmPaging) HibernateTemplate.execute(new HibernateCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				StringBuilder hql = new StringBuilder("from AlarmLifeEntity a where 1=1");
				if (!StringUtils.isEmpty(query.getId())) {
					hql.append(" and ").append("a.id like ").append("'%").append(query.getId()).append("%'");
				}
				if (!StringUtils.isEmpty(query.getHostName())) {
					hql.append(" and ").append("a.alarm.hostName like ").append("'%").append(query.getHostName())
							.append("%'");
				}
				if (!StringUtils.isEmpty(query.getHostIp())) {
					hql.append(" and ").append("a.alarm.hostIp like ").append("'%").append(query.getHostIp())
							.append("%'");
				}
				if (!StringUtils.isEmpty(query.getAlarmStatus())) {
					hql.append(" and ").append("a.alarmStatus=").append("'").append(query.getAlarmStatus()).append("'");
				}
				if (!StringUtils.isEmpty(query.getSeverity())) {
					hql.append(" and ").append("a.alarm.severity=").append(query.getSeverity().ordinal());
				}
				if (!StringUtils.isEmpty(query.getItemType())) {
					hql.append(" and ").append("a.itemType=").append("'").append(query.getItemType()).append("'");
				}
				if (query.getAlarmQueryType().ordinal() == 0 && null != localAuthId) {
					hql.append(" and ").append("a.handlerCurrent=").append(localAuthId);
				}
				// System.out.println(hql.toString());
				AlarmPaging paging = new AlarmPaging();
				paging.setTotalCount((long) ses.createQuery("select count(*) " + hql.toString()).uniqueResult());
				paging.setEntries(ses.createQuery(hql.append(" order by a.triDate desc").toString())
						.setFirstResult(index).setMaxResults(nums).list());
				return paging;
			}
		});
	}
		

	@Override
	public AlarmPaging selectByCurTeamPaging(int index, int nums, AlarmQueryDto query, Set<String> alarmLifeIds)
			throws RuntimeException {

		return (AlarmPaging) HibernateTemplate.execute(new HibernateCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				AlarmPaging paging = new AlarmPaging();
				if (alarmLifeIds.size() > 0) {
					StringBuilder hql = new StringBuilder("from AlarmLifeEntity a where 1=1");
					if (!StringUtils.isEmpty(query.getId())) {
						hql.append(" and ").append("a.id like ").append("'%").append(query.getId()).append("%'");
					}
					if (!StringUtils.isEmpty(query.getHostName())) {
						hql.append(" and ").append("a.alarm.hostName like ").append("'%").append(query.getHostName())
								.append("%'");
					}
					if (!StringUtils.isEmpty(query.getHostIp())) {
						hql.append(" and ").append("a.alarm.hostIp like ").append("'%").append(query.getHostIp())
								.append("%'");
					}
					if (!StringUtils.isEmpty(query.getAlarmStatus())) {
						hql.append(" and ").append("a.alarmStatus=").append("'").append(query.getAlarmStatus())
								.append("'");
					}
					if (!StringUtils.isEmpty(query.getSeverity())) {
						hql.append(" and ").append("a.alarm.severity=").append(query.getSeverity().ordinal());
					}
					if (!StringUtils.isEmpty(query.getItemType())) {
						hql.append(" and ").append("a.itemType=").append("'").append(query.getItemType()).append("'");
					}
					hql.append(" and ").append("a.id in (:alarmLifeIds) ");
					// System.out.println(hql.toString());
					paging.setTotalCount((long) ses.createQuery("select count(*) " + hql.toString())
							.setParameterList("alarmLifeIds", alarmLifeIds).uniqueResult());
					paging.setEntries(ses.createQuery(hql.append(" order by a.triDate desc").toString())
							.setParameterList("alarmLifeIds", alarmLifeIds).setFirstResult(index).setMaxResults(nums)
							.list());
				}

				return paging;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupByItemType(Long date) throws RuntimeException {
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select a.itemType,a.triDate,a.recDate,a.alarmEvent.resDate from AlarmLifeEntity a where a.triDate>?")
						.setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupByHostIpItemType(Long date) throws RuntimeException {
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select a.alarm.hostIp,a.itemType,count(1) from AlarmLifeEntity a where a.triDate>? group by a.alarm.hostIp,a.itemType")
						.setParameter(0, date).list();			
			}
		});
	}
	
	@Override
	public Long countHostIps(Long date) throws RuntimeException {
		return (Long) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select count(distinct a.alarm.hostIp) from AlarmLifeEntity a where a.triDate>?")
						.setParameter(0, date).uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> selectAlarmInfo(Long date) throws RuntimeException {
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select a.triDate,a.recDate,a.resDate from AlarmEventDateEntity a where a.triDate>?")
						.setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> selectBatchByIds(String[] ids) throws RuntimeException {
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				List<Object[]> objects = new ArrayList<Object[]>();
				for (String id : ids) {
					Object[] object= (Object[] )ses.createQuery("select a.triDate,a.recDate,a.alarmEvent.resDate from AlarmLifeEntity a where a.id=?")
							.setParameter(0, id).uniqueResult();
					if(null != object) {
						objects.add(object);
					}					
				}
				return objects;
			}
		});
	}

	@Override
	public void delById(String id) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.delete(ses.load(AlarmLifeEntity.class, id));
				return null;
			}
		});
		
	}
	
	@Override
	public AlarmSourceEntity saveAlarmSource(final AlarmSourceEntity alarmSourceEntity) throws RuntimeException {
		return (AlarmSourceEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(alarmSourceEntity);
				return alarmSourceEntity;
			}
		}, alarmSourceEntity.getTenantId());

	}

	@Override
	public AlarmRecoveryEntity saveAlarmRecovery(final AlarmRecoveryEntity alarmRecoveryEntity)
			throws RuntimeException {
		return (AlarmRecoveryEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(alarmRecoveryEntity);
				return alarmRecoveryEntity;
			}
		}, alarmRecoveryEntity.getAlarmSourceEntity().getTenantId());
	}

	@Override
	public AlarmSourceEntity selectAlarmSourceByEventId(final String eventId, final String monitorIp,
			final String tenantId, final AgentType agentType) throws RuntimeException {
		return (AlarmSourceEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {

				List entities = ses.createCriteria(AlarmSourceEntity.class, "a")
						.add(Restrictions.eq("a.eventId", Long.parseLong(eventId)))
						.add(Restrictions.eq("a.monitorIp", monitorIp))
						.add(Restrictions.eq("a.agentType", agentType))
						.list();

				return entities.get(entities.size() - 1);
			}
		}, tenantId);
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public List<AlarmLifeEntity> selectBatchByIds(final String[] ids) {
		return (List<AlarmLifeEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				List<AlarmLifeEntity> entities = new ArrayList<AlarmLifeEntity>();
				for (String id : ids) {
					entities.add((AlarmLifeEntity) ses.get(AlarmLifeEntity.class, id));
				}
				return entities;
			}
		});
	}*/

}
