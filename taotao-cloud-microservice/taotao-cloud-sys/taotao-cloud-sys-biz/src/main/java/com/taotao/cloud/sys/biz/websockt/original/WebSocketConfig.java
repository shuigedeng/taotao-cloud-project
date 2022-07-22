package com.taotao.cloud.sys.biz.websockt.original;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 基于原生注解
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-11 09:08:11
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {

    //@Bean
    //public ServerEndpointExporter serverEndpoint() {
    //    return new ServerEndpointExporter();
    //}
}
