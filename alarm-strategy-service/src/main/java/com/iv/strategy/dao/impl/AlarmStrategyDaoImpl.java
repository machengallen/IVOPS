package com.iv.strategy.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.util.StringUtils;

import com.iv.common.enumeration.Severity;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.strategy.api.dto.StrategyQueryDto;
import com.iv.strategy.dao.IAlarmStrategyDao;
import com.iv.strategy.dao.StrategyPaging;
import com.iv.strategy.entity.AlarmStrategyEntity;

public class AlarmStrategyDaoImpl implements IAlarmStrategyDao {

	private static IAlarmStrategyDao dao = new AlarmStrategyDaoImpl();

	private AlarmStrategyDaoImpl() {

	}

	public static IAlarmStrategyDao getInstance() {
		return dao;
	}

	@Override
	public void saveStrategy(final AlarmStrategyEntity alarmStrategy) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(alarmStrategy);
				return null;
			}
		});

	}

	@Override
	public AlarmStrategyEntity selectStrategy(Severity severity, String itemType)
			throws RuntimeException {
		return (AlarmStrategyEntity) HibernateTemplate.execute(new HibernateCallBack() {
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				AlarmStrategyEntity lifeStrategyEntity = (AlarmStrategyEntity) ses.createQuery(
						"from AlarmStrategyEntity a where a.severity=" + severity.ordinal() + " and a.itemType= ?")
						.setParameter(0, itemType).uniqueResult();

				/*
				 * AlarmStrategyEntity lifeStrategyEntity = (AlarmStrategyEntity) ses
				 * .createCriteria(AlarmStrategyEntity.class,
				 * "a").add(Restrictions.eq("a.severity", severity)) .uniqueResult();
				 */
				return lifeStrategyEntity;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public StrategyPaging selectAll() throws RuntimeException {
		return (StrategyPaging) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				StrategyPaging strategies = new StrategyPaging();
				strategies.setStrategies(ses.createQuery("from AlarmStrategyEntity").list());
				strategies.setCount(ses.createQuery("from AlarmStrategyEntity").list().size());
				return strategies;
			}
		});
	}

	@Deprecated
	@Override
	public void updateEnable(String id) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.createQuery("update AlarmLifeStrategyEntity a set a.enabled=0 where a.enabled=1").executeUpdate();

				ses.createQuery("update AlarmLifeStrategyEntity a set a.enabled=1 where a.id=?").setParameter(0, id)
						.executeUpdate();

				return null;
			}
		});
	}

	@Override
	public void delStrategies(List<String> ids) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				for (String id : ids) {
					ses.delete(ses.load(AlarmStrategyEntity.class, id));
				}
				return null;
			}
		});
	}

	@Override
	public AlarmStrategyEntity selectById(String id) throws RuntimeException {
		return (AlarmStrategyEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return (AlarmStrategyEntity) ses.createQuery("from AlarmStrategyEntity a where a.id=?")
						.setParameter(0, id).uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlarmStrategyEntity> selectBatchStrategies(List<String> ids) throws RuntimeException {
		return (List<AlarmStrategyEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				List<AlarmStrategyEntity> list = new ArrayList<AlarmStrategyEntity>();
				for (String id : ids) {
					list.add((AlarmStrategyEntity) ses.createQuery("from AlarmStrategyEntity a where a.id=?")
							.setParameter(0, id).uniqueResult());
				}
				return list;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public StrategyPaging selectByCurPage(int curItems, int item, StrategyQueryDto query) throws RuntimeException {
		return (StrategyPaging) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				StrategyPaging strategypaging = new StrategyPaging();
				StringBuffer hql = new StringBuffer("from AlarmStrategyEntity a where 1=1");
				if (!StringUtils.isEmpty(query.getId())) {
					hql.append(" and a.id = ").append("'").append(query.getId()).append("'");
				}
				if (!StringUtils.isEmpty(query.getTag())) {
					hql.append(" and a.tag like ").append("'%").append(query.getTag()).append("%'");
				}
				if (!StringUtils.isEmpty(query.getSeverity())) {
					hql.append(" and a.severity = ").append(query.getSeverity().ordinal());
				}
				if (!StringUtils.isEmpty(query.getItemType())) {
					hql.append(" and a.itemType = ").append("'").append(query.getItemType()).append("'");
				}
				strategypaging.setCount((long)ses.createQuery("select count(*) " + hql.toString()).uniqueResult());
				strategypaging.setStrategies(ses.createQuery(hql.append("order by a.severity").toString())
						.setFirstResult(curItems).setMaxResults(item).list());
				return strategypaging;
			}
		});
	}

	@Override
	public int selectCountsByType(String ItemType) throws RuntimeException {
		return (int) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from AlarmStrategyEntity a where a.itemType = ?0").setParameter("0", ItemType)
						.list().size();
			}
		});
	}

	@Override
	public int countByGroupId(short groupId) throws RuntimeException {
		return (int) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.createQuery("select count(*) from AlarmStrategyEntity a join a.groupIds g where g.groupIds=?")
						.setParameter(0, groupId).list();
				return null;
			}
		});
	}

}
