
package com.taotao.cloud.data.mybatisplus.dynamic.config;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.taotao.cloud.data.mybatisplus.dynamic.conatant.DataSourceConstants;
import com.taotao.cloud.data.mybatisplus.dynamic.properties.MybatisPlusDynamicDataSourceProperties;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源来源的默认实现是YmlDynamicDataSourceProvider，其从yaml或properties中读取信息并解析出所有数据源信息。
 * 我们继承AbstractJdbcDataSourceProvider从数据源中获取配置信息
 * 从3.4.0开始，可以注入多个DynamicDataSourceProvider的Bean以实现同时从多个不同来源加载数据源，注意同名会被覆盖。
 */
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

	private final DataSourceProperties properties;
	private final MybatisPlusDynamicDataSourceProperties dynamicDataSourceProperties;

	private final StringEncryptor stringEncryptor;

	public JdbcDynamicDataSourceProvider(StringEncryptor stringEncryptor, DataSourceProperties properties, MybatisPlusDynamicDataSourceProperties dynamicDataSourceProperties) {
		super(properties.getDriverClassName(), properties.getUrl(), properties.getUsername(), properties.getPassword());
		this.stringEncryptor = stringEncryptor;
		this.properties = properties;
		this.dynamicDataSourceProperties = dynamicDataSourceProperties;
	}

	/**
	 * 执行语句获得数据源参数
	 *
	 * @param statement 语句
	 * @return 数据源参数
	 * @throws SQLException sql异常
	 */
	@Override
	protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
		ResultSet rs = statement.executeQuery(dynamicDataSourceProperties.getQueryDsSql());

		Map<String, DataSourceProperty> map = new HashMap<>(8);
		while (rs.next()) {
			String name = rs.getString(DataSourceConstants.DS_NAME);
			String username = rs.getString(DataSourceConstants.DS_USER_NAME);
			String password = rs.getString(DataSourceConstants.DS_USER_PWD);
			String url = rs.getString(DataSourceConstants.DS_JDBC_URL);
			DataSourceProperty property = new DataSourceProperty();
			property.setUsername(username);
			property.setLazy(true);
			try {
				property.setPassword(stringEncryptor.decrypt(password));
			} catch (Exception e) {
				property.setPassword(password);
			}
			property.setUrl(url);
			map.put(name, property);
		}

		// 添加默认主数据源
		DataSourceProperty property = new DataSourceProperty();
		property.setUsername(properties.getUsername());
		property.setPassword(properties.getPassword());
		property.setUrl(properties.getUrl());
		property.setLazy(false);
		map.put(DataSourceConstants.DS_MASTER, property);
		return map;
	}

}
