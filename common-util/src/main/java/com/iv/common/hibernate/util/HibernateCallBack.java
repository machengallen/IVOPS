package com.iv.common.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;


/**
 * 回调接口
 * @author macheng
 *
 */
public interface HibernateCallBack {

	Object doInHibernate(Session ses) throws HibernateException;

}
