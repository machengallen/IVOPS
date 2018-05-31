package com.iv.tenant.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.tenant.api.dto.UserListDto;
import com.iv.tenant.entity.EnterpriseEntity;
import com.iv.tenant.entity.SubEnterpriseEntity;

@Repository
public class SubEnterpriseDaoImpl implements ISubEnterpriseDao {

	@Override
	public void save(SubEnterpriseEntity subEnterpriseEntity) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(subEnterpriseEntity);
				return null;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubEnterpriseEntity> selectByEnterprise(EnterpriseEntity enterpriseEntity) throws RuntimeException {
		return (List<SubEnterpriseEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SubEnterpriseEntity s where s.enterprise=?")
						.setParameter(0, enterpriseEntity).list();
			}
		});
	}

	@Override
	public SubEnterpriseEntity countWithEnterprise(EnterpriseEntity enterpriseEntity) throws RuntimeException {
		return (SubEnterpriseEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SubEnterpriseEntity s where s.enterprise=? order by s.id desc")
						.setParameter(0, enterpriseEntity).setMaxResults(1).uniqueResult();
			}
		});
	}

	@Override
	public SubEnterpriseEntity selectByTenantId(String subTenant) throws RuntimeException {
		return (SubEnterpriseEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SubEnterpriseEntity s where s.tenantId=?").setParameter(0, subTenant)
						.uniqueResult();
			}
		});
	}

	@Override
	public SubEnterpriseEntity selectById(int id) throws RuntimeException {
		return (SubEnterpriseEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(SubEnterpriseEntity.class, id);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> selectAllTenantId() throws RuntimeException {
		return (List<String>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select s.tenantId from SubEnterpriseEntity s").list();
			}
		});
	}

	@Override
	public void delById(int id) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.delete(ses.load(SubEnterpriseEntity.class, id));
				return null;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubEnterpriseEntity> selectByName(String name) throws RuntimeException {
		return (List<SubEnterpriseEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SubEnterpriseEntity s where s.name like ?")
						.setParameter(0, "%" + name + "%").list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubEnterpriseEntity> selectAll() throws RuntimeException {
		return (List<SubEnterpriseEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SubEnterpriseEntity").list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubEnterpriseEntity> selectByUserId(int userId) throws RuntimeException {
		return (List<SubEnterpriseEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SubEnterpriseEntity s join s.userIds u where u=?").setParameter(0, userId)
						.list();
			}
		});
	}

	@Override
	public SubEnterpriseEntity selectByIdentifier(String identifier) throws RuntimeException {
		return (SubEnterpriseEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from SubEnterpriseEntity s where s.subIdentifier=?").setParameter(0, identifier)
						.uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserListDto selectUsersByTenantId(String tenantId, int page, int items) throws RuntimeException {

		return (UserListDto) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				UserListDto dto = new UserListDto();
				dto.setTotal(
						(long) ses.createQuery("select count(s.userIds) from SubEnterpriseEntity s where s.tenantId=?")
								.setParameter(0, tenantId).uniqueResult());
				dto.setUserIds(ses
						.createQuery("select u from SubEnterpriseEntity s join s.userIds u where s.tenantId=?")
						.setParameter(0, tenantId).setFirstResult((page - 1) * items).setMaxResults(items).list());
				return dto;
			}
		});
	}

}
