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

package com.taotao.cloud.mq.common.retry.core.constant;

import com.taotao.cloud.mq.common.retry.core.support.wait.NoRetryWait;

/**
 * 重试等待时间常量类
 * @author shuigedeng
 * @since 0.0.3
 */
public final class RetryWaitConst {

    private RetryWaitConst() {}

    /**
     * 默认等待类
     */
    public static final Class RETRY_WAIT_CLASS = NoRetryWait.class;

    /**
     * 默认基础值
     * 1. 1s
     */
    public static final long VALUE_MILLS = 1000L;

    /**
     * 最小等待时间
     * 30min
     */
    public static final long MIN_MILLS = 0L;

    /**
     * 最大等待时间
     * 30min
     */
    public static final long MAX_MILLS = 30 * 60 * 1000L;

    /**
     * 增加的毫秒数因数
     * 1. 默认为 2S
     */
    public static final double INCREASE_MILLS_FACTOR = 2000;

    /**
     * 因数
     * 1. 默认为黄金分割比
     */
    public static final double MULTIPLY_FACTOR = 1.618;
}
