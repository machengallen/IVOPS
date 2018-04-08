package com.iv.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.hibernate.util.HibernateCallBack;
import com.iv.common.hibernate.util.HibernateTemplateWithTenant;
import com.iv.common.spring.util.ConstantContainer;
import com.iv.dao.UserOauthDao;
import com.iv.entity.UserOauth;

@Repository
public class UserOauthDaoImpl implements UserOauthDao {

	@Override
	public UserOauth selectUserOauthByUnionid(String unionid, String loginType) throws RuntimeException {
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
}
