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

package com.taotao.cloud.rpc.common.common.support.inteceptor.impl;

import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志拦截器
 *
 * @author shuigedeng
 * @since 0.2.2
 */
public class LogRpcInterceptor extends RpcInterceptorAdaptor {

    private static final Logger LOG = LoggerFactory.getLogger(LogRpcInterceptor.class);

    @Override
    public void before(RpcInterceptorContext context) {
        //        log.info("[Interceptor] param {} for traceId {}",
        // Arrays.toString(context.params()), context.traceId());
    }

    @Override
    public void after(RpcInterceptorContext context) {
        //        log.info("[Interceptor] result {} for traceId {}", context.result(),
        // context.traceId());
    }
}
