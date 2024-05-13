package com.taotao.cloud.rpc.client.client.support.fail.impl;

import com.taotao.cloud.rpc.client.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.client.support.fail.FailStrategy;

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
