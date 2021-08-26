package com.taotao.cloud.seckill.biz.queue.disruptor;

import com.itstyle.seckill.common.config.SpringUtil;
import com.itstyle.seckill.common.entity.Result;
import com.itstyle.seckill.common.enums.SeckillStatEnum;
import com.itstyle.seckill.queue.jvm.TaskRunner;
import com.itstyle.seckill.service.ISeckillService;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消费者(秒杀处理器)
 * 创建者 科帮网
 */
public class SeckillEventConsumer implements EventHandler<SeckillEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(SeckillEventConsumer.class);
	
	private ISeckillService seckillService = (ISeckillService) SpringUtil.getBean("seckillService");
	
	@Override
    public void onEvent(SeckillEvent seckillEvent, long seq, boolean bool) {
		Result result =
				seckillService.startSeckilAopLock(seckillEvent.getSeckillId(), seckillEvent.getUserId());
		if(result.equals(Result.ok(SeckillStatEnum.SUCCESS))){
			LOGGER.info("用户:{}{}",seckillEvent.getUserId(),"秒杀成功");
		}
	}
}
