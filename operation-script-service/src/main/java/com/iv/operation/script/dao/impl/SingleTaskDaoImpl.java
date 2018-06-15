package com.iv.operation.script.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.operation.script.dto.SingleTaskPageDto;
import com.iv.operation.script.dto.SingleTaskQueryDto;
import com.iv.operation.script.entity.SingleTaskEntity;
import com.iv.operation.script.entity.SingleTaskTargetEntity;

@Repository
public class SingleTaskDaoImpl implements ISingleTaskDao {

	@Override
	public void save(SingleTaskEntity entity) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return null;
			}
		});

	}

	@Override
	public SingleTaskEntity selectById(int id) throws RuntimeException {
		return (SingleTaskEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(SingleTaskEntity.class, id);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SingleTaskPageDto selectByCondition(SingleTaskQueryDto query) throws RuntimeException {
		return (SingleTaskPageDto) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				SingleTaskPageDto pageDto = new SingleTaskPageDto();
				if(null != query.getId()) {
					pageDto.setTotalCount(1);
					pageDto.setEntries(Arrays.asList(ses.get(SingleTaskEntity.class, query.getId())));
					return pageDto;
				}
				int page = query.getCurPage();
				int items = query.getItems();
				String hql = "from SingleTaskEntity s where 1=1";
				if(!StringUtils.isEmpty(query.getTaskName())) {
					hql = hql + " and s.taskName like '%" + query.getTaskName() + "%'";
				}
				pageDto.setTotalCount((long)ses.createQuery("select count(*) " + hql).uniqueResult());
				pageDto.setEntries(ses.createQuery(hql).setFirstResult((page-1)*items).setMaxResults(items).list());
				return pageDto;
			}
		});
	}

}
