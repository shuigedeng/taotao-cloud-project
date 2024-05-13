package com.taotao.cloud.rpc.core.hook;


import com.taotao.cloud.rpc.common.factory.ThreadPoolFactory;
import com.taotao.cloud.rpc.common.idworker.utils.JRedisHelper;
import com.taotao.cloud.rpc.common.util.IpUtils;
import com.taotao.cloud.rpc.common.util.NacosUtils;
import com.taotao.cloud.rpc.core.net.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerShutdownHook {

	private static final ServerShutdownHook shutdownHook = new ServerShutdownHook();

	public static ServerShutdownHook getShutdownHook() {
		return shutdownHook;
	}

	/**
	 * 添加清除钩子
	 */
	public void addClearAllHook() {
		log.info("All services will be cancel after shutdown");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			JRedisHelper.remWorkerId(IpUtils.getPubIpAddr());
			log.info("the cache for workId has bean cleared successfully");
			NacosUtils.clearRegistry();
			NettyServer.shutdownAll();
			ThreadPoolFactory.shutdownAll();
		}));
	}


}
