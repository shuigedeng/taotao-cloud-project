package com.taotao.cloud.wechat.biz.wechatpush.job;

import com.taotao.cloud.wechat.biz.wechatpush.service.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobWorker {
    /**
     * 要推送的用户openid
     */
    @Value("${target.openId}")
    private String openId;

    @Autowired
    Pusher pusherService;

    @Scheduled(cron = "0 30 7 * * ?")
    public void goodMorning(){
        pusherService.push(openId);
    }

}
