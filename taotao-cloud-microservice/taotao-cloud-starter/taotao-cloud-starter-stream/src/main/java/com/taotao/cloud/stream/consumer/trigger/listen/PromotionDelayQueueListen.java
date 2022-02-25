package com.taotao.cloud.stream.consumer.trigger.listen;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.stream.consumer.trigger.AbstractDelayQueueListen;
import com.taotao.cloud.stream.framework.trigger.enums.DelayQueueEnums;
import com.taotao.cloud.stream.framework.trigger.interfaces.TimeTrigger;
import com.taotao.cloud.stream.framework.trigger.model.TimeTriggerMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * PromotionTimeTriggerListen
 */
@Component
public class PromotionDelayQueueListen extends AbstractDelayQueueListen {

    @Autowired
    private TimeTrigger timeTrigger;

    @Override
    public void invoke(String jobId) {
        timeTrigger.execute(JSONUtil.toBean(jobId, TimeTriggerMsg.class));
    }


    @Override
    public String setDelayQueueName() {
        return DelayQueueEnums.PROMOTION.name();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.init();
    }
}
