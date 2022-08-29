package com.taotao.cloud.redis.redisson.handle.impl;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.redis.redisson.handle.RedisDelayQueueHandle;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 订单超时未评价处理类
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-30 09:09:02
 */
@Component
public class OrderTimeoutNotEvaluated implements RedisDelayQueueHandle {

	@Override
	public void execute(Object obj) {
		if (obj instanceof Map map) {
			// TODO 订单超时未评价，系统默认好评处理业务...
			Long now = System.currentTimeMillis();
			Long timestamp = Long.valueOf(String.valueOf(map.get("timestamp")));
			Long delayTime = now - timestamp;
			Long random = Long.valueOf(String.valueOf(map.get("random")));
			Long diffTime = delayTime - random * 1000;
			LogUtils.info("(OrderTimeoutNotEvaluated) orderId：{}, 预计延迟时间：{} 秒，实际延迟时间：{} 毫秒，相差：{} 毫秒",
				map.get("orderId"), random, delayTime, diffTime);
		}

	}
}
