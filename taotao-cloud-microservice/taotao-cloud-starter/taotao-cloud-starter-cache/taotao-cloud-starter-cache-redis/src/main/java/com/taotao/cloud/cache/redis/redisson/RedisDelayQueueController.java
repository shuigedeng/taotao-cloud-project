//package com.taotao.cloud.redis.redisson;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 延迟队列测试
// */
//@RestController
//public class RedisDelayQueueController {
//
//	@Autowired
//	private RedisDelayQueue redisDelayQueue;
//
//
//	/**
//	 * 添加订单支付超时测试数据
//	 *
//	 * @param count 订单数量
//	 */
//	@PostMapping("/addQueue1")
//	public void addQueue1(Integer count) {
//		for (int i = 0; i < count; i++) {
//			Integer random = new Random().nextInt(300) + 1;
//			Map<String, String> map1 = new HashMap<>();
//			map1.put("orderId", String.valueOf(i));
//			map1.put("remark", "订单支付超时，自动取消订单");
//			map1.put("random", String.valueOf(random));
//			map1.put("timestamp", String.valueOf(System.currentTimeMillis()));
//			redisDelayQueue.addDelayQueue(map1, random, TimeUnit.SECONDS,
//				RedisDelayQueueEnum.ORDER_PAYMENT_TIMEOUT.getCode());
//		}
//	}
//
//	/**
//	 * 添加订单超时未评价测试数据
//	 *
//	 * @param count 订单数量
//	 */
//	@PostMapping("/addQueue2")
//	public void addQueue2(Integer count) {
//		for (int i = 0; i < count; i++) {
//			Integer random = new Random().nextInt(100) + 1;
//			Map<String, String> map1 = new HashMap<>();
//			map1.put("orderId", String.valueOf(i));
//			map1.put("remark", "订单超时未评价，系统默认好评");
//			map1.put("random", String.valueOf(random));
//			map1.put("timestamp", String.valueOf(System.currentTimeMillis()));
//			redisDelayQueue.addDelayQueue(map1, random, TimeUnit.SECONDS,
//				RedisDelayQueueEnum.ORDER_TIMEOUT_NOT_EVALUATED.getCode());
//		}
//	}
//}
