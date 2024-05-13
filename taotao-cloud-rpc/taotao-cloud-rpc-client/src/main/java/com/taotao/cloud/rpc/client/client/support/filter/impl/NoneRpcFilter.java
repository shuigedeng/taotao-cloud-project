package com.github.houbb.rpc.client.support.filter.impl;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.rpc.client.proxy.RemoteInvokeContext;
import com.github.houbb.rpc.client.support.filter.RpcFilter;

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
