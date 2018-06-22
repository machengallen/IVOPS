package com.iv.operation.script.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.operation.script.entity.ScheduleTargetEntity;

@Repository
public class ScheduleTargetDaoImpl implements IScheduleTargetDao {

	@Override
	public void batchSave(List<ScheduleTargetEntity> entities) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				for (ScheduleTargetEntity entity : entities) {
					ses.saveOrUpdate(entity);
				}
				return null;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleTargetEntity> selectByScheduleId(int scheduleId) throws RuntimeException {
		return (List<ScheduleTargetEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {

				return ses.createQuery("from ScheduleTargetEntity s where s.taskSchedule.id=?").setParameter(0, scheduleId)
						.list();
			}
		});
	}

	@Override
	public void delByScheduleId(int scheduleId) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.createQuery("delete from ScheduleTargetEntity s where s.taskSchedule.id=?").setParameter(0, scheduleId)
						.executeUpdate();
				return null;
			}
		});

	}

}
