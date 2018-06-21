package com.iv.wechat.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.jpa.util.hibernate.HibernateTemplateWithTenant;
import com.iv.wechat.dao.UserWechatDao;
import com.iv.wechat.entity.UserWechatEntity;

@Repository
public class UserWechatDaoImpl implements UserWechatDao {

	@Override
	public void saveOrUpdateUserWechat(UserWechatEntity userWechatEntity) {
		// TODO Auto-generated method stub
		HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				ses.saveOrUpdate(userWechatEntity);
				return null;
			}
		});
	}

	@Override
	public UserWechatEntity selectUserWechatByUnionid(String unionid) throws RuntimeException {
		// TODO Auto-generated method stub
		return (UserWechatEntity) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.get(UserWechatEntity.class, unionid);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 根据unionids查询微信信息集
	 */
	public List<UserWechatEntity> selectUserWechatsByUnionids(Set<String> unionids) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<UserWechatEntity>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from UserWechatEntity u where u.unionid in :unionids")
						.setParameterList("unionids", unionids).list();
			}
		});
	}

	@Override
	/**
	 * 保存微信信息列表
	 */
	public void saveOrUpdateUserWechats(List<UserWechatEntity> userWechatEntitys) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				for (int i = 1; i <= userWechatEntitys.size(); i++) {
					ses.saveOrUpdate(userWechatEntitys.get(i - 1));
					if (i % 50 == 0) {
						ses.flush();
						ses.clear();
					}
				}
				return null;
			}
		});
		
	}

}
