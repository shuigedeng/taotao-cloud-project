package com.taotao.cloud.mq.consistency.ttc.spring.host;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 宿主侧胶水装配 (2026-10-03 自 z-opc main-starter 平移):
 * MqProxyController (/api/mq/** 自省面) + ZMqEmbeddedServerConfig (内嵌 namesrv/broker spawn)
 * + ZMqTopicBootstrap (启动后 topic 预创建).
 *
 * <p>跟随 z.mq.host.enabled 开关 (默认关): 寄生 all-in-one 模式由宿主打开,
 * standalone 分布式模式 (z-mq 独立容器) 不开. ZmqAutoConfiguration (client) 不受影响.
 */
@Configuration
@ConditionalOnProperty(prefix = "z.mq.host", name = "enabled", havingValue = "true", matchIfMissing = false)
@ComponentScan(basePackages = "com.taotao.cloud.mq.consistency.ttc.spring.host")
public class MqHostAutoConfiguration {
}
