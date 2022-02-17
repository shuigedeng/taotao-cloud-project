package com.taotao.cloud.redis.delay.listener;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.delay.message.FastJsonCodec;
import com.taotao.cloud.redis.delay.message.RedissonMessage;
import java.util.Objects;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RFuture;
import org.redisson.client.RedisException;
import org.redisson.client.protocol.RedisCommand;
import org.redisson.client.protocol.decoder.ListObjectDecoder;
import org.redisson.command.CommandAsyncExecutor;


public class SimpleRedissonListenerContainer extends AbstractRedissonListenerContainer {

	private RedisCommand<Object> LPOP_VALUE = new RedisCommand<>("LPOP",
		new ListObjectDecoder<>(1));

	public SimpleRedissonListenerContainer(ContainerProperties containerProperties) {
		super(containerProperties);
	}

	private AsyncMessageProcessingConsumer takeMessageTask;

	@Override
	protected void doStart() {
		this.takeMessageTask = new AsyncMessageProcessingConsumer();
		this.getTaskExecutor().execute(this.takeMessageTask);
	}

	@Override
	protected void doStop() {
		this.takeMessageTask.stop();
	}

	private final class AsyncMessageProcessingConsumer implements Runnable {

		private volatile Thread currentThread = null;

		private volatile ConsumerStatus status = ConsumerStatus.CREATED;

		@Override
		public void run() {
			if (this.status != ConsumerStatus.CREATED) {
				LogUtil.info(
					"consumer currentThread [{}] will exit, because consumer status is {},expected is CREATED",
					this.currentThread.getName(), this.status);
				return;
			}
			final String queue = SimpleRedissonListenerContainer.this.getContainerProperties()
				.getQueue();
			final Redisson redisson = (Redisson) SimpleRedissonListenerContainer.this.getRedissonClient();
			final RBlockingQueue<Object> blockingQueue = redisson.getBlockingQueue(queue,
				FastJsonCodec.INSTANCE);
			if (blockingQueue == null) {
				LogUtil.error("error occurred while create blockingQueue for queue [{}]", queue);
				return;
			}
			CommandAsyncExecutor commandExecutor = redisson.getCommandExecutor();
			this.currentThread = Thread.currentThread();
			this.status = ConsumerStatus.RUNNING;
			final long maxWaitMillis = 100;
			long emptyFetchTimes = 0;
			for (; ; ) {
				try {
					RFuture<RedissonMessage> asyncResult = commandExecutor.writeAsync(
						blockingQueue.getName(), blockingQueue.getCodec(), LPOP_VALUE,
						blockingQueue.getName());
					RedissonMessage redissonMessage = commandExecutor.get(asyncResult);
					if (Objects.isNull(redissonMessage)) {
						Thread.sleep(Math.min(++emptyFetchTimes * 5, maxWaitMillis));
					} else {
						//reset counting
						emptyFetchTimes = 0;
						SimpleRedissonMessageListenerAdapter redissonListener = (SimpleRedissonMessageListenerAdapter) SimpleRedissonListenerContainer.this.getRedissonListener();
						redissonListener.onMessage(redissonMessage);
					}
				} catch (InterruptedException | RedisException e) {
					//ignore
				} catch (Exception e) {
					LogUtil.error("error occurred while take message from redisson", e);
				}
				if (this.status == ConsumerStatus.STOPPED) {
					LogUtil.info("consumer currentThread [{}] will exit, because of STOPPED status",
						this.currentThread.getName());
					break;
				}
			}
			this.currentThread = null;
		}

		private void stop() {
			if (this.currentThread != null) {
				this.status = ConsumerStatus.STOPPED;
				this.currentThread.interrupt();
			}
		}
	}

}
