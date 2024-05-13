package com.taotao.cloud.ttcrpc.core.hook;

import com.taotao.cloud.rpc.common.factory.ThreadPoolFactory;
import com.taotao.cloud.ttcrpc.core.net.netty.client.ChannelProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端清除钩子
 */

@Slf4j
public class ClientShutdownHook {

	private static final ClientShutdownHook shutdownHook = new ClientShutdownHook();

	public static ClientShutdownHook getShutdownHook() {
		return shutdownHook;
	}

	/**
	 * 添加清除钩子
	 */
	public void addClearAllHook() {
		log.info("All services will be cancel after shutdown");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			ChannelProvider.shutdownAll();
			ThreadPoolFactory.shutdownAll();
		}));
	}
}

