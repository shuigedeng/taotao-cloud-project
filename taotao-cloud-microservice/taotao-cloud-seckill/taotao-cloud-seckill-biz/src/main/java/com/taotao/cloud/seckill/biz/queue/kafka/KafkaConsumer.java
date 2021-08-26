package com.taotao.cloud.seckill.biz.queue.kafka;

import com.taotao.cloud.seckill.biz.common.redis.RedisUtil;
import com.taotao.cloud.seckill.biz.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 消费者 spring-kafka 2.0 + 依赖JDK8
 * @author 科帮网 By https://blog.52itstyle.com
 */
@Component
public class KafkaConsumer {
	@Autowired
	private ISeckillService seckillService;
	
	private static RedisUtil redisUtil = new RedisUtil();
    /**
     * 监听seckill主题,有消息就读取
     * @param message
     */
    @KafkaListener(topics = {"seckill"})
    public void receiveMessage(String message){
		/**
		 * 收到通道的消息之后执行秒杀操作
		 */
		String[] array = message.split(";");
    	if(redisUtil.getValue(array[0])==null){
    		Result result = seckillService.startSeckilAopLock(Long.parseLong(array[0]), Long.parseLong(array[1]));
			if(result.equals(Result.ok(SeckillStatEnum.SUCCESS))){
    			WebSocketServer.sendInfo(array[0], "秒杀成功");
    		}else{
    			WebSocketServer.sendInfo(array[0], "秒杀失败");
    			redisUtil.cacheValue(array[0], "ok");
    		}
    	}else{
    		WebSocketServer.sendInfo(array[0], "秒杀失败");
    	}
    }
}
