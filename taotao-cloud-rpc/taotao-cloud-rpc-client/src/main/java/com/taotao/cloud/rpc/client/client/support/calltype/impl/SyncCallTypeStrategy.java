package com.taotao.cloud.rpc.client.client.support.calltype.impl;

import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.client.client.proxy.ServiceContext;
import com.taotao.cloud.rpc.client.client.support.calltype.CallTypeStrategy;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import javax.annotation.concurrent.ThreadSafe;

/**
 * 同步调用服务实现类
 *
 * @author shuigedeng
 * @since 0.1.0
 */
@ThreadSafe
class SyncCallTypeStrategy implements CallTypeStrategy {

	/**
	 * 实例
	 *
	 * @since 0.1.0
	 */
	private static final CallTypeStrategy INSTANCE = new SyncCallTypeStrategy();

	/**
	 * 获取实例
	 *
	 * @since 0.1.0
	 */
	static CallTypeStrategy getInstance() {
		return INSTANCE;
	}

	@Override
	public RpcResponse result(ServiceContext proxyContext, RpcRequest rpcRequest) {
		final String seqId = rpcRequest.seqId();
		return proxyContext.invokeManager().getResponse(seqId);
	}

}
