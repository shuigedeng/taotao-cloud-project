package com.taotao.cloud.im.biz.platform.modules.chat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取高德地图相关配置
 */
@Component
@Data
public class AmapConfig {

    @Value("${amap.key}")
    private String key;

}
