package com.taotao.cloud.workflow.biz.common.listerner;

import com.taotao.cloud.workflow.biz.common.config.ConfigValueUtil;
import com.taotao.cloud.workflow.biz.common.util.RedisUtil;
import com.taotao.cloud.workflow.biz.common.util.context.SpringContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 */
public class WorkflowListener implements ApplicationListener<ContextRefreshedEvent> {

    private ConfigValueUtil configValueUtil;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        configValueUtil= SpringContext.getBean(ConfigValueUtil.class);
        if("false".equals(configValueUtil.getTestVersion())){
            RedisUtil redisUtil = SpringContext.getBean(RedisUtil.class);
            redisUtil.removeAll();
        }
    }
}
