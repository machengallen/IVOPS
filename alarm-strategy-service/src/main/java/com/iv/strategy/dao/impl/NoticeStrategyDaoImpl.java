package com.iv.strategy.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.strategy.dao.INoticeStrategyDao;
import com.iv.strategy.entity.NoticeStrategyEntity;

@Repository
public class NoticeStrategyDaoImpl implements INoticeStrategyDao {

	@Override
	public NoticeStrategyEntity selectByUserId(int userId) throws RuntimeException {
		
		return (NoticeStrategyEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				NoticeStrategyEntity entity = ses.get(NoticeStrategyEntity.class, userId);
				if(null == entity) {
					entity = new NoticeStrategyEntity();
					entity.setUserId(userId);
					entity.setEmailNotice(Boolean.TRUE);
					entity.setWechatNotice(Boolean.TRUE);
					ses.save(entity);
				}
				return entity;
			}
		}, ConstantContainer.ALARM_STRATEGY_DB);
	}

	@Override
	public NoticeStrategyEntity save(NoticeStrategyEntity entity) throws RuntimeException {
		
		return (NoticeStrategyEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return entity;
			}
		}, ConstantContainer.ALARM_STRATEGY_DB);
	}

}
