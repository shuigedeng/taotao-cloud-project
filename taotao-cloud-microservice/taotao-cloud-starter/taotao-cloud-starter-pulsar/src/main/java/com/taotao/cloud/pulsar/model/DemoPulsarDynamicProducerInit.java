package com.taotao.cloud.pulsar.model;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.taotao.cloud.common.utils.log.LogUtil;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerBuilder;

/**
 * 动态生成销毁的 producer 示例
 * <p>
 * 还有一些业务，我们的 producer 可能会根据业务来进行动态的启动或销毁，如接收道路上车辆的数据并发送给指定的 topic。我们不会让内存里面驻留所有的
 * producer，这会导致占用大量的内存，我们可以采用类似于 LRU Cache 的方式来管理 producer 的生命周期
 */
public class DemoPulsarDynamicProducerInit {

	/**
	 * topic -- producer
	 */
	private final AsyncLoadingCache<String, Producer<byte[]>> producerCache;

	public DemoPulsarDynamicProducerInit() {
		this.producerCache = Caffeine.newBuilder()
			.expireAfterAccess(600, TimeUnit.SECONDS)
			.maximumSize(3000)
			.removalListener((RemovalListener<String, Producer<byte[]>>) (topic, value, cause) -> {
				LogUtil.info("topic {} cache removed, because of {}", topic, cause);
				if (value == null) {
					return;
				}

				try {
					value.close();
				} catch (Exception e) {
					LogUtil.error("close failed, ", e);
				}
			})
			.buildAsync(new AsyncCacheLoader<>() {
				@Override
				public CompletableFuture<Producer<byte[]>> asyncLoad(String topic,
					Executor executor) {
					return acquireFuture(topic);
				}

				@Override
				public CompletableFuture<Producer<byte[]>> asyncReload(String topic,
					Producer<byte[]> oldValue,
					Executor executor) {
					return acquireFuture(topic);
				}
			});
	}

	private CompletableFuture<Producer<byte[]>> acquireFuture(String topic) {
		CompletableFuture<Producer<byte[]>> future = new CompletableFuture<>();
		try {
			ProducerBuilder<byte[]> builder = DemoPulsarClientInit.getInstance().getPulsarClient()
				.newProducer().enableBatching(true);
			final Producer<byte[]> producer = builder.topic(topic).create();
			future.complete(producer);
		} catch (Exception e) {
			LogUtil.error("create producer exception ", e);
			future.completeExceptionally(e);
		}
		return future;
	}

	public void sendMsg(String topic, byte[] msg) {
		final CompletableFuture<Producer<byte[]>> cacheFuture = producerCache.get(topic);
		cacheFuture.whenComplete((producer, e) -> {
			if (e != null) {
				LogUtil.error("create pulsar client exception ", e);
				return;
			}
			try {
				producer.sendAsync(msg).whenComplete(((messageId, throwable) -> {
					if (throwable == null) {
						LogUtil.info("topic {} send success, msg id is {}", topic, messageId);
						return;
					}
					LogUtil.error("send producer msg error ", throwable);
				}));
			} catch (Exception ex) {
				LogUtil.error("send async failed ", ex);
			}
		});
	}

	private final Timer timer = new HashedWheelTimer();

	public void sendMsgWithRetry(String topic, byte[] msg, int retryTimes, int maxRetryTimes) {
		final CompletableFuture<Producer<byte[]>> cacheFuture = producerCache.get(topic);
		cacheFuture.whenComplete((producer, e) -> {
			if (e != null) {
				LogUtil.error("create pulsar client exception ", e);
				return;
			}

			try {
				producer.sendAsync(msg).whenComplete(((messageId, throwable) -> {
					if (throwable == null) {
						LogUtil.info("topic {} send success, msg id is {}", topic, messageId);
						return;
					}
					if (retryTimes < maxRetryTimes) {
						LogUtil.warn("topic {} send failed, begin to retry {} times exception is ",
							topic, retryTimes, throwable);
						timer.newTimeout(
							timeout -> DemoPulsarDynamicProducerInit.this.sendMsgWithRetry(topic,
								msg, retryTimes + 1, maxRetryTimes), 1L << retryTimes,
							TimeUnit.SECONDS);
					}
					LogUtil.error("send producer msg error ", throwable);
				}));
			} catch (Exception ex) {
				LogUtil.error("send async failed ", ex);
			}
		});
	}

}
