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

package com.taotao.cloud.jdbcpool.datasource;

import com.taotao.cloud.jdbcpool.api.ILifeCycle;
import com.taotao.cloud.jdbcpool.api.IPooledDataSourceConfig;
import com.taotao.cloud.jdbcpool.constant.PooledConst;

/**
 * @author shuigedeng
 * @since 1.1.0
 */
public abstract class AbstractPooledDataSourceConfig extends AbstractDataSourceConfig
        implements IPooledDataSourceConfig, ILifeCycle {

    /**
     * 最小尺寸
     */
    protected int minSize = PooledConst.DEFAULT_MIN_SIZE;

    /**
     * 最大尺寸
     */
    protected int maxSize = PooledConst.DEFAULT_MAX_SIZE;

    /**
     * 最大的等待时间
     */
    protected long maxWaitMills = PooledConst.DEFAULT_MAX_WAIT_MILLS;

    /**
     * 验证查询
     */
    protected String validQuery = PooledConst.DEFAULT_VALID_QUERY;

    /**
     * 验证的超时时间
     */
    protected int validTimeOutSeconds = PooledConst.DEFAULT_VALID_TIME_OUT_SECONDS;

    /**
     * 获取时验证
     */
    protected boolean testOnBorrow = PooledConst.DEFAULT_TEST_ON_BORROW;

    /**
     * 归还时验证
     */
    protected boolean testOnReturn = PooledConst.DEFAULT_TEST_ON_RETURN;

    /**
     * 闲暇时验证
     */
    protected boolean testOnIdle = PooledConst.DEFAULT_TEST_ON_IDLE;

    /**
     * 闲暇时验证的时间间隔
     */
    protected long testOnIdleIntervalSeconds = PooledConst.DEFAULT_TEST_ON_IDLE_INTERVAL_SECONDS;

    public int getMinSize() {
        return minSize;
    }

    @Override
    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getMaxWaitMills() {
        return maxWaitMills;
    }

    @Override
    public void setMaxWaitMills(long maxWaitMills) {
        this.maxWaitMills = maxWaitMills;
    }

    public String getValidQuery() {
        return validQuery;
    }

    @Override
    public void setValidQuery(String validQuery) {
        this.validQuery = validQuery;
    }

    public int getValidTimeOutSeconds() {
        return validTimeOutSeconds;
    }

    @Override
    public void setValidTimeOutSeconds(int validTimeOutSeconds) {
        this.validTimeOutSeconds = validTimeOutSeconds;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    @Override
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    @Override
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestOnIdle() {
        return testOnIdle;
    }

    @Override
    public void setTestOnIdle(boolean testOnIdle) {
        this.testOnIdle = testOnIdle;
    }

    public long getTestOnIdleIntervalSeconds() {
        return testOnIdleIntervalSeconds;
    }

    @Override
    public void setTestOnIdleIntervalSeconds(long testOnIdleIntervalSeconds) {
        this.testOnIdleIntervalSeconds = testOnIdleIntervalSeconds;
    }

    @Override
    public void init() {}

    @Override
    public void destroy() {}
}
