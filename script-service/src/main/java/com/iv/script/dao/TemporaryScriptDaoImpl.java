package com.iv.script.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.script.entity.ScriptEntity;
import com.iv.script.entity.TemporaryScriptEntity;

@Repository
public class TemporaryScriptDaoImpl implements TemporaryScriptDao {

	/**
	 * 查询临时库文件信息
	 */
	@Override
	public TemporaryScriptEntity selectById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		return (TemporaryScriptEntity) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(TemporaryScriptEntity.class, id);
			}
		});
	}

	/**
	 * 保存临时文件信息
	 */
	@Override
	public TemporaryScriptEntity saveOrUpdate(TemporaryScriptEntity temporaryScriptInfo) throws RuntimeException {
		// TODO Auto-generated method stub
		return (TemporaryScriptEntity) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(temporaryScriptInfo);
				return temporaryScriptInfo;
			}
		});
	}

	/**
	 * 根据id删除临时文件
	 */
	@Override
	public void delById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.delete(ses.load(TemporaryScriptEntity.class, id));
				return null;
			}
		});
	}

}
