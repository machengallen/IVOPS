package com.iv.common.util.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author macheng
 *
 */
public class HibernateTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTemplate.class);
	/**
	 * Hibernate执行模板类
	 * @throws InterruptedException 
	 */
	public static Object execute(HibernateCallBack hc) {
		Session ses = null;
		Transaction tx = null;
		Object obj = null;
		try {
			ses = HibernateUtil.getSession(null);
			tx = ses.beginTransaction();
			//回调接口具体实现
			obj = hc.doInHibernate(ses);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			LOGGER.error("数据库操作失败",e);
			throw new HibernateException(e);
		} finally {
			HibernateUtil.close(ses);
		}
		return obj;
	}
	
}
