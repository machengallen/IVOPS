package com.iv.permission.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.permission.dao.PermissionDao;
import com.iv.permission.dto.PermissionPageDto;
import com.iv.permission.entity.PermissionInfo;
@Repository
public class PermissionDaoImpl implements PermissionDao {

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 获取全部基础库权限列表
	 */
	public PermissionPageDto getPermissionPageList(int curItems, int item) {
		// TODO Auto-generated method stub
		return (PermissionPageDto) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				PermissionPageDto pageDto = new PermissionPageDto();
				pageDto.setPermissions(ses.createQuery("from PermissionInfo p")
						.setFirstResult(curItems).setMaxResults(item).list());
				pageDto.setTotalCount((long)ses.createQuery("select count(1) from PermissionInfo").uniqueResult());
				return pageDto;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 根据codes获取权限列表
	 */
	public List<PermissionInfo> getPermissionsByIds(List<String> codes) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<PermissionInfo>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from PermissionInfo p where p.code in :codes")
						.setParameterList("codes", codes).list();
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 获取基础库分页权限列表
	 */
	public List<PermissionInfo> getPermissionList() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<PermissionInfo>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from PermissionInfo p").list();						
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 权限api新增
	 */
	public void saveOrUpdatePermission(PermissionInfo permissionInfo) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(permissionInfo);
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 删除权限
	 */
	public void deletePermissionByIds(Set<String> codes) throws RuntimeException {
		// TODO Auto-generated method stub		
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				for (String code : codes) {
					ses.delete(ses.load(PermissionInfo.class, code));
				}
				return null;
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}

	@Override
	/**
	 * 根据id获取权限
	 */
	public PermissionInfo getPermissionsById(String code) throws RuntimeException {
		// TODO Auto-generated method stub
		return (PermissionInfo) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(PermissionInfo.class, code);
			}
		}, ConstantContainer.PERMISSION_SERVICE);
	}


}
