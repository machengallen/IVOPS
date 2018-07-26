package com.iv.message.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.dto.ObjectPageDto;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.message.dao.IMsgDao;
import com.iv.message.entity.MsgEntity;

@Repository
public class MsgDaoImpl implements IMsgDao {

	@Override
	public MsgEntity save(MsgEntity entity) throws RuntimeException {

		return (MsgEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return entity;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectPageDto<MsgEntity> selectPage(int userId, int page, int items) throws RuntimeException {

		return (ObjectPageDto<MsgEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {

				return ses.createQuery("from MsgEntity m where m.userId=? order by m.msgDate desc")
						.setParameter(0, userId).setFirstResult((page - 1) * items).setMaxResults(items).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectPageDto<MsgEntity> selectPageByConfirm(int userId, int page, int items, boolean confirmed)
			throws RuntimeException {

		return (ObjectPageDto<MsgEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {

				ObjectPageDto<MsgEntity> pageDto = new ObjectPageDto<>();
				pageDto.setData(
						ses.createQuery("from MsgEntity m where m.userId=? and m.confirmed=? order by m.msgDate desc")
								.setParameter(0, userId).setParameter(1, confirmed).setFirstResult((page - 1) * items)
								.setMaxResults(items).list());
				return pageDto;
			}
		});
	}

	@Override
	public int updateConfirmed(List<String> ids, boolean confirmed) throws RuntimeException {
		return (int) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("update MsgEntity m set m.confirmed=? where m.id in (:ids)")
						.setParameter(0, confirmed).setParameterList("ids", ids).executeUpdate();
			}
		});
	}

	@Override
	public int updateConfirmedAllByUserId(int userId, boolean confirmed) throws RuntimeException {
		return (int) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("update MsgEntity m set m.confirmed=? where m.userId=?")
						.setParameter(0, confirmed).setParameter(1, userId).executeUpdate();
			}
		});
	}

	@Override
	public int updateConfirmedPageByUserId(int userId, int page, int items, boolean confirmed) throws RuntimeException {
		return (int) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("update MsgEntity m set m.confirmed=? where m.userId=? order by m.msgDate desc")
						.setParameter(0, confirmed).setParameter(1, userId).setFirstResult((page - 1) * items)
						.setMaxResults(items).executeUpdate();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectPageDto<MsgEntity> selectPageByTypeAndConfirm(int userId, int page, int items, String type,
			int confirmeType) throws RuntimeException {

		return (ObjectPageDto<MsgEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ObjectPageDto<MsgEntity> pageDto = new ObjectPageDto<MsgEntity>();
				Boolean confirmed = null;
				if (0 == confirmeType) {
					confirmed = false;
				} else if (1 == confirmeType) {
					confirmed = true;
				}
				List<MsgEntity> data = null;
				long count = 0;
				if (confirmed != null) {
					data = ses.createQuery("from MsgEntity m where m.userId=? and m.confirmed=?")
							.setParameter(0, userId).setParameter(1, confirmed).setFirstResult((page - 1) * items)
							.setMaxResults(items).list();
					count = (long) ses.createQuery("count(*) from MsgEntity m where m.userId=? and m.confirmed=?")
							.setParameter(0, userId).setParameter(1, confirmed).uniqueResult();

				} else {
					data = ses.createQuery("from MsgEntity m where m.userId=?").setParameter(0, userId)
							.setFirstResult((page - 1) * items).setMaxResults(items).list();
					count = (long) ses.createQuery("count(*) from MsgEntity m where m.userId=?").setParameter(0, userId)
							.uniqueResult();
				}
				pageDto.setData(data);
				pageDto.setTotal(count);
				return pageDto;
			}
		});
	}

	@Override
	public int deleteByIds(List<String> ids) throws RuntimeException {

		return (int) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {

				return ses.createQuery("delete from MsgEntity m where m.id in (:ids)").setParameterList("ids", ids)
						.executeUpdate();
			}
		});
	}

}
