package com.iv.dao.impl;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.iv.dao.ILocalAuthDao;
import com.iv.entity.LocalAuth;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;

@Repository
public class LocalAuthDaoImpl implements ILocalAuthDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalAuthDaoImpl.class);	

	@Override
	public LocalAuth selectLocalauthInfoByName(String localAuthName) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from LocalAuth l where l.userName=?")
						.setParameter(0, localAuthName).uniqueResult();
			}
		});
	}

	@Override
	public LocalAuth saveOrUpdateLocalAuth(LocalAuth localAuth) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(localAuth);
				return localAuth;
			}
		});
	}

	@Override
	public LocalAuth selectLocalAuthById(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(LocalAuth.class, userId);
			}
		});
	}

	@Override
	/**
	 * 根据用户名查询用户信息
	 */
	public LocalAuth selectLocalAuthByUserName(String userName) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createCriteria(LocalAuth.class, "la").add(Restrictions.eq("la.userName", userName))
						.uniqueResult();
			}
		});
	}

	@Override
	/**
	 * 根据用户名、或者邮箱、或者已验证手机查询用户信息
	 */
	public LocalAuth selectLocalAuthByUserBaseInfo(String userBaseInfo) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from LocalAuth l where l.userName=? or l.email=? or l.tel=?")
						.setParameter(0, userBaseInfo).setParameter(1, userBaseInfo).setParameter(2, userBaseInfo)
						.uniqueResult();
			}
		});
	}
}
