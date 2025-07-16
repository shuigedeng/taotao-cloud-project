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
 * 内置耗时 rpc 拦截器实现
 * @author shuigedeng
 * @since 0.1.4
 */
public class CostTimeRpcInterceptor extends RpcInterceptorAdaptor {

    private static final Logger LOG = LoggerFactory.getLogger(CostTimeRpcInterceptor.class);

    @Override
    public void before(RpcInterceptorContext context) {}

    @Override
    public void after(RpcInterceptorContext context) {
        long costMills = context.endTime() - context.startTime();
        //        log.info("[Interceptor] cost time {} mills for traceId: {}", costMills,
        //                context.traceId());
    }
}
