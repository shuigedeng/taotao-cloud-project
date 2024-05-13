package com.taotao.cloud.rpc.client.client.proxy;

/**
 * 远程调用服务
 * @author shuigedeng
 * @since 0.1.1
 */
public interface RemoteInvokeService {

    /**
     * 远程调用
     * @param context 远程调用上下文
     * @return 最终调用结果
     * @since 0.1.1
     */
    Object remoteInvoke(final RemoteInvokeContext context);

}
