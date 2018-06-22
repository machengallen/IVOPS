package com.iv.operation.script.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.operation.script.entity.ImmediateTargetEntity;

@Repository
public class ImmediateTargetDaoImpl implements IImmediateTargetDao {

	@Override
	public void batchSave(List<ImmediateTargetEntity> entities) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				for (ImmediateTargetEntity entity : entities) {
					ses.saveOrUpdate(entity);
				}
				return null;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ImmediateTargetEntity> selectByTaskId(int taskId) throws RuntimeException {
		return (List<ImmediateTargetEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {

				return ses.createQuery("from ImmediateTargetEntity i where i.task.id=?").setParameter(0, taskId)
						.list();
			}
		});
	}

	@Override
	public void delByTaskId(int taskId) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.createQuery("delete from ImmediateTargetEntity i where i.task.id=?").setParameter(0, taskId)
						.executeUpdate();
				return null;
			}
		});

	}

}
