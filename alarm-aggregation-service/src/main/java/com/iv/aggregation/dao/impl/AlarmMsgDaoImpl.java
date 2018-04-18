package com.iv.aggregation.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.aggregation.api.constant.NoticeType;
import com.iv.aggregation.dao.IAlarmMsgDao;
import com.iv.aggregation.entity.AlarmMsgEntity;
import com.iv.common.dto.ObjectPageDto;
import com.iv.common.util.hibernate.HibernateCallBack;
import com.iv.common.util.hibernate.HibernateTemplate;
import com.iv.common.util.hibernate.HibernateTemplateWithTenant;

@Repository
public class AlarmMsgDaoImpl implements IAlarmMsgDao {

	@Override
	public void save(AlarmMsgEntity entity, String tenantId) throws RuntimeException {
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return null;
			}
		}, tenantId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectPageDto selectAlarmByUserId(int userId, int firstResult, int maxResults) throws RuntimeException {
		return (ObjectPageDto) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ObjectPageDto dto = new ObjectPageDto();
				String hql = "from AlarmMsgEntity a where a.userId=? and a.type=? and a.confirmed=? order by a.msgDate desc";
				dto.setData(ses.createQuery(hql).setParameter(0, userId).setParameter(1, NoticeType.ALARM)
						.setParameter(2, false).setFirstResult(firstResult).setMaxResults(maxResults).list());
				dto.setTotal((long) ses.createQuery("select count(*) " + hql).setParameter(0, userId)
						.setParameter(1, NoticeType.ALARM).setParameter(2, false).uniqueResult());

				return dto;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectPageDto selectRecoveryByUserId(int userId, int firstResult, int maxResults) throws RuntimeException {
		return (ObjectPageDto) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ObjectPageDto dto = new ObjectPageDto();
				String hql = "from AlarmMsgEntity a where a.userId=? and a.type=? and a.confirmed=? order by a.msgDate desc";
				dto.setData(ses.createQuery(hql).setParameter(0, userId).setParameter(1, NoticeType.RECOVERY)
						.setParameter(2, false).setFirstResult(firstResult).setMaxResults(maxResults).list());
				dto.setTotal((long) ses.createQuery("select count(*) " + hql).setParameter(0, userId)
						.setParameter(1, NoticeType.RECOVERY).setParameter(2, false).uniqueResult());

				return dto;
			}
		});
	}

	@Override
	public ObjectPageDto selectAllByUserId(int userId, int firstResult, int maxResults) throws RuntimeException {
		return (ObjectPageDto) HibernateTemplate.execute(new HibernateCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ObjectPageDto dto = new ObjectPageDto();
				String hql = "from AlarmMsgEntity a where a.userId=? and a.confirmed=? order by a.msgDate desc";
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
		});
	}

	@Override
	public AlarmMsgEntity selectUnconfirmedMsgById(String alarmId, int userId, String tenantId)
			throws RuntimeException {
		return (AlarmMsgEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from AlarmMsgEntity a where a.userId=? and a.alarmId=? and a.confirmed='0'")
						.setParameter(0, userId).setParameter(1, alarmId).uniqueResult();
			}
		}, tenantId);
	}

	@Override
	public void updateConfirmed(List<String> ids, boolean confirmed) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.createQuery("update AlarmMsgEntity a set a.confirmed=? where a.id in (:idList)")
						.setParameter(0, confirmed).setParameterList("idList", ids).executeUpdate();
				return null;
			}
		});

	}

	@Override
	public void updateConfirmedAllByUserId(int userId, boolean confirmed, NoticeType type) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.createQuery("update AlarmMsgEntity a set a.confirmed=? where a.userId=? and a.type=?")
						.setParameter(0, confirmed).setParameter(1, userId).setParameter(2, type).executeUpdate();
				return null;
			}
		});

	}

	@Override
	public void updateConfirmedAllByUserId(int userId, boolean confirmed) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.createQuery("update AlarmMsgEntity a set a.confirmed=? where a.userId=?")
						.setParameter(0, confirmed).setParameter(1, userId).executeUpdate();
				return null;
			}
		});

	}

}
