package com.taotao.cloud.seckill.biz.queue.redis;

import com.taotao.cloud.seckill.biz.common.entity.Result;
import com.taotao.cloud.seckill.biz.common.enums.SeckillStatEnum;
import com.taotao.cloud.seckill.biz.common.redis.RedisUtil;
import com.taotao.cloud.seckill.biz.common.webSocket.WebSocketServer;
import com.taotao.cloud.seckill.biz.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消费者
 */
@Service
public class RedisConsumer {
	
	@Autowired
	private ISeckillService seckillService;
	@Autowired
	private RedisUtil redisUtil;
	
    public void receiveMessage(String message) {
		Thread th=Thread.currentThread();
		System.out.println("Tread name:"+th.getName());
        //收到通道的消息之后执行秒杀操作(超卖)
    	String[] array = message.split(";"); 
    	if(redisUtil.getValue(array[0])==null){//control层已经判断了，其实这里不需要再判断了
    		Result result = seckillService.startSeckilDBPCC_TWO(Long.parseLong(array[0]), Long.parseLong(array[1]));
    		if(result.equals(Result.ok(SeckillStatEnum.SUCCESS))){
    			WebSocketServer.sendInfo("秒杀成功",array[0]);//推送给前台
    		}else{
    			WebSocketServer.sendInfo("秒杀失败",array[0]);//推送给前台
    			redisUtil.cacheValue(array[0], "ok");//秒杀结束
    		}
    	}else{
    		WebSocketServer.sendInfo("秒杀失败",array[0]);//推送给前台
    	}
    }
}
