package com.github.houbb.rpc.client.support.fail.impl;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.rpc.client.proxy.RemoteInvokeContext;
import com.github.houbb.rpc.client.support.fail.FailStrategy;
import com.github.houbb.rpc.common.rpc.domain.impl.RpcResponses;

/**
 * 快速失败策略
 * @author shuigedeng
 * @since 0.1.1
 */
@ThreadSafe
class FailFastStrategy implements FailStrategy {

    @Override
    public Object fail(final RemoteInvokeContext context) {
        final Class returnType = context.request().returnType();
        return RpcResponses.getResult(context.rpcResponse(), returnType);
    }

}
