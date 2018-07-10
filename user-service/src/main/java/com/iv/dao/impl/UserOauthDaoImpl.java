package com.iv.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.dao.UserOauthDao;
import com.iv.entity.UserOauth;
import com.iv.enumeration.LoginType;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;

@Repository
public class UserOauthDaoImpl implements UserOauthDao {

	@Override
	public UserOauth selectUserOauthByUnionid(String unionid, LoginType loginType) throws RuntimeException {
		// TODO Auto-generated method stub
		return (UserOauth) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from UserOauth u where u.unionid=? and u.loginType=?")
						.setParameter(0, unionid).setParameter(1, loginType).uniqueResult();
			}
		});
	}

	@Override
	public UserOauth selectUserWechatUnionid(int userId, LoginType loginType) throws RuntimeException {
		// TODO Auto-generated method stub
		return (UserOauth) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from UserOauth u where u.userId=? and u.loginType=?")
						.setParameter(0, userId).setParameter(1, loginType).uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 根据用户列表id、登录方式查找三方信息
	 */
	public List<UserOauth> selectUsersWechatUnionid(List<Integer> userIds, LoginType loginType) {
		// TODO Auto-generated method stub
		return (List<UserOauth>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {				
				// TODO Auto-generated method stub
				return ses.createQuery("from UserOauth u where u.loginType=? and u.userId in :ids")
						.setParameter(0, loginType).setParameterList("ids", userIds).list();
			}
		});
	}

	@Override
	/**
	 * 保存或更新三方绑定信息
	 */
	public UserOauth saveOrUpdateUserOauth(UserOauth userOauth) throws RuntimeException {
		// TODO Auto-generated method stub
		return (UserOauth) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(userOauth);
				return userOauth;
			}
		});
	}

	/**
	 * 解绑微信
	 */
	@Override
	public void deleteUserOauthById(String id) throws RuntimeException {
		// TODO Auto-generated method stub
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.delete(ses.load(UserOauth.class, id));
				return null;
			}
		});
	}
}
