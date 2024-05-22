package com.taotao.cloud.mq.client.consumer.core;

import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeBo;
import com.taotao.cloud.mq.common.rpc.RpcChannelFuture;
import com.taotao.cloud.mq.common.tmp.ILoadBalance;

public class LoadBalances {

	public static ILoadBalance<RpcChannelFuture> weightRoundRobbin() {
		return null;
	}
}
