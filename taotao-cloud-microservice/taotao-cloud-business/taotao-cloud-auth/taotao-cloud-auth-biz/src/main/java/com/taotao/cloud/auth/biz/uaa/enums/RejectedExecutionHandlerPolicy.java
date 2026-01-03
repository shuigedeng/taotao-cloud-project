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

package com.taotao.cloud.auth.biz.uaa.enums;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略枚举
 */
@SuppressWarnings("unused")
public enum RejectedExecutionHandlerPolicy {
    /**
     * @see ThreadPoolExecutor.CallerRunsPolicy
     */
    CALLER_RUNS /**
     * null
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
            class{
        @Override
        public RejectedExecutionHandler getRejectedHandler ( ) {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        }
    }{
    },
    /**
     * @see ThreadPoolExecutor.AbortPolicy
     */
    ABORT /**
     * null
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
            class{
        @Override
        public RejectedExecutionHandler getRejectedHandler ( ) {
            return new ThreadPoolExecutor.AbortPolicy();
        }
    }{
    },
    /**
     * @see ThreadPoolExecutor.DiscardOldestPolicy
     */
    DISCARD_OLDEST /**
     * null
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
            class{
        @Override
        public RejectedExecutionHandler getRejectedHandler ( ) {
            return new ThreadPoolExecutor.DiscardOldestPolicy();
        }
    }{
    },
    /**
     * @see ThreadPoolExecutor.DiscardPolicy
     */
    DISCARD /**
     * null
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
            class{
        @Override
        public RejectedExecutionHandler getRejectedHandler ( ) {
            return new ThreadPoolExecutor.DiscardPolicy();
        }
    }{
    };

    public abstract RejectedExecutionHandler getRejectedHandler();
}
