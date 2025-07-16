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

package com.taotao.cloud.rpc.client.support.filter.impl;

import com.taotao.cloud.rpc.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.support.filter.RpcFilter;
import com.taotao.cloud.rpc.common.common.rpc.Pipeline;

/**
 * rpc 过滤器
 * @since 0.2.0
 */
public final class RpcFilters {

    private RpcFilters() {}

    /**
     * 什么都不做的 filter
     * @return filter
     * @since 0.2.0
     */
    public static RpcFilter none() {
        return new NoneRpcFilter();
    }

    /**
     * 生成对应的过滤器链
     * @param filters 过滤器
     * @return 条件实现
     * @since 0.2.0
     */
    public static RpcFilter chains(final RpcFilter firstFilter, final RpcFilter... filters) {
        return new AbstractRpcFilterInit() {
            @Override
            protected void init(Pipeline<RpcFilter> pipeline, RemoteInvokeContext context) {
                pipeline.addLast(firstFilter);

                //                if(ArrayUtil.isNotEmpty(filters)) {
                //                    for(RpcFilter filter : filters) {
                //                        if(ObjectUtil.isNull(filter)) {
                //                            continue;
                //                        }
                //                        pipeline.addLast(filter);
                //                    }
                //                }
            }
        };
    }
}
