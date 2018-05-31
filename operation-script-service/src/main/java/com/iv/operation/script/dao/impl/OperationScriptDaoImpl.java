package com.iv.operation.script.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.operation.script.entity.OperationScriptEntity;

@Repository
public class OperationScriptDaoImpl implements IOperationScriptDao {

	@Override
	public void save(OperationScriptEntity entity) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(entity);
				return null;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationScriptEntity> selectById(int id) throws RuntimeException {
		return (List<OperationScriptEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(OperationScriptEntity.class, id);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationScriptEntity> selectPage(int page, int items) throws RuntimeException {
		return (List<OperationScriptEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from ScriptOperationEntity").setFirstResult((page - 1) * items)
						.setMaxResults(items).list();
			}
		});
	}

}
