/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.taotao.cloud.auth.biz.uaa.enums;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略枚举
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/15 12:44
 */
@SuppressWarnings("unused")
public enum RejectedExecutionHandlerPolicy {
    /**
     * @see ThreadPoolExecutor.CallerRunsPolicy
     */
    CALLER_RUNS {
        @Override
        public RejectedExecutionHandler getRejectedHandler() {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        }
    },
    /**
     * @see ThreadPoolExecutor.AbortPolicy
     */
    ABORT
            {
                @Override
                public RejectedExecutionHandler getRejectedHandler() {
                    return new ThreadPoolExecutor.AbortPolicy();
                }
            },
    /**
     * @see ThreadPoolExecutor.DiscardOldestPolicy
     */
    DISCARD_OLDEST
            {
                @Override
                public RejectedExecutionHandler getRejectedHandler() {
                    return new ThreadPoolExecutor.DiscardOldestPolicy();
                }
            },
    /**
     * @see ThreadPoolExecutor.DiscardPolicy
     */
    DISCARD
            {
                @Override
                public RejectedExecutionHandler getRejectedHandler() {
                    return new ThreadPoolExecutor.DiscardPolicy();
                }
            };

    public abstract RejectedExecutionHandler getRejectedHandler();
}
