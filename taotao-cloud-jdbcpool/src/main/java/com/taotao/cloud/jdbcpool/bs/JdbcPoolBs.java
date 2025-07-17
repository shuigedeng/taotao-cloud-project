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

package com.taotao.cloud.jdbcpool.bs;

import com.taotao.cloud.jdbcpool.constant.DriverNameConst;
import com.taotao.cloud.jdbcpool.constant.PooledConst;
import com.taotao.cloud.jdbcpool.datasource.PooledDataSource;
import com.taotao.cloud.jdbcpool.datasource.UnPooledDataSource;
import javax.sql.DataSource;

/**
 * jdbc 线程池引导类
 * @since 1.6.0
 */
public class JdbcPoolBs {

    /**
     * 驱动类
     */
    private String driverClass = DriverNameConst.MYSQL_8;

    private String url =
            "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";

    private String username = "root";

    private String password = "123456";

    /**
     * 最小尺寸
     */
    private int minSize = PooledConst.DEFAULT_MIN_SIZE;

    /**
     * 最大尺寸
     */
    private int maxSize = PooledConst.DEFAULT_MAX_SIZE;

    /**
     * 最大的等待时间
     */
    private long maxWaitMills = PooledConst.DEFAULT_MAX_WAIT_MILLS;

    /**
     * 验证查询
     */
    private String validQuery = PooledConst.DEFAULT_VALID_QUERY;

    /**
     * 验证的超时时间
     */
    private int validTimeOutSeconds = PooledConst.DEFAULT_VALID_TIME_OUT_SECONDS;

    /**
     * 获取时验证
     */
    private boolean testOnBorrow = PooledConst.DEFAULT_TEST_ON_BORROW;

    /**
     * 归还时验证
     */
    private boolean testOnReturn = PooledConst.DEFAULT_TEST_ON_RETURN;

    /**
     * 闲暇时验证
     */
    private boolean testOnIdle = PooledConst.DEFAULT_TEST_ON_IDLE;

    /**
     * 闲暇时验证的时间间隔
     */
    private long testOnIdleIntervalSeconds = PooledConst.DEFAULT_TEST_ON_IDLE_INTERVAL_SECONDS;

    public JdbcPoolBs driverClass(String driverClass) {
        this.driverClass = driverClass;
        return this;
    }

    public JdbcPoolBs url(String url) {
        this.url = url;
        return this;
    }

    public JdbcPoolBs username(String username) {
        this.username = username;
        return this;
    }

    public JdbcPoolBs password(String password) {
        this.password = password;
        return this;
    }

    public JdbcPoolBs minSize(int minSize) {
        this.minSize = minSize;
        return this;
    }

    public JdbcPoolBs maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public JdbcPoolBs maxWaitMills(long maxWaitMills) {
        this.maxWaitMills = maxWaitMills;
        return this;
    }

    public JdbcPoolBs validQuery(String validQuery) {
        this.validQuery = validQuery;
        return this;
    }

    public JdbcPoolBs validTimeOutSeconds(int validTimeOutSeconds) {
        this.validTimeOutSeconds = validTimeOutSeconds;
        return this;
    }

    public JdbcPoolBs testOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        return this;
    }

    public JdbcPoolBs testOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
        return this;
    }

    public JdbcPoolBs testOnIdle(boolean testOnIdle) {
        this.testOnIdle = testOnIdle;
        return this;
    }

    public JdbcPoolBs testOnIdleIntervalSeconds(long testOnIdleIntervalSeconds) {
        this.testOnIdleIntervalSeconds = testOnIdleIntervalSeconds;
        return this;
    }

    public static JdbcPoolBs newInstance() {
        return new JdbcPoolBs();
    }

    /**
     * 简单的池化功能
     * @return 池化
     */
    public DataSource pooled() {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl(url);

        dataSource.setMinSize(minSize);
        dataSource.setMaxSize(maxSize);
        dataSource.setMaxWaitMills(maxWaitMills);

        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnIdle(testOnIdle);
        dataSource.setTestOnIdleIntervalSeconds(testOnIdleIntervalSeconds);
        dataSource.setTestOnReturn(testOnReturn);

        dataSource.setValidQuery(validQuery);
        dataSource.setValidTimeOutSeconds(validTimeOutSeconds);

        dataSource.init();
        return dataSource;
    }

    /**
     * 非池化
     * @return 结果
     */
    public DataSource unPooled() {
        UnPooledDataSource dataSource = new UnPooledDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl(url);
        return dataSource;
    }
}
