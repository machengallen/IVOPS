package com.iv.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.CollectionUtils;

import com.iv.common.hibernate.util.HibernateCallBack;
import com.iv.common.hibernate.util.HibernateTemplateWithTenant;
import com.iv.common.spring.util.ConstantContainer;
import com.iv.entity.UserWechatEntity;


public class IUserDaoImpl implements IUserDao {

	private static final IUserDaoImpl dao = new IUserDaoImpl();

	private IUserDaoImpl() {

	}

	public static IUserDaoImpl getInstance() {

		return dao;
	}

	@Override
	public void saveUsersInfo(final List<UserWechatEntity> usersInfo) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				for (int i = 1; i <= usersInfo.size(); i++) {
					ses.saveOrUpdate(usersInfo.get(i - 1));
					if (i % 50 == 0) {
						ses.flush();
						ses.clear();
					}
				}
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public void saveUserInfo(final UserWechatEntity userInfo) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(userInfo);
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserWechatEntity> selectAllUserWechatInfo() throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<UserWechatEntity>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				List<UserWechatEntity> list = ses.createCriteria(UserWechatEntity.class).list();
				return list;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public void deleteUserInfoById(final String openId) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.delete(ses.load(UserWechatEntity.class, openId));
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);

	}

	@Override
	public UserWechatEntity selectUserWechatById(final String openId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (UserWechatEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(UserWechatEntity.class, openId);
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserWechatEntity> selectBatchUserWechatInfo(final List<String> openIds) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<UserWechatEntity>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				Set<Criterion> res = new HashSet<Criterion>();
				if(CollectionUtils.isEmpty(openIds)){
					return null;
				}
				for (String string : openIds) {
					res.add(Restrictions.eq("u.openid", string));
				}
				
				return ses.createCriteria(UserWechatEntity.class, "u")
						.add(Restrictions.or(res.toArray(new Criterion[res.size()]))).list();
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

}
