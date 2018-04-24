package com.iv.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.util.hibernate.HibernateCallBack;
import com.iv.common.util.hibernate.HibernateTemplate;
import com.iv.common.util.hibernate.HibernateTemplateWithTenant;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.dao.UserOauthDao;
import com.iv.entity.UserOauth;
import com.iv.enumeration.LoginType;

@Repository
public class UserOauthDaoImpl implements UserOauthDao {

	@Override
	public UserOauth selectUserOauthByUnionid(String unionid, LoginType loginType) throws RuntimeException {
		// TODO Auto-generated method stub
		return (UserOauth) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from UserOauth u where u.unionid=? and u.loginType=?")
						.setParameter(0, unionid).setParameter(1, loginType).uniqueResult();
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public String selectUserWechatUnionid(int userId, String loginType) throws RuntimeException {
		// TODO Auto-generated method stub
		return (String) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from UserOauth u where u.userId=? and u.loginType=?")
						.setParameter(0, userId).setParameter(1, loginType).uniqueResult();
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 根据用户列表id、登录方式查找三方信息
	 */
	public List<UserOauth> selectUsersWechatUnionid(List<Integer> userIds, String loginType) {
		// TODO Auto-generated method stub
		return (List<UserOauth>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from UserOauth u where u.loginType=? and u.userId in ?")
						.setParameter(0, loginType).setParameter(0, userIds).list();
			}
		});
	}
}
