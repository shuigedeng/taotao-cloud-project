package com.taotao.cloud.redis.redisson.handle.impl;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.redisson.handle.RedisDelayQueueHandle;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 订单超时未评价处理类
 * Created by LPB on 2021/04/20.
 */
@Component
public class OrderTimeoutNotEvaluated implements RedisDelayQueueHandle<Map> {
	@Override
	public void execute(Map map) {
		// TODO 订单超时未评价，系统默认好评处理业务...
		Long now = System.currentTimeMillis();
		Long timestamp = Long.valueOf(String.valueOf(map.get("timestamp")));
		Long delayTime = now - timestamp;
		Long random = Long.valueOf(String.valueOf(map.get("random")));
		Long diffTime = delayTime - random * 1000;
		LogUtil.info("(OrderTimeoutNotEvaluated) orderId：{}, 预计延迟时间：{} 秒，实际延迟时间：{} 毫秒，相差：{} 毫秒",
				map.get("orderId"), random, delayTime, diffTime);
	}
}
