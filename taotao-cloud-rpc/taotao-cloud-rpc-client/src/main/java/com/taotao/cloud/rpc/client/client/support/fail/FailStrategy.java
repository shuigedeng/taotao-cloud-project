package com.taotao.cloud.rpc.client.client.support.fail;

import com.taotao.cloud.rpc.client.client.proxy.RemoteInvokeContext;

/**
 * 失败策略
 * @author shuigedeng
 * @since 0.1.1
 */
public interface FailStrategy {

    /**
     * 失败策略
     * @param context 远程调用上下文
     * @return 最终的结果值
     * @since 0.1.1
     */
    Object fail(final RemoteInvokeContext context);

}
