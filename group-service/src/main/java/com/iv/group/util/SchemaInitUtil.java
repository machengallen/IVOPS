package com.iv.group.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.jpa.util.hibernate.AbstractMultiTenantConnectionProvider;
import com.iv.jpa.util.hibernate.HibernateUtil;

@Component
@DependsOn("hibernateUtil")
public class SchemaInitUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchemaInitUtil.class);

	/**
	 * 初始化更新所有database表
	 */
	@PostConstruct
	public void InitSchema() {

		AbstractMultiTenantConnectionProvider connectionProvider = (AbstractMultiTenantConnectionProvider) ((SessionFactoryImpl) HibernateUtil
				.getFactory()).getServiceRegistry().getService(MultiTenantConnectionProvider.class);
		try {

			final Connection connection = connectionProvider.getAnyConnection();
			// 初始化租户的业务库表
			String schemataSql = "SELECT sc.SCHEMA_NAME FROM information_schema.SCHEMATA sc where sc.SCHEMA_NAME like 's80%' or sc.SCHEMA_NAME like 't80%'";
			sqlStatement(connection, schemataSql, ConstantContainer.BUS_SQL_SCRIPT_NAME);

			// 初始化基础知识库表
			/*schemataSql = "SELECT sc.SCHEMA_NAME FROM information_schema.SCHEMATA sc where sc.SCHEMA_NAME='"
					+ ConstantContainer.KNOWLEDGE_DB_ID + "'";
			sqlStatement(connection, schemataSql, ConstantContainer.KNOWLEDGE_SQL_SCRIPT_NAME);*/

		} catch (SQLException e) {
			LOGGER.error("**********table update failed**********", e);
		}
	}

	private void sqlStatement(Connection connection, String schemataSql, String sqlScript) throws SQLException {

		String line;
		PreparedStatement ps = connection.prepareStatement(schemataSql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String schema = rs.getString(1);
			connection.createStatement().execute("use " + schema);
			InputStream stream = getClass().getClassLoader().getResourceAsStream(sqlScript);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuilder stringBuilder = new StringBuilder();
			try {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (!"".equals(line) && !line.startsWith("--")) {
						stringBuilder.append(line);
					} else {
						connection.createStatement().execute(stringBuilder.toString());
						stringBuilder.setLength(0);
					}
				}
			} catch (IOException e) {
				LOGGER.error("读取sql脚本失败", e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.error("IO关闭异常", e);
				}
			}
		
		}

	}

}
