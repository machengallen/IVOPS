package com.iv.tenant.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.entity.EnterpriseEntity;

@Repository
public class EnterpriseDaoImpl implements IEnterpriseDao {

	@Override
	public EnterpriseEntity save(EnterpriseEntity entity) throws RuntimeException {
		return (EnterpriseEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return entity;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnterpriseEntity> selectAll() throws RuntimeException {
		return (List<EnterpriseEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from EnterpriseEntity").list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnterpriseEntity> selectByCondition(QueryEnterReq req) throws RuntimeException {
		return (List<EnterpriseEntity>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				StringBuilder builder = new StringBuilder("from EnterpriseEntity e where 1=1");
				boolean flag = false;
				if(!StringUtils.isEmpty(req.getName())) {
					builder.append(" and e.name like '%" + req.getName() + "%'");
					flag = true;
				}
				if(!StringUtils.isEmpty(req.getIdentifier())) {
					builder.append(" and e.identifier like '%" + req.getIdentifier() + "%'");
					flag = true;
				}
				if(!StringUtils.isEmpty(req.getAddress())) {
					builder.append(" and e.address like '%" + req.getAddress() + "%'");
					flag = true;
				}
				if(!StringUtils.isEmpty(req.getIndustry())) {
					builder.append(" and e.industry like '%" + req.getIndustry() + "%'");
					flag = true;
				}
				if(!flag) {
					return null;
				}
				return ses.createQuery(builder.toString()).list();
			}
		});
	}

	@Override
	public long countAll() throws RuntimeException {
		return (long) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select count(*) from EnterpriseEntity").uniqueResult();
			}
		});
	}

	@Override
	public EnterpriseEntity selectByOrgCode(String orgCode) throws RuntimeException {
		return (EnterpriseEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from EnterpriseEntity e where e.orgCode=?").setParameter(0, orgCode)
						.uniqueResult();
			}
		});
	}

	@Override
	public EnterpriseEntity selectByName(String name) throws RuntimeException {
		return (EnterpriseEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from EnterpriseEntity e where e.name=?").setParameter(0, name)
						.uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnterpriseEntity> selectByUserId(int userId) throws RuntimeException {
		return (List<EnterpriseEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from EnterpriseEntity e where ? in e.userIds").setParameter(0, userId)
						.list();
			}
		});
	}

}
