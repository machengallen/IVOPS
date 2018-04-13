package com.iv.common.hibernate.util;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6687923144918600029L;

	//private final DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	private DataSource dataSource;

	@Override
	protected DataSource selectAnyDataSource() {
		// TODO Auto-generated method stub
		Object dataSource = entityManagerFactory.getProperties().get("javax.persistence.nonJtaDataSource");
		return (dataSource != null && dataSource instanceof DataSource
				? (DataSource) dataSource : null);
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

}
