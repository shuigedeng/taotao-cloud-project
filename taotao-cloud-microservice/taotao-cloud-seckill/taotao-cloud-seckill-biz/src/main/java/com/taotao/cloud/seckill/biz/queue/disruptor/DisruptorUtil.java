package com.taotao.cloud.seckill.biz.queue.disruptor;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
/**
 * 来自<tukangzheng>的建议，具体性能待测试
 * 创建者 张志朋
 * 创建时间	2018年5月23日
 *
 */
public class DisruptorUtil {
	
	static Disruptor<SeckillEvent> disruptor;
	static{
		SeckillEventFactory factory = new SeckillEventFactory();
		int ringBufferSize = 1024;
		ThreadFactory threadFactory = runnable -> new Thread(runnable);
		disruptor = new Disruptor<>(factory, ringBufferSize, threadFactory);
		disruptor.handleEventsWith(new SeckillEventConsumer());
		disruptor.start();
	}
	
	public static void producer(SeckillEvent kill){
		RingBuffer<SeckillEvent> ringBuffer = disruptor.getRingBuffer();
		SeckillEventProducer producer = new SeckillEventProducer(ringBuffer);
		producer.seckill(kill.getSeckillId(),kill.getUserId());
	}
}
