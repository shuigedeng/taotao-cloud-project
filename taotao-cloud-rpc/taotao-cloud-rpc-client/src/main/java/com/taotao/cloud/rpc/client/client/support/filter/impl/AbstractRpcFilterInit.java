package com.taotao.cloud.rpc.client.client.support.filter.impl;

import com.taotao.cloud.rpc.client.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.client.support.filter.RpcFilter;
import com.taotao.cloud.rpc.common.common.rpc.DefaultPipeline;
import com.taotao.cloud.rpc.common.common.rpc.Pipeline;
import java.util.List;
import javax.annotation.concurrent.ThreadSafe;

/**
 * 什么都不做的过滤器
 *
 * @author shuigedeng
 * @since 0.2.0
 */
@ThreadSafe
public abstract class AbstractRpcFilterInit implements RpcFilter {

	/**
	 * 初始化监听器列表
	 *
	 * @param pipeline 泳道
	 * @param context  重试信息
	 * @since 2024.06
	 */
	protected abstract void init(final Pipeline<RpcFilter> pipeline,
		final RemoteInvokeContext context);

	@Override
	public void filter(RemoteInvokeContext conditionContext) {
		Pipeline<RpcFilter> pipeline = new DefaultPipeline<>();
		this.init(pipeline, conditionContext);

		List<RpcFilter> filterList = pipeline.list();

		for (RpcFilter filter : filterList) {
			filter.filter(conditionContext);
		}
	}

}
