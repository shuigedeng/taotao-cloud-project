package com.taotao.cloud.rpc.client.support.fail.impl;

import com.taotao.cloud.rpc.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.support.fail.FailStrategy;
import com.taotao.cloud.rpc.common.common.exception.RpcRuntimeException;
import com.taotao.cloud.rpc.common.common.exception.RpcTimeoutException;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.RpcResponses;

import javax.annotation.concurrent.ThreadSafe;

/**
 * 如果调用遇到异常，则进行尝试其他 server 端进行调用。 （1）最大重试次数=2  不能太多次 （2）重试的时候如何标识重试次数还剩多少次？ （3）如何在失败的时候获取重试相关上下文？
 *
 * @author shuigedeng
 * @since 0.1.1
 */
@ThreadSafe
class FailOverStrategy implements FailStrategy {

	@Override
	public Object fail(final RemoteInvokeContext context) {
		try {
			final Class returnType = context.request().returnType();
			final RpcResponse rpcResponse = context.rpcResponse();
			return RpcResponses.getResult(rpcResponse, returnType);
		}
		catch (Exception e) {
			Throwable throwable = e.getCause();
			if (throwable instanceof RpcTimeoutException) {
				throw new RpcRuntimeException();
			}

			// 进行失败重试。
			int retryTimes = context.retryTimes();
			if (retryTimes > 0) {
				// 进行重试
				retryTimes--;
				context.retryTimes(retryTimes);
				return context.remoteInvokeService()
					.remoteInvoke(context);
			}
			else {
				throw e;
			}
		}
	}

}
