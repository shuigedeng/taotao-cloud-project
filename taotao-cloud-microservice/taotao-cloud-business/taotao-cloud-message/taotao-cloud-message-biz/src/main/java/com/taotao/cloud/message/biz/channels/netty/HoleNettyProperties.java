package com.taotao.cloud.message.biz.channels.netty;

import lombok.Data;
import lombok.experimental.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * HoleNettyProperties
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@ConfigurationProperties(prefix = "netty")
@Data
@Configuration
public class HoleNettyProperties {

    /**
     * boss线程数量 默认为cpu线程数*2
     */
    private Integer boss;
    /**
     * worker线程数量 默认为cpu线程数*2
     */
    private Integer worker;
    /**
     * 连接超时时间 默认为30s
     */
    private Integer timeout = 30000;
    /**
     * 服务器主端口 默认9000
     */
    private Integer port = 9000;

    private String host = "127.0.0.1";
}

