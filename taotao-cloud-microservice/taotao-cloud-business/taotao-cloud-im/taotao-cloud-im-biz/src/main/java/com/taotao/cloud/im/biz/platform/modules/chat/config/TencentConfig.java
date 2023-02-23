package com.taotao.cloud.im.biz.platform.modules.chat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 腾讯nlp配置
 */
@Component
@Data
public class TencentConfig {

    @Value("${tencent.appId}")
    private String appId;

    @Value("${tencent.appKey}")
    private String appKey;

    @Value("${tencent.appSecret}")
    private String appSecret;

}
