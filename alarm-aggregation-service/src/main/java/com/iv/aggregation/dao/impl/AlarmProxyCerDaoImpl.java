package com.iv.aggregation.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.aggregation.dao.IAlarmProxyCerDao;
import com.iv.aggregation.entity.AlarmProxyCerEntity;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;

@Repository
public class AlarmProxyCerDaoImpl implements IAlarmProxyCerDao {

	@Override
	public String save(AlarmProxyCerEntity entity) throws RuntimeException {
		return (String) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return entity.getToken();
			}
		}, ConstantContainer.ALARM_AGGREGATION_DB);
	}

	@Override
	public AlarmProxyCerEntity selectByToken(String token) throws RuntimeException {
		return (AlarmProxyCerEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
					
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(AlarmProxyCerEntity.class, token);
			}
		}, ConstantContainer.ALARM_AGGREGATION_DB);
	}

}
