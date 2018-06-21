package com.iv.permission.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.permission.dao.LocalAuthRoleDao;
import com.iv.permission.entity.LocalAuthRole;

@Repository
public class LocalAuthRoleDaoImpl implements LocalAuthRoleDao{

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 获取具有某个权限的人员信息列表（基础库）
	 */
	public List<Integer> getUserIdsByCode(String code) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Integer>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select distinct lar.userId from LocalAuthRole lar join lar.globalRoles gr join gr.permissionInfos p where p.code=?")
						.setParameter(0, code).list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	public LocalAuthRole selectLocalAuthRoleById(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuthRole) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(LocalAuthRole.class, userId);
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 保存用户角色
	 */
	public void saveOrUpdate(LocalAuthRole localAuthRole) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(localAuthRole);
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

}
