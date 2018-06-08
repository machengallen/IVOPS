package com.iv.operation.script.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.operation.script.entity.SingleTaskTargetEntity;

@Repository
public class SingleTaskTargetDaoImpl implements ISingleTaskTargetDao {

	@Override
	public void batchSave(List<SingleTaskTargetEntity> entities) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				for (SingleTaskTargetEntity entity : entities) {
					ses.saveOrUpdate(entity);
				}
				return null;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SingleTaskTargetEntity> selectBySingleTaskId(int taskId) throws RuntimeException {
		return (List<SingleTaskTargetEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {

				return ses.createQuery("from SingleTaskTargetEntity s where s.singleTask.id=?").setParameter(0, taskId)
						.list();
			}
		});
	}

	@Override
	public void delBySingleTaskId(int taskId) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.createQuery("delete from SingleTaskTargetEntity s where s.singleTask.id=?").setParameter(0, taskId)
						.executeUpdate();
				return null;
			}
		});

	}

}
