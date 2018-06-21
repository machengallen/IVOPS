package com.iv.permission.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.permission.dao.GlobalRoleDao;
import com.iv.permission.entity.GlobalRole;
@Repository
public class GlobalRoleDaoImpl implements GlobalRoleDao {

	@Override
	/**
	 * 角色新增（基础库）
	 */
	public void saveOrUpdate(GlobalRole globalRole) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(globalRole);
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 根据角色id查询角色信息
	 */
	public GlobalRole selectById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		return (GlobalRole) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(GlobalRole.class, id);
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 角色删除（基础库）
	 */
	public void deleteGlobalRoleById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.delete(ses.load(GlobalRole.class, id));
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 根据ids查询角色信息
	 */
	public List<GlobalRole> selectGlobalRoleByIds(Set<Integer> ids) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<GlobalRole>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from GlobalRole g where g.id in :ids")
						.setParameterList("ids", ids).list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 批量删除角色
	 */
	public void deleteGlobalRoleByIds(Set<Integer> ids) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				for (Integer id : ids) {
					ses.delete(ses.load(GlobalRole.class, id));
				}
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 获取角色列表
	 */
	public List<GlobalRole> selectGlobalRoles() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<GlobalRole>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from GlobalRole g order by g.createDate desc").list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

}
