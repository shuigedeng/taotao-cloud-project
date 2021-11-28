package com.taotao.cloud.netty.configuration;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.netty.properties.NettyWebsocketProperties;
import com.taotao.cloud.netty.standard.ServerEndpointExporter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({NettyWebsocketProperties.class})
@ConditionalOnMissingBean(ServerEndpointExporter.class)
@ConditionalOnProperty(prefix = NettyWebsocketProperties.PREFIX, name = "enabled", havingValue = "true")
public class NettyWebSocketAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(NettyWebSocketAutoConfiguration.class, StarterNameConstant.NETTY_WEBSOCKET_STARTER);
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
