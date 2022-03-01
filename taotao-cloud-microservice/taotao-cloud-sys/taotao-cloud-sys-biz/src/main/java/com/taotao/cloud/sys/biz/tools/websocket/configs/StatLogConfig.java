package com.taotao.cloud.sys.biz.tools.websocket.configs;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

@Configuration
public class StatLogConfig implements InitializingBean {
    @Autowired
    private WebSocketMessageBrokerStats webSocketMessageBrokerStats;

    /**
     * websocket 日志统计信息输出时间间隔
     */
    private static long PERIOD = 24 * 60 * 60 * 1000;

    @Override
    public void afterPropertiesSet() throws Exception {
        webSocketMessageBrokerStats.setLoggingPeriod(PERIOD);
    }
}
