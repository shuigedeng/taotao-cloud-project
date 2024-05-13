package com.taotao.cloud.rpc.client.client.support.filter.impl;

import com.taotao.cloud.rpc.client.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.client.support.filter.RpcFilter;

/**
 * 什么都不做的过滤器
 * @author shuigedeng
 * @since 0.2.0
 */
@ThreadSafe
public class NoneRpcFilter implements RpcFilter {

    @Override
    @SuppressWarnings("all")
    public void filter(RemoteInvokeContext context) {
        // do nothing
    }

}
