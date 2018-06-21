package com.iv.permission.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.enumeration.ServiceType;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.permission.dao.FunctionInfoDao;
import com.iv.permission.entity.FunctionInfo;
import com.iv.permission.entity.PermissionInfo;

@Repository
public class FunctionInfoDaoImpl implements FunctionInfoDao{

	@Override
	/**
	 * 角色创建
	 */
	public void saveFunction(FunctionInfo functionInfo) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(functionInfo);
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 根据id集合查询功能集合
	 */
	public List<FunctionInfo> selectFunctionByIds(Set<Integer> ids) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<FunctionInfo>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from FunctionInfo f where f.id in :ids")
						.setParameterList("ids", ids).list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 查询所有功能
	 */
	public List<FunctionInfo> selectAllFunction() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<FunctionInfo>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from FunctionInfo f order by f.createDate desc").list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 批量删除权限
	 */
	public void deleteFunctions(Set<Integer> ids) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				for (Integer id : ids) {
					ses.delete(ses.load(FunctionInfo.class, id));
				}
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 根据id查询权限
	 */
	public FunctionInfo selectFunctionById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		return (FunctionInfo) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(FunctionInfo.class, id);
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	/**
	 * 根据功能所属服务类别查询功能列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionInfo> selectFunctionByServiceType(List<ServiceType> services) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<FunctionInfo>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from FunctionInfo f where f.serviceType in :services")
						.setParameterList("services", services).list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	/**
	 * 查询项目组包含的所有权限
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> selectAllSubFunction() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Integer>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select f.id from FunctionInfo f where f.serviceType!=? order by f.createDate desc")
						.setParameter(0, ServiceType.OPERATE).list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	/**
	 * 查询含有某个api的权限id列表（拥有审批权限人员查询）
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> selectApproveFuncIds(String code) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Integer>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select f.id from FunctionInfo f join f.permissionInfos p where p.code=?")
						.setParameter(0, code).list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

}
