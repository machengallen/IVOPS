package com.iv.dao.impl;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.iv.common.util.hibernate.HibernateCallBack;
import com.iv.common.util.hibernate.HibernateTemplate;
import com.iv.common.util.hibernate.HibernateTemplateWithTenant;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.dao.ILocalAuthDao;
import com.iv.entity.LocalAuth;

@Repository
public class LocalAuthDaoImpl implements ILocalAuthDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalAuthDaoImpl.class);	

	@Override
	public LocalAuth selectLocalauthInfoByName(String localAuthName) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("from LocalAuth l where l.userName=?")
						.setParameter(0, localAuthName).uniqueResult();
			}
		});
	}

	@Override
	public LocalAuth saveOrUpdateLocalAuth(LocalAuth localAuth) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(localAuth);
				return localAuth;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public LocalAuth selectLocalAuthById(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(LocalAuth.class, userId);
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	/**
	 * 根据用户名查询用户信息
	 */
	public LocalAuth selectLocalAuthByUserName(String userName) throws RuntimeException {
		// TODO Auto-generated method stub
		return (LocalAuth) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createCriteria(LocalAuth.class, "la").add(Restrictions.eq("la.userName", userName))
						.uniqueResult();
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}
	
	
	/*
	// private static final IMessageDao MESSAGE_DAO = MessageDaoImpl.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalUserDaoImpl.class);

	private static LocalUserDaoImpl daoImpl = new LocalUserDaoImpl();

	private LocalUserDaoImpl() {

	}

	public static LocalUserDaoImpl getInstance() {
		return daoImpl;
	}

	@Override
	public LocalAuth selectUserAuthByUserName(final String userName) throws RuntimeException {
		return (LocalAuth) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createCriteria(LocalAuth.class, "la").add(Restrictions.eq("la.userName", userName))
						.uniqueResult();
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public LocalAuth saveUserAuth(final LocalAuth user) throws RuntimeException {
		return (LocalAuth) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(user);
				return user;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public LocalAuth selectByWechatInfo(final UserWechatEntity wechatInfo) throws RuntimeException {
		return (LocalAuth) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {

				LocalAuth localAuth = (LocalAuth) ses.createCriteria(LocalAuth.class, "la")
						.add(Restrictions.eq("la.wechatInfo", wechatInfo)).uniqueResult();
				return localAuth;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalAuth> selectAll() throws RuntimeException {
		return (List<LocalAuth>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from LocalAuth").list();
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public boolean updateOpenId(final int userId, final UserWechatEntity wechatEntity) throws RuntimeException {
		return (boolean) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				LocalAuth localAuth = (LocalAuth) ses.createCriteria(LocalAuth.class, "la")
						.add(Restrictions.eq("la.wechatInfo", wechatEntity)).uniqueResult();
				if (null == localAuth) {
					localAuth = (LocalAuth) ses.get(LocalAuth.class, userId);
					localAuth.setWechatInfo(wechatEntity);
					localAuth.setBoundFlag((byte) 1);
					return true;
				} else {
					localAuth = (LocalAuth) ses.get(LocalAuth.class, userId);
					localAuth.setBoundFlag((byte) 2);
					return false;
				}

				
				 * //测试，绑定关系可不唯一 LocalAuth localAuth = (LocalAuth) ses.get(LocalAuth.class,
				 * userId); localAuth.setWechatInfo(wechatEntity); return null;
				 
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public LocalAuth selectUserAuthById(final int userId) throws RuntimeException {
		return (LocalAuth) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(LocalAuth.class, userId);
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public void unboundWechat(final UserWechatEntity wechatEntity) throws RuntimeException {
		HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				LocalAuth localAuth = (LocalAuth) ses.createCriteria(LocalAuth.class, "la")
						.add(Restrictions.eq("la.wechatInfo", wechatEntity)).uniqueResult();
				if (null != localAuth) {
					localAuth.setWechatInfo(null);
					localAuth.setBoundFlag((byte) 0);
				}
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@Override
	public int wechatBindingAck(final int userId) throws RuntimeException {
		return (int) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				int i = 0;
				while (true) {
					i += 250;
					LocalAuth localAuth = (LocalAuth) ses.get(LocalAuth.class, userId);
					// 判断绑定状态标志位
					if (1 == localAuth.getBoundFlag()) {
						return 0;
					} else if (2 == localAuth.getBoundFlag()) {
						// 将进行绑定的微信号已被其他账户使用
						localAuth.setBoundFlag((byte) 0);
						return 1;
					}
					// 查询延时0.25s
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						LOGGER.error("wechatBindingAck():[线程中断]:i = " + i, e);
					}
					// 默认10s后断开连接
					if (i >= 10000) {
						return 2;
					}
				}
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalAuth> selectByIds(List<Integer> ids) throws RuntimeException {
		return (List<LocalAuth>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				List<LocalAuth> users = new ArrayList<LocalAuth>();
				for (Integer integer : ids) {
					users.add(ses.get(LocalAuth.class, integer));
				}
				return users;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalAuth> selectUserByRole(String roleName, String tenantId) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<LocalAuth>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select l from LocalAuth l join l.roles r where r.name=? and r.tenantId=?")
						.setParameter(0, roleName).setParameter(1, tenantId).list();
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalAuth> selectByCondition(String userName, String realName, String tel) throws RuntimeException {
		return (List<LocalAuth>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				StringBuilder hql = new StringBuilder("from LocalAuth l where ");
				boolean flag = false;
				if(!StringUtils.isEmpty(userName)) {
					hql.append("l.userName=").append("'").append(userName).append("'");
					flag = true;
				}
				if(!StringUtils.isEmpty(realName)) {
					if(flag) {
						hql.append(" and ");
					}
					hql.append("l.realName=").append("'").append(realName).append("'");
					flag = true;
				}
				if(!StringUtils.isEmpty(tel)) {
					if(flag) {
						hql.append(" and ");
					}
					hql.append("l.tel=").append("'").append(tel).append("'");
					flag = true;
				}
				if(flag) {
					return ses.createQuery(hql.toString()).list();
				}
				return null;
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalAuth> selectUserAuthByRealName(String realName) throws RuntimeException {
		
		return (List<LocalAuth>) HibernateTemplateWithTenant.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from LocalAuth l where l.realName like '%" + realName + "%'").list();
			}
		}, ConstantContainer.TENANT_SHARED_ID);
	}
*/}
