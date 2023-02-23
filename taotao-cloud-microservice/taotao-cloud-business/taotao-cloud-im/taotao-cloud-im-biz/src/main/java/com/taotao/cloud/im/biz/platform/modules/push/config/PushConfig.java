package com.taotao.cloud.im.biz.platform.modules.push.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 推送配置
 */
@Component
@Data
public class PushConfig {

    @Value("${push.appId}")
    private String appId;

    @Value("${push.appKey}")
    private String appKey;

    @Value("${push.appSecret}")
    private String appSecret;

    @Value("${push.masterSecret}")
    private String masterSecret;

}
