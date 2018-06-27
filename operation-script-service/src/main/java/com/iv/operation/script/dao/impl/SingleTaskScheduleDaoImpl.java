package com.iv.operation.script.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.iv.common.dto.ObjectPageDto;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.operation.script.dto.ScheduleQueryDto;
import com.iv.operation.script.entity.SingleTaskScheduleEntity;

@Repository
public class SingleTaskScheduleDaoImpl implements ISingleTaskScheduleDao {

	@Override
	public SingleTaskScheduleEntity save(SingleTaskScheduleEntity entity) throws RuntimeException {
		return (SingleTaskScheduleEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return entity;
			}
		});
	}

	@Override
	public SingleTaskScheduleEntity selectById(int id) throws RuntimeException {
		return (SingleTaskScheduleEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(SingleTaskScheduleEntity.class, id);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SingleTaskScheduleEntity> selectByTaskId(int taskId) throws RuntimeException {
		return (List<SingleTaskScheduleEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SingleTaskScheduleEntity s where s.singleTask.id=?")
						.setParameter(0, taskId).list();
			}
		});
	}

	@Override
	public void delById(int id) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.delete(ses.load(SingleTaskScheduleEntity.class, id));
				return null;
			}
		});

	}

	@Override
	public int delByTaskId(int taskId) throws RuntimeException {

		return (int) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("delete from SingleTaskScheduleEntity s where s.singleTask.id=?")
						.setParameter(0, taskId).executeUpdate();
			}
		});
	}

	@Override
	public ObjectPageDto selectPage(ScheduleQueryDto queryDto) throws RuntimeException {
		return (ObjectPageDto) HibernateTemplate.execute(new HibernateCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ObjectPageDto dto = new ObjectPageDto();
				if (null != queryDto.getId()) {
					dto.setTotal(1);
					dto.setData(Arrays.asList(ses.get(SingleTaskScheduleEntity.class, queryDto.getId())));
				}
				int page = queryDto.getCurPage();
				int items = queryDto.getItems();
				String hql = "from SingleTaskScheduleEntity s where 1=1";
				if (!StringUtils.isEmpty(queryDto.getTaskName())) {
					hql = hql + " and s.singleTask.taskName like '%" + queryDto.getTaskName() + "%'";
				}
				dto.setTotal((long) ses.createQuery("select count(*) " + hql).uniqueResult());
				dto.setData(ses.createQuery(hql).setFirstResult((page - 1) * items).setMaxResults(items).list());
				return dto;
			}
		});
	}

}
