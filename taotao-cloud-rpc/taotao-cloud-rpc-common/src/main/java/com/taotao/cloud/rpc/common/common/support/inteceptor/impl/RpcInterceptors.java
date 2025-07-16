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

import com.taotao.cloud.rpc.common.common.rpc.Pipeline;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptor;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptorContext;

/**
 * rpc 拦截器适配器
 * @author shuigedeng
 * @since 0.1.4
 */
public final class RpcInterceptors {

    private RpcInterceptors() {}

    /**
     * 什么都不做的拦截器
     * @return 实现
     * @since 0.2.2
     */
    public static RpcInterceptor none() {
        return new NoneRpcInterceptor();
    }

    /**
     * 耗时拦截器
     * @return 实现
     * @since 0.2.2
     */
    public static RpcInterceptor costTime() {
        return new CostTimeRpcInterceptor();
    }

    /**
     * 日志拦截器
     * @return 实现
     * @since 0.2.2
     */
    public static RpcInterceptor log() {
        return new LogRpcInterceptor();
    }

    /**
     * 生成对应的过滤器链
     * @param first 第一个
     * @param others 其他
     * @return 条件实现
     * @since 0.2.2
     */
    public static RpcInterceptor chains(
            final RpcInterceptor first, final RpcInterceptor... others) {
        return new AbstractRpcInterceptorInit() {
            @Override
            protected void init(Pipeline<RpcInterceptor> pipeline, RpcInterceptorContext context) {
                pipeline.addLast(first);

                //                if(ArrayUtil.isNotEmpty(others)) {
                //                    for(RpcInterceptor other : others) {
                //                        if(ObjectUtil.isNull(other)) {
                //                            continue;
                //                        }
                //                        pipeline.addLast(other);
                //                    }
                //                }
            }
        };
    }
}
