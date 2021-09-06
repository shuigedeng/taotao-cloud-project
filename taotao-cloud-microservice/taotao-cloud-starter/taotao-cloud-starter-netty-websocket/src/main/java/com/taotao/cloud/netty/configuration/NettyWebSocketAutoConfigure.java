package com.taotao.cloud.netty.configuration;


import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.netty.annotation.EnableWebSocket;
import org.springframework.beans.factory.InitializingBean;

@EnableWebSocket
public class NettyWebSocketAutoConfigure implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(NettyWebSocketAutoConfigure.class, StarterName.NETTY_WEBSOCKET_STARTER);
	}
}
