package com.taotao.cloud.mq.broker.core;

import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeBo;
import com.taotao.cloud.mq.common.tmp.ILoadBalance;

public class LoadBalances {

	public static ILoadBalance<ConsumerSubscribeBo> weightRoundRobbin() {
		return null;
	}
}
