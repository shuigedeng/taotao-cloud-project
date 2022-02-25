package com.taotao.cloud.core.configuration;

import com.dtp.common.em.QueueTypeEnum;
import com.dtp.common.em.RejectedTypeEnum;
import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.thread.ThreadPoolBuilder;
import com.taotao.cloud.common.factory.YamlPropertySourceFactory;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * DynamicTpAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-25 09:41:50
 */
@Configuration
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:dynamic-tp.yml")
public class DynamicTpAutoConfiguration {

	//@Bean
	//public DtpExecutor demo1Executor() {
	//	return DtpCreator.createDynamicFast("demo1-executor");
	//}
	//
	//@Bean
	//public ThreadPoolExecutor demo2Executor() {
	//	return ThreadPoolBuilder.newBuilder()
	//		.threadPoolName("demo2-executor")
	//		.corePoolSize(8)
	//		.maximumPoolSize(16)
	//		.keepAliveTime(50)
	//		.allowCoreThreadTimeOut(true)
	//		.workQueue(QueueTypeEnum.SYNCHRONOUS_QUEUE.getName(), null, false)
	//		.rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
	//		.buildDynamic();
	//}

	//public static void main(String[] args) {
	//	DtpExecutor dtpExecutor = DtpRegistry.getExecutor("dynamic-tp-test-1");
	//	dtpExecutor.execute(() -> System.out.println("test"));
	//}

}
