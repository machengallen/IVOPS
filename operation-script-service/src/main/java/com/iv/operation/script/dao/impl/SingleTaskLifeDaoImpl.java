package com.iv.operation.script.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.operation.script.entity.SingleTaskLifeEntity;

@Repository
public class SingleTaskLifeDaoImpl implements ISingleTaskLifeDao {

	@Override
	public SingleTaskLifeEntity selectByTaskId(int taskId) throws RuntimeException {
		return (SingleTaskLifeEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SingleTaskLifeEntity s where s.singleTask.id=?").setParameter(0, taskId)
						.uniqueResult();
			}
		});
	}

	@Override
	public void save(SingleTaskLifeEntity entity) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return null;
			}
		});
		
	}

}
