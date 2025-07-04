package com.taotao.cloud.rpc.client.support.fail.impl;

import com.taotao.cloud.rpc.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.support.fail.FailStrategy;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.RpcResponses;

import javax.annotation.concurrent.ThreadSafe;

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
