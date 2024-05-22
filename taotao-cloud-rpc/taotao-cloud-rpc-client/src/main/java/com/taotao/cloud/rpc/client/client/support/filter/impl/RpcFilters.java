package com.taotao.cloud.rpc.client.client.support.filter.impl;

import com.taotao.cloud.rpc.client.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.client.support.filter.RpcFilter;
import com.taotao.cloud.rpc.common.common.rpc.Pipeline;

/**
 * rpc 过滤器
 * @since 0.2.0
 */
public final class RpcFilters {

    private RpcFilters(){}

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
    public static RpcFilter chains(final RpcFilter firstFilter, final RpcFilter ... filters) {
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
