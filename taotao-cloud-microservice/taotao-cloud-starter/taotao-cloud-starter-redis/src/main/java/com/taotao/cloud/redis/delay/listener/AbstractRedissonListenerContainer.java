package com.taotao.cloud.redis.delay.listener;

import com.taotao.cloud.redis.delay.support.ThreadFactoryCreator;
import org.openjdk.nashorn.internal.objects.annotations.Getter;
import org.openjdk.nashorn.internal.objects.annotations.Setter;
import org.redisson.api.RedissonClient;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.Assert;

import java.util.concurrent.Executor;


public abstract class AbstractRedissonListenerContainer implements RedissonListenerContainer {

    private final Object lifecycleMonitor = new Object();

    private Executor taskExecutor = new SimpleAsyncTaskExecutor(ThreadFactoryCreator.create("RedissonConsumeThread"));

	public Executor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(Executor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	private final ContainerProperties containerProperties;

    private RedissonMessageListener<?> redissonListener;

	public RedissonMessageListener<?> getRedissonListener() {
		return redissonListener;
	}

    private RedissonClient redissonClient;

	public RedissonClient getRedissonClient() {
		return redissonClient;
	}

    private boolean autoStartup = true;

	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

    private int phase = Integer.MAX_VALUE;

	public void setPhase(int phase) {
		this.phase = phase;
	}

	private volatile boolean running = false;

    public AbstractRedissonListenerContainer(ContainerProperties containerProperties) {
        Assert.notNull(containerProperties, "ContainerProperties must not be null");
        this.containerProperties = containerProperties;
    }

    @Override
    public boolean isAutoStartup() {
        return this.autoStartup;
    }

    @Override
    public void stop(Runnable callback) {
        try {
            this.stop();
        } finally {
            callback.run();
        }
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        synchronized (this.lifecycleMonitor) {
            this.doStart();
            this.running = true;
        }
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        synchronized (this.lifecycleMonitor) {
            this.doStop();
            this.running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public int getPhase() {
        return this.phase;
    }

    @Override
    public ContainerProperties getContainerProperties() {
        return this.containerProperties;
    }

    @Override
    public void setListener(RedissonMessageListener<?> listener) {
        Assert.notNull(listener, "RedissonMessageListener must not be null");
        this.redissonListener = listener;
    }

    @Override
    public void setRedissonClient(RedissonClient redissonClient) {
        Assert.notNull(redissonClient, "RedissonClient must not be null");
        this.redissonClient = redissonClient;
    }

    /**
     * do start
     */
    protected abstract void doStart();

    /**
     * do stop
     */
    protected abstract void doStop();


    protected enum ConsumerStatus {
        /**
         * created
         */
        CREATED,
        /**
         * running
         */
        RUNNING,
        /**
         * stopped
         */
        STOPPED
    }

}
