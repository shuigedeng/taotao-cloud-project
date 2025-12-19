/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.tx.rm.connection;

import com.taotao.cloud.tx.rm.transactional.TransactionalType;
import com.taotao.cloud.tx.rm.transactional.TtcTx;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 自定义的数据库连接类（必须要实现JDBC的Connection接口）
/**
 * TtcConnection
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class TtcConnection implements Connection {

    // 原本应该返回的数据库连接对象
    private Connection connection;
    // 存放参与分布式事务的子事务
    private TtcTx ttcTx;

    // 负责提交事务的线程
    private ExecutorService commitT = Executors.newSingleThreadExecutor();
    // 负责回滚事务的线程
    private ExecutorService rollbackT = Executors.newSingleThreadExecutor();

    public TtcConnection( Connection connection, TtcTx ttcTx ) {
        this.connection = connection;
        this.ttcTx = ttcTx;
    }

    @Override
    public void commit() throws SQLException {
        // 交给线程池中的线程来做最终的事务提交
        commitT.execute(
                () -> {
                    try {
                        // 阻塞线程，禁止提交
                        ttcTx.getTask().waitTask();
                        // 如果管理者返回事务可以提交，则提交事务
                        if (ttcTx.getTransactionalType().equals(TransactionalType.commit)) {
                            System.out.println("\n收到管理者最终决断：提交事务中\n");
                            connection.commit();
                            System.out.println("\n子事务提交事务成功...\n");
                        }
                        // 否则调用rollback()方法回滚事务
                        else {
                            System.out.println("\n收到管理者最终决断：回滚事务中...\n");
                            connection.rollback();
                            System.out.println("\n子事务回滚事务成功...\n");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void rollback() throws SQLException {
        // 交给线程池中的线程来做最终的事务回滚
        rollbackT.execute(
                () -> {
                    ttcTx.getTask().waitTask();
                    try {
                        connection.rollback();
                        System.out.println("\n\n子事务回滚事务成功...\n\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    /**
     * default
     */
    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement( String sql ) throws SQLException {
        return connection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall( String sql ) throws SQLException {
        return connection.prepareCall(sql);
    }

    @Override
    public String nativeSQL( String sql ) throws SQLException {
        return connection.nativeSQL(sql);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    @Override
    public void setReadOnly( boolean readOnly ) throws SQLException {
        connection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    @Override
    public void setCatalog( String catalog ) throws SQLException {
        connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation( int level ) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    @Override
    public Statement createStatement( int resultSetType, int resultSetConcurrency )
            throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(
            String sql, int resultSetType, int resultSetConcurrency ) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall( String sql, int resultSetType, int resultSetConcurrency )
            throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    @Override
    public void setTypeMap( Map<String, Class<?>> map ) throws SQLException {
        connection.setTypeMap(map);
    }

    @Override
    public void setHoldability( int holdability ) throws SQLException {
        connection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint( String name ) throws SQLException {
        return connection.setSavepoint(name);
    }

    @Override
    public void rollback( Savepoint savepoint ) throws SQLException {
        connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint( Savepoint savepoint ) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(
            int resultSetType, int resultSetConcurrency, int resultSetHoldability )
            throws SQLException {
        return connection.createStatement(
                resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(
            String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability )
            throws SQLException {
        return connection.prepareStatement(
                sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(
            String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability )
            throws SQLException {
        return connection.prepareCall(
                sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement( String sql, int autoGeneratedKeys )
            throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement( String sql, int[] columnIndexes ) throws SQLException {
        return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement( String sql, String[] columnNames )
            throws SQLException {
        return connection.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    @Override
    public boolean isValid( int timeout ) throws SQLException {
        return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo( String name, String value ) throws SQLClientInfoException {
        connection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo( Properties properties ) throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo( String name ) throws SQLException {
        return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    @Override
    public Array createArrayOf( String typeName, Object[] elements ) throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct( String typeName, Object[] attributes ) throws SQLException {
        return connection.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema( String schema ) throws SQLException {
        connection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return connection.getSchema();
    }

    @Override
    public void abort( Executor executor ) throws SQLException {
        connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout( Executor executor, int milliseconds ) throws SQLException {
        connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return connection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap( Class<T> iface ) throws SQLException {
        return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor( Class<?> iface ) throws SQLException {
        return connection.isWrapperFor(iface);
    }

    @Override
    public void setAutoCommit( boolean autoCommit ) throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false);
        }
    }
}
