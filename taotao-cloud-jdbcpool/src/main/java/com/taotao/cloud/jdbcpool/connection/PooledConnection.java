package com.github.houbb.thread.pool.connection;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.thread.pool.api.IPooledDataSourceConfig;
import com.github.houbb.thread.pool.exception.JdbcPoolException;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 池化的连接
 *
 * @since 1.1.0
 */
public class PooledConnection implements IPooledConnection {

    private static final Log LOG = LogFactory.getLog(PooledConnection.class);

    /**
     * 是否繁忙
     * @since 1.1.0
     */
    private volatile boolean isBusy;

    /**
     * 数据库链接信息
     * @since 1.1.0
     */
    private Connection connection;

    /**
     * 对应的数据源信息
     *
     * @since 1.1.0
     */
    private IPooledDataSourceConfig dataSource;

    @Override
    public Statement createStatement() throws SQLException {
        checkStatus();

        return connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkStatus();

        return connection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        checkStatus();

        return connection.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        checkStatus();

        return connection.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkStatus();

        connection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        checkStatus();

        return connection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        checkStatus();

        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        checkStatus();

        connection.rollback();
    }

    @Override
    public void close() throws SQLException {
        checkStatus();

        this.dataSource.returnConnection(this);
    }

    @Override
    public boolean isClosed() throws SQLException {
        checkStatus();

        return connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        checkStatus();

        return connection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        checkStatus();

        connection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        checkStatus();

        return connection.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        checkStatus();

        connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        checkStatus();

        return connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        checkStatus();

        connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        checkStatus();

        return connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        checkStatus();

        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        checkStatus();

        connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        checkStatus();

        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkStatus();

        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkStatus();

        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        checkStatus();

        return connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        checkStatus();

        connection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        checkStatus();

        connection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        checkStatus();

        return connection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        checkStatus();

        return connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        checkStatus();

        return connection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        checkStatus();

        connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        checkStatus();

        connection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        checkStatus();

        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        checkStatus();

        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        checkStatus();

        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        checkStatus();

        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        checkStatus();

        return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        checkStatus();

        return connection.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        checkStatus();

        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        checkStatus();

        return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        checkStatus();

        return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        checkStatus();

        return connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        checkStatus();

        return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        checkStatus();

        connection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        checkStatus();

        connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        checkStatus();

        return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        checkStatus();

        return connection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        checkStatus();

        return connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        checkStatus();

        return connection.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        checkStatus();

        connection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        checkStatus();

        return connection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        checkStatus();

        connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        checkStatus();

        connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        checkStatus();

        return connection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        checkStatus();

        return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        checkStatus();

        return connection.isWrapperFor(iface);
    }

    @Override
    public boolean isBusy() {
        return isBusy;
    }

    @Override
    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public IPooledDataSourceConfig getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(IPooledDataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 对于设置为繁忙的连接，等价于关闭
     * @since 1.4.0
     */
    private void checkStatus() {
        if(!isBusy) {
            throw new JdbcPoolException("Connection has been closed");
        }
    }

}
