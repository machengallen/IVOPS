package com.iv.permission.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.Constants;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.permission.dao.SubTenantPermissionDao;
import com.iv.permission.entity.SubTenantPermission;

@Repository
public class SubTenantPermissionDaoImpl implements SubTenantPermissionDao {

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 根据ids查询项目组权限列表
	 */
	public List<SubTenantPermission> selectSubTenantPermissionByIds(Set<Integer> ids) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<SubTenantPermission>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from SubTenantPermission sr where sr.groupPermissionId in :ids")
						.setParameterList("ids", ids).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 查询项目组获取的所有权限code
	 */
	public List<Integer> selectAllSubTenantPermission() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Integer>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select sp.groupPermissionId from SubTenantPermission sp").list();
			}
		});
	}

	/**
	 * 查询项目组获取的所有权限code
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> selectAllSubTenantPermissionByTenId(String tenantId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Integer>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select sp.groupPermissionId from SubTenantPermission sp").list();
			}
		}, ConstantContainer.PERMISSION_SERVICE + "_" + tenantId);
	}

	/**
	 * 保存项目组拥有的权限列表
	 */
	@Override
	public void saveOrUpdate(List<SubTenantPermission> permissions, String tenantId) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				for (SubTenantPermission subTenantPermission : permissions) {
					ses.saveOrUpdate(subTenantPermission);
				}
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE + "_" + tenantId);
	}
	
}
