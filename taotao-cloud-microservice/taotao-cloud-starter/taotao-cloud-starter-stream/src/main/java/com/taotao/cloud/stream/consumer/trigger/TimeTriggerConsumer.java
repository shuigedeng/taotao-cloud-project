package com.taotao.cloud.stream.consumer.trigger;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.stream.framework.trigger.model.TimeTriggerMsg;
import com.taotao.cloud.stream.framework.trigger.util.DelayQueueTools;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 事件触发消费者
 */
@Component
@RocketMQMessageListener(topic = "${lili.data.rocketmq.promotion-topic}", consumerGroup = "${lili.data.rocketmq.promotion-group}")
public class TimeTriggerConsumer implements RocketMQListener<TimeTriggerMsg> {
    @Autowired
    private RedisRepository redisRepository;

    @Override
    public void onMessage(TimeTriggerMsg timeTriggerMsg) {
        try {
            String key = DelayQueueTools.generateKey(timeTriggerMsg.getTriggerExecutor(), timeTriggerMsg.getTriggerTime(), timeTriggerMsg.getUniqueKey());

            if (redisRepository.get(key) == null) {
                LogUtil.info("执行器执行被取消：{} | 任务标识：{}", timeTriggerMsg.getTriggerExecutor(), timeTriggerMsg.getUniqueKey());
                return;
            }

	        LogUtil.info("执行器执行：" + timeTriggerMsg.getTriggerExecutor());
	        LogUtil.info("执行器参数：" + JSONUtil.toJsonStr(timeTriggerMsg.getParam()));

	        redisRepository.del(key);

            TimeTriggerExecutor executor = (TimeTriggerExecutor) ContextUtil.getBean(timeTriggerMsg.getTriggerExecutor(), true);
            executor.execute(timeTriggerMsg.getParam());
        } catch (Exception e) {
	        LogUtil.error("mq延时任务异常", e);
        }

    }

}
