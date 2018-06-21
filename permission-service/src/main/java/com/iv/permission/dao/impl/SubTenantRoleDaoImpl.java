package com.iv.permission.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.permission.dao.SubTenantRoleDao;
import com.iv.permission.entity.SubTenantRole;

@Repository
public class SubTenantRoleDaoImpl implements SubTenantRoleDao {

	@Override
	/**
	 * 保存或者更新项目组角色
	 */
	public void saveOrUpdateSubTenantRole(SubTenantRole subTenantRole) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(subTenantRole);
				return null;
			}
		});
	}

	@Override
	/**
	 * 保存或者更新项目组角色(指定DB)
	 */
	public void saveOrUpdateSubTenantRoleBytenantId(SubTenantRole subTenantRole, String tenantId)
			throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(subTenantRole);
				return null;
			}
		}, tenantId);
	}

	@Override
	/**
	 * 删除角色列表
	 */
	public void deleteSubTenantRole(Set<Integer> ids) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				for (Integer id : ids) {
					ses.delete(ses.load(SubTenantRole.class, id));
				}				
				return null;
			}
		});
	}

	@Override
	/**
	 * 根据id查询项目组角色
	 */
	public SubTenantRole selectSubTenantRoleById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		return (SubTenantRole) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(SubTenantRole.class, id);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 查询项目组下角色列表
	 */
	public List<SubTenantRole> selectAllSubTenantRole() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<SubTenantRole>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from SubTenantRole s order by s.createDate desc").list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 根据ids查询角色列表
	 */
	public List<SubTenantRole> selectSubTenantRoleByIds(Set<Integer> ids) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<SubTenantRole>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from SubTenantRole s where s.id in :ids")
						.setParameterList("ids", ids).list();
			}
		});
	}
	
	/**
	 * 根据权限ids查询角色id
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> selectSubTenantRoleIds(Set<Integer> Permissionids) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Integer>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select s.id from SubTenantRole s join s.subTenantPermissions sp where sp.groupPermissionId in :ids")
						.setParameterList("ids", Permissionids).list();
			}
		});
	}
	

}
