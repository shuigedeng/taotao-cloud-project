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

package com.taotao.cloud.jdbcpool.constant;

/**
 * 线程池常量
 * @since 1.1.0
 */
public final class PooledConst {

    private PooledConst() {}

    /**
     * 默认的最小连接数
     */
    public static final int DEFAULT_MIN_SIZE = 10;

    /**
     * 默认最大的连接数
     */
    public static final int DEFAULT_MAX_SIZE = 300;

    /**
     * 默认最大的等待毫秒数
     * 默认：1 min
     */
    public static final int DEFAULT_MAX_WAIT_MILLS = 60 * 1000;

    /**
     * 默认验证查询的语句
     */
    public static final String DEFAULT_VALID_QUERY = "select 1 from dual";

    /**
     * 默认的验证的超时时间
     */
    public static final int DEFAULT_VALID_TIME_OUT_SECONDS = 5;

    /**
     * 获取连接时，默认不校验
     */
    public static final boolean DEFAULT_TEST_ON_BORROW = false;

    /**
     * 归还连接时，默认不校验
     */
    public static final boolean DEFAULT_TEST_ON_RETURN = false;

    /**
     * 默认闲暇的时候，进行校验
     */
    public static final boolean DEFAULT_TEST_ON_IDLE = true;

    /**
     * 1min 自动校验一次
     */
    public static final long DEFAULT_TEST_ON_IDLE_INTERVAL_SECONDS = 60;
}
