package com.iv.common.util.hibernate;

import javax.persistence.EntityManagerFactory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据库初始化工具类
 * 
 * @author macheng
 *
 */
@Component("hibernateUtil")
public class HibernateUtil {

	private static SessionFactory factory;

	/*
	 * static { try { Configuration cfg = new Configuration().configure();
	 * ServiceRegistry sr = new StandardServiceRegistryBuilder()
	 * .applySettings(cfg.getProperties()).build(); factory =
	 * cfg.buildSessionFactory(sr); } catch (HibernateException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 */
	@Autowired
	public HibernateUtil(EntityManagerFactory emf) {
		super();
		// TODO Auto-generated constructor stub
		if (emf.unwrap(SessionFactory.class) == null) {

			throw new NullPointerException("factory is not a hibernate factory");
		}
		HibernateUtil.factory = emf.unwrap(SessionFactory.class);
	}

	/*
	 * @Autowired public void setFactory(EntityManagerFactory emf) {
	 * 
	 * if(emf.unwrap(SessionFactory.class) == null){
	 * 
	 * throw new NullPointerException("factory is not a hibernate factory"); }
	 * HibernateUtil.factory = emf.unwrap(SessionFactory.class); }
	 */
	public static Session getSession(String tenantId) {
		if (null != tenantId) {
			return factory.withOptions().tenantIdentifier(tenantId).openSession();
		}
		return factory.openSession();
	}

	public static void close(Session ses) {
		if (ses != null) {
			ses.close();
		}
	}

	public static SessionFactory getFactory() {
		return factory;
	}

	public static void setFactory(SessionFactory factory) {
		HibernateUtil.factory = factory;
	}

}
