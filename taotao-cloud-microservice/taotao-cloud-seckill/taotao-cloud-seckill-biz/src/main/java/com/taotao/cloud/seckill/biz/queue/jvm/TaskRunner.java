package com.taotao.cloud.seckill.biz.queue.jvm;

import com.taotao.cloud.seckill.biz.common.entity.SuccessKilled;
import com.taotao.cloud.seckill.biz.service.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 消费秒杀队列
 * 创建者 科帮网
 * 创建时间	2018年4月3日
 */
@Component
public class TaskRunner implements ApplicationRunner{

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

    @Autowired
    private ISeckillService seckillService;

    @Override
    public void run(ApplicationArguments var){
        new Thread(() -> {
            LOGGER.info("提醒队列启动成功");
            while(true){
                try {
                    //进程内队列
                    SuccessKilled kill = SeckillQueue.getSkillQueue().consume();
                    if(kill!=null){
                        Result result =
                                seckillService.startSeckilAopLock(kill.getSeckillId(), kill.getUserId());
                        if(result.equals(Result.ok(SeckillStatEnum.SUCCESS))){
                            LOGGER.info("用户:{}{}",kill.getUserId(),"秒杀成功");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
