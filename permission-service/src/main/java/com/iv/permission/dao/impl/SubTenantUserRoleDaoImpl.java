package com.iv.permission.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.Constants;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.permission.dao.SubTenantUserRoleDao;
import com.iv.permission.entity.FunctionInfo;
import com.iv.permission.entity.SubTenantRole;
import com.iv.permission.entity.SubTenantUserRole;

@Repository
public class SubTenantUserRoleDaoImpl implements SubTenantUserRoleDao {

	@Override
	/**
	 * 保存项目组用户角色
	 */
	public void save(SubTenantUserRole subTenantUserRole) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(subTenantUserRole);
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 项目下具有审核权限的人员id列表
	 */
	public List<Integer> getUserIdsByFuncIds(String tenantId, List<Integer> funcIds) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Integer>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select distinct aur.userId from SubTenantUserRole aur left join aur.subTenantRoles str left join str.subTenantPermissions p where p.groupPermissionId in :funcIds")
						.setParameterList("funcIds", funcIds).list();	
				
			}
		}, ConstantContainer.PERMISSION_SERVICE + "_" + tenantId);
	}

	@Override
	/**
	 * 指定DB保存
	 */
	public void saveByTenantId(SubTenantUserRole subTenantUserRole, String tenantId) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(subTenantUserRole);
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE + "_" + tenantId);
	}

	@Override
	/**
	 * 根据userId查询用户角色
	 */
	public SubTenantUserRole selectUserRoleByUserId(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (SubTenantUserRole) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(SubTenantUserRole.class, userId);
			}
		});
	}

	/**
	 * 根据userId/租户标志查询用户角色（人员列表展示）
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubTenantRole> selectUserRoleByTenantId(int userId, String tenantId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<SubTenantRole>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select sr.subTenantRoles from SubTenantUserRole sr where sr.userId=?")
						.setParameter(0, userId).list();
			}
		}, ConstantContainer.PERMISSION_SERVICE + "_" + tenantId);
	}

}
