package com.iv.message.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.dto.ObjectPageDto;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.message.dao.IApplyFlowMsgDao;
import com.iv.message.entity.ApplyFlowMsgEntity;

@Repository
public class ApplyFlowMsgDaoImpl implements IApplyFlowMsgDao {

	@Override
	public void save(ApplyFlowMsgEntity entity) throws RuntimeException {
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);

	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectPageDto selectByUserId(int userId, int firstResult, int maxResults) throws RuntimeException {
		return (ObjectPageDto) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ObjectPageDto dto = new ObjectPageDto();
				String hql = "from ApplyFlowMsgEntity a where a.userId=? and a.confirmed=? order by a.msgDate desc";
				int num = -1; //查询所有数据
				if(num == maxResults) {
					List list = ses.createQuery(hql).setParameter(0, userId).setParameter(1, false).list();
					dto.setData(list);
					dto.setTotal((long)list.size());
					return dto;
				}
				
				dto.setData(ses.createQuery(hql).setParameter(0, userId).setParameter(1, false)
						.setFirstResult(firstResult).setMaxResults(maxResults).list());
				dto.setTotal((long) ses.createQuery("select count(*) " + hql).setParameter(0, userId)
						.setParameter(1, false).uniqueResult());

				return dto;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public void updateConfirmed(List<String> ids, boolean confirmed) throws RuntimeException {
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				int num = ses.createQuery("update ApplyFlowMsgEntity a set a.confirmed=? where a.id in (:idList)")
						.setParameter(0, confirmed).setParameterList("idList", ids).executeUpdate();
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);

	}

	@Override
	public void updateConfirmedAllByUserId(int userId, boolean confirmed) throws RuntimeException {
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				int num = ses.createQuery("update ApplyFlowMsgEntity a set a.confirmed=? where a.userId=?")
						.setParameter(0, confirmed).setParameter(1, userId).executeUpdate();
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);

	}

}
