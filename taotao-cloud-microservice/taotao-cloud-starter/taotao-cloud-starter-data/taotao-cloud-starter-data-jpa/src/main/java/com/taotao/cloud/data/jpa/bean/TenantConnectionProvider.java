 /*
  * Copyright (c) 2017-2020 original authors
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * https://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
 package com.taotao.cloud.data.jpa.bean;

 import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;

 import javax.sql.DataSource;
 import java.io.Serial;
 import java.sql.Connection;
 import java.sql.SQLException;

 /**
  * jpa 多租户连接提供者 
  *
  * @author shuigedeng
  * @version 2021.9
  * @since 2021-09-04 07:31:10
  */
 public class TenantConnectionProvider implements MultiTenantConnectionProvider {

	 @Serial
	 private static final long serialVersionUID = -1166976596388409766L;

	 private final transient DataSource dataSource;

	 public TenantConnectionProvider(final DataSource dataSource) {
		 this.dataSource = dataSource;
	 }

	 @Override
	 public Connection getAnyConnection() throws SQLException {
		 return dataSource.getConnection();
	 }

	 @Override
	 public void releaseAnyConnection(final Connection connection) throws SQLException {
		 connection.close();
	 }

	 @Override
	 public Connection getConnection(final String tenantIdentifier) throws SQLException {
		 final Connection connection = getAnyConnection();
		 connection.setCatalog(tenantIdentifier);
		 connection.setSchema(tenantIdentifier);
		 return connection;
	 }

	 @Override
	 public void releaseConnection(final String tenantIdentifier, final Connection connection)
		 throws SQLException {
		 connection.setSchema(tenantIdentifier);
		 connection.setCatalog(tenantIdentifier);
		 releaseAnyConnection(connection);
	 }

	 @Override
	 public boolean supportsAggressiveRelease() {
		 return false;
	 }

	 @Override
	 public boolean isUnwrappableAs(final Class unwrapType) {
		 return false;
	 }

	 @Override
	 public <T> T unwrap(final Class<T> unwrapType) {
		 return null;
	 }
 }
