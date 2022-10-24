package com.taotao.cloud.cache.redis.delay.listener;

import com.taotao.cloud.cache.redis.delay.consts.ListenerType;
import org.springframework.context.Lifecycle;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * ConcurrentRedissonListenerContainer 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public class ConcurrentRedissonListenerContainer extends AbstractRedissonListenerContainer {

    private final int concurrency;

	public int getConcurrency() {
		return concurrency;
	}

	private List<RedissonListenerContainer> containers = new ArrayList<>();

    private RedissonListenerContainerFactory containerFactory = new RedissonListenerContainerFactoryAdapter();

    public ConcurrentRedissonListenerContainer(ContainerProperties containerProperties, int concurrency) {
        super(containerProperties);
        Assert.isTrue(concurrency > 0, "concurrency must be greater than 0");
        this.concurrency = concurrency;
    }

    @Override
    protected void doStart() {
        for (int i = 0; i < this.concurrency; i++) {
            RedissonListenerContainer container = containerFactory.createListenerContainer(this.getContainerProperties());
            container.setRedissonClient(this.getRedissonClient());
            container.setListener(this.getRedissonListener());
            container.start();
            this.containers.add(container);
        }
    }

    @Override
    protected void doStop() {
        this.containers.forEach(Lifecycle::stop);
        this.containers.clear();
    }

    private static class RedissonListenerContainerFactoryAdapter implements RedissonListenerContainerFactory {
        @Override
        public RedissonListenerContainer createListenerContainer(ContainerProperties containerProperties) {
            ListenerType listenerType = containerProperties.getListenerType();
            if (listenerType == ListenerType.BATCH) {
                return new BatchRedissonListenerContainer(containerProperties, containerProperties.getMaxFetch());
            }
            return new SimpleRedissonListenerContainer(containerProperties);
        }
    }

}
