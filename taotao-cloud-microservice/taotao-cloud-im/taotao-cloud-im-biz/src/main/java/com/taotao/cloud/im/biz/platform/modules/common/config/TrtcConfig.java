package com.taotao.cloud.im.biz.platform.modules.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取trtc相关配置
 */
@Component
@Data
public class TrtcConfig {

    @Value("${trtc.appId}")
    private String appId;
    @Value("${trtc.expire}")
    private String expire;
    @Value("${trtc.secret}")
    private String secret;

}
