package com.iv.operation.script.dao.impl;

import java.util.ArrayList;
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
import com.iv.operation.script.dto.SingleTaskScheduleDto;
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

	@SuppressWarnings("unchecked")
	@Override
	public ObjectPageDto<SingleTaskScheduleDto> selectPage(ScheduleQueryDto queryDto) throws RuntimeException {
		return (ObjectPageDto<SingleTaskScheduleDto>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ObjectPageDto<SingleTaskScheduleDto> dto = new ObjectPageDto<SingleTaskScheduleDto>();
				if (null != queryDto.getId()) {
					dto.setTotal(1);
					SingleTaskScheduleEntity scheduleEntity = ses.get(SingleTaskScheduleEntity.class, queryDto.getId());
					dto.setData(Arrays.asList(entity2Dto(scheduleEntity, ses)));
					return dto;
				}
				int page = queryDto.getCurPage();
				int items = queryDto.getItems();
				String hql = "from SingleTaskScheduleEntity s where 1=1";
				if (!StringUtils.isEmpty(queryDto.getTaskName())) {
					hql = hql + " and s.singleTask.taskName like '%" + queryDto.getTaskName() + "%'";
				}
				dto.setTotal((long) ses.createQuery("select count(*) " + hql).uniqueResult());
				List<SingleTaskScheduleEntity> scheduleEntities = ses.createQuery(hql).setFirstResult((page - 1) * items).setMaxResults(items).list();
				List<SingleTaskScheduleDto> scheduleDtos = new ArrayList<SingleTaskScheduleDto>(scheduleEntities.size());
				for (SingleTaskScheduleEntity scheduleEntity : scheduleEntities) {
					scheduleDtos.add(entity2Dto(scheduleEntity, ses));
				}
				dto.setData(scheduleDtos);
				return dto;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private SingleTaskScheduleDto entity2Dto(SingleTaskScheduleEntity entity, Session ses) {
		SingleTaskScheduleDto dto = new SingleTaskScheduleDto();
		dto.setId(entity.getId());
		dto.setCronExp(entity.getCronExp());
		dto.setCreator(entity.getCreator());
		dto.setCreaDate(entity.getCreaDate());
		dto.setModifier(entity.getModifier());
		dto.setModDate(entity.getModDate());
		dto.setName(entity.getName());
		dto.setTaskId(entity.getSingleTask().getId());
		dto.setTaskName(entity.getSingleTask().getTaskName());
		dto.setResults(ses.createQuery("from ScheduleTargetEntity s where s.taskSchedule.id=?").setParameter(0, entity.getId())
				.list());
		return dto;
		
	}

}
