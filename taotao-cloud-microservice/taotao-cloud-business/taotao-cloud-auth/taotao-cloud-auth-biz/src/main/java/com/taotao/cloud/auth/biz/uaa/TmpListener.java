package com.taotao.cloud.auth.biz.uaa;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TmpListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
         if (event instanceof ApplicationFailedEvent applicationFailedEvent) {
             applicationFailedEvent.getException().printStackTrace();
             LogUtils.error("启动失败TmpListener ----------------- TmpListener",  applicationFailedEvent.getException());
        }
    }
}
