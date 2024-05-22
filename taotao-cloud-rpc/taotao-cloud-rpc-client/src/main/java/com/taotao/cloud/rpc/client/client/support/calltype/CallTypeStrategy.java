package com.taotao.cloud.rpc.client.client.support.calltype;

import com.taotao.cloud.rpc.client.client.proxy.ServiceContext;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;

/**
 * 调用方式上下文
 * @author shuigedeng
 * @since 0.1.0
 */
public interface CallTypeStrategy {

    /**
     * 获取结果
     * @param proxyContext 代理上下文
     * @param rpcRequest 请求信息
     * @return 结果
     * @since 0.1.0
     */
    RpcResponse result(final ServiceContext proxyContext,
                       final RpcRequest rpcRequest);

}
