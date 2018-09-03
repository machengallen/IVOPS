package com.iv.report.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.AbstractMultiTenantConnectionProvider;


public class MultiTenantConnectionProviderImpl extends AbstractMultiTenantConnectionProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3565754320432761251L;

	private static final C3P0ConnectionProvider connectionProvider = new C3P0ConnectionProvider();

	@Override
	protected C3P0ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		/*try {
			connection.createStatement().execute("use alarm_aggregation_service");
		} catch (SQLException e) {
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
		}*/
		connectionProvider.closeConnection(connection);

	}
	
	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		final Connection connection = super.getAnyConnection();
		PreparedStatement ps = connection.prepareStatement(
				"SELECT count(sc.SCHEMA_NAME) FROM information_schema.SCHEMATA sc where sc.SCHEMA_NAME=?");
		ps.setString(1, tenantIdentifier);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			if (rs.getInt(1) == 0) {
				connection.createStatement().execute("use " + ConstantContainer.ALARM_AGGREGATION_DB);
			}else {
				connection.createStatement().execute("use " + tenantIdentifier);
			}
		}
		return connection;
	}

	@Override
	public String getExcuteScriptName(String dbName) {
		return "init_table.sql";
	}

}
