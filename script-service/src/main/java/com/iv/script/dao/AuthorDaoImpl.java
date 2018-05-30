package com.iv.script.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.script.entity.AuthorEntity;

@Repository
public class AuthorDaoImpl implements IAuthorDao {

	@Override
	public void save(AuthorEntity entity) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return null;
			}
		});

	}

	@Override
	public AuthorEntity selectById(int userId) throws RuntimeException {
		
		return (AuthorEntity) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(AuthorEntity.class, userId);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuthorEntity> selectByRealName(String name) throws RuntimeException {
		
		return (List<AuthorEntity>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from AuthorEntity a where a.realName=?").setParameter(0, name).list();
			}
		});
	}

}
