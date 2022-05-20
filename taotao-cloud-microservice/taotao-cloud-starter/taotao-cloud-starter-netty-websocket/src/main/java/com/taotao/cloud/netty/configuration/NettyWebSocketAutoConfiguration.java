package com.taotao.cloud.netty.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.netty.properties.NettyWebsocketProperties;
import com.taotao.cloud.netty.standard.ServerEndpointExporter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网状汽车配置网络套接字
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-20 17:39:22
 */
@AutoConfiguration
@EnableConfigurationProperties({NettyWebsocketProperties.class})
@ConditionalOnMissingBean(ServerEndpointExporter.class)
@ConditionalOnProperty(prefix = NettyWebsocketProperties.PREFIX, name = "enabled", havingValue = "true")
public class NettyWebSocketAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(NettyWebSocketAutoConfiguration.class, StarterName.NETTY_WEBSOCKET_STARTER);
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
