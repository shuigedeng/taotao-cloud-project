package com.taotao.cloud.pulsar.model;

import com.taotao.cloud.common.utils.log.LogUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SizeUnit;

//初始化 Client--商用级别

/**
 * 商用级别的Pulsar Client新增了 5 个配置参数：
 *
 * <p>•ioThreads netty 的 ioThreads 负责网络 IO 操作，如果业务流量较大，可以调高ioThreads个数；</p>
 * <p>•listenersThreads 负责调用以listener模式启动的消费者的回调函数，建议配置大于该 client 负责的partition数目；</p>
 * <p>•memoryLimit 当前用于限制pulsar生产者可用的最大内存，可以很好地防止网络中断、Pulsar 故障等场景下，消息积压在producer侧，导致 Java 程序
 * OOM；</p>
 * <p>•operationTimeout 一些元数据操作的超时时间，Pulsar 默认为 30s，有些保守，可以根据自己的网络情况、处理性能来适当调低；</p>
 * <p>•connectionTimeout 连接 Pulsar 的超时时间，配置原则同上。</p>
 */
public class DemoPulsarClientInitUltimate {

	private static final DemoPulsarClientInitUltimate INSTANCE = new DemoPulsarClientInitUltimate();

	private volatile PulsarClient pulsarClient;

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
		new DefaultThreadFactory("pulsar-cli-init"));

	public static DemoPulsarClientInitUltimate getInstance() {
		return INSTANCE;
	}

	public void init() {
		executorService.scheduleWithFixedDelay(this::initWithRetry, 0, 10, TimeUnit.SECONDS);
	}

	private void initWithRetry() {
		try {
			pulsarClient = PulsarClient.builder()
				.serviceUrl(PulsarConstant.SERVICE_HTTP_URL)
				.ioThreads(4)
				.listenerThreads(10)
				.memoryLimit(64, SizeUnit.MEGA_BYTES)
				.operationTimeout(5, TimeUnit.SECONDS)
				.connectionTimeout(15, TimeUnit.SECONDS)
				.build();

			LogUtil.info("pulsar client init success");
			this.executorService.shutdown();
		} catch (Exception e) {
			LogUtil.error("init pulsar error, exception is ", e);
		}
	}

	public PulsarClient getPulsarClient() {
		return pulsarClient;
	}

}
