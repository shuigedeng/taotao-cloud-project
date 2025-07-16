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

package com.taotao.cloud.rpc.common.common.rpc.domain.impl;

import com.taotao.cloud.rpc.common.common.exception.RpcTimeoutException;
import com.taotao.cloud.rpc.common.common.exception.ShutdownException;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;

/**
 * 响应工厂类
 *
 * @author shuigedeng
 * @since 2024.06
 */
public final class RpcResponseFactory {

    private RpcResponseFactory() {}

    /**
     * 超时异常信息
     *
     * @since 2024.06
     */
    private static final DefaultRpcResponse TIMEOUT;

    /**
     * 状态已经关闭
     *
     * @since 0.1.3
     */
    private static final DefaultRpcResponse SHUTDOWN;

    /**
     * 打断异常
     *
     * @since 0.1.3
     */
    private static final DefaultRpcResponse INTERRUPTED;

    static {
        TIMEOUT = new DefaultRpcResponse();
        TIMEOUT.error(new RpcTimeoutException());

        SHUTDOWN = new DefaultRpcResponse();
        SHUTDOWN.error(new ShutdownException());

        INTERRUPTED = new DefaultRpcResponse();
        INTERRUPTED.error(new InterruptedException());
    }

    /**
     * 获取超时响应结果
     *
     * @return 响应结果
     * @since 2024.06
     */
    public static RpcResponse timeout() {
        return TIMEOUT;
    }

    /**
     * 获取 shutdown 响应结果
     *
     * @return 响应结果
     * @since 0.1.3
     */
    public static RpcResponse shutdown() {
        return SHUTDOWN;
    }

    /**
     * 获取 INTERRUPTED 响应结果
     *
     * @return 响应结果
     * @since 0.1.3
     */
    public static RpcResponse interrupted() {
        return INTERRUPTED;
    }
}
