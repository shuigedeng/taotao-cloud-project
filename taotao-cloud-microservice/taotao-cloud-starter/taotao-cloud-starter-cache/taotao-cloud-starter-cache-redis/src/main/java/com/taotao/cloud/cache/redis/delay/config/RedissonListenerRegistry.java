package com.taotao.cloud.cache.redis.delay.config;

import com.taotao.cloud.cache.redis.delay.listener.RedissonListenerContainer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;

/**
 * RedissonListenerRegistry
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:24:16
 */
public class RedissonListenerRegistry implements SmartLifecycle {

	private List<RedissonListenerContainer> listenerContainers = new ArrayList<>(8);

	public void registerListenerContainer(RedissonListenerContainer listenerContainer) {
		this.listenerContainers.add(listenerContainer);
	}

	@PreDestroy
	public void destroy() {
		this.stop();
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		this.listenerContainers.forEach(listenerContainer -> listenerContainer.stop(callback));
	}

	@Override
	public void start() {
		this.listenerContainers.forEach(Lifecycle::start);
	}

	@Override
	public void stop() {
		this.listenerContainers.forEach(Lifecycle::stop);
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}
}
