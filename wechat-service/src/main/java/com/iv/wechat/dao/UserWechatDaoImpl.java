package com.iv.wechat.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.util.hibernate.HibernateCallBack;
import com.iv.common.util.hibernate.HibernateTemplateWithTenant;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.wechat.dao.UserWechatDao;
import com.iv.wechat.entity.UserWechatEntity;

@Repository
public class UserWechatDaoImpl implements UserWechatDao {

	@Override
	public void saveOrUpdateUserWechat(UserWechatEntity userWechatEntity) {
		// TODO Auto-generated method stub
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(userWechatEntity);
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public UserWechatEntity selectUserWechatByUnionid(String unionid) throws RuntimeException {
		// TODO Auto-generated method stub
		return (UserWechatEntity) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(UserWechatEntity.class, unionid);
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

}
