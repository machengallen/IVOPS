package com.iv.aggregation.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.aggregation.dao.IAlarmCleanStrategyDao;
import com.iv.aggregation.entity.AlarmCleanStrategyEntity;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;

@Repository
public class AlarmCleanStrategyDaoImpl implements IAlarmCleanStrategyDao {

	@Override
	public void saveStrategy(AlarmCleanStrategyEntity entity) throws RuntimeException {

		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.save(entity);
				return null;
			}
		});
	}
	
	@Override
	public AlarmCleanStrategyEntity saveStrategy(AlarmCleanStrategyEntity entity, String tenantId) throws RuntimeException {
		
		return (AlarmCleanStrategyEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.save(entity);
				return entity;
			}
		}, tenantId);
	}

	@Override
	public AlarmCleanStrategyEntity selectById(int id) throws RuntimeException {
		
		return (AlarmCleanStrategyEntity) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(AlarmCleanStrategyEntity.class, id);
			}
		});
	}
	
	@Override
	public AlarmCleanStrategyEntity selectById(int id, String tenantId) throws RuntimeException {
		
		return (AlarmCleanStrategyEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(AlarmCleanStrategyEntity.class, id);
			}
		}, tenantId);
	}

}
