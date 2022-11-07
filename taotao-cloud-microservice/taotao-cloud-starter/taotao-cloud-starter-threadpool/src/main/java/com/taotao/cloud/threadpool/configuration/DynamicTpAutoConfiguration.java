package com.taotao.cloud.threadpool.configuration;

import com.dtp.common.em.QueueTypeEnum;
import com.dtp.core.spring.EnableDynamicTp;
import com.dtp.core.support.DynamicTp;
import com.dtp.core.support.ThreadPoolBuilder;
import com.dtp.core.support.ThreadPoolCreator;
import com.dtp.core.thread.DtpExecutor;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.threadpool.properties.ThreadPoolProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DynamicTpAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-25 09:41:50
 */
@AutoConfiguration
@EnableDynamicTp
@ConditionalOnProperty(prefix = ThreadPoolProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(ThreadPoolProperties.class)
//@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:dynamic-tp.yml")
public class DynamicTpAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(DynamicTpAutoConfiguration.class, StarterName.THREADPOOL_STARTER);
	}

	/**
	 * 通过{@link DynamicTp} 注解定义普通juc线程池，会享受到该框架监控功能，注解名称优先级高于方法名
	 *
	 * @return 线程池实例
	 */
	@DynamicTp("commonExecutor")
	@Bean
	public ThreadPoolExecutor commonExecutor() {
		return (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
	}

	/**
	 * 通过{@link ThreadPoolCreator} 快速创建一些简单配置的动态线程池
	 * tips: 建议直接在配置中心配置就行，不用@Bean声明
	 *
	 * @return 线程池实例
	 */
	@Bean
	public DtpExecutor dtpExecutor1() {
		return ThreadPoolCreator.createDynamicFast("dtpExecutor1");
	}

	/**
	 * 通过{@link ThreadPoolBuilder} 设置详细参数创建动态线程池（推荐方式），
	 * ioIntensive，参考tomcat线程池设计，实现了处理io密集型任务的线程池，具体参数可以看代码注释
	 * <p>
	 * tips: 建议直接在配置中心配置就行，不用@Bean声明
	 *
	 * @return 线程池实例
	 */
	@Bean
	public DtpExecutor ioIntensiveExecutor() {
		return ThreadPoolBuilder.newBuilder()
			.threadPoolName("ioIntensiveExecutor")
			.corePoolSize(20)
			.maximumPoolSize(50)
			.queueCapacity(2048)
			.ioIntensive(true)
			.buildDynamic();
	}

	/**
	 * tips: 建议直接在配置中心配置就行，不用@Bean声明
	 *
	 * @return 线程池实例
	 */
	@Bean
	public ThreadPoolExecutor dtpExecutor2() {
		return ThreadPoolBuilder.newBuilder()
			.threadPoolName("dtpExecutor2")
			.corePoolSize(10)
			.maximumPoolSize(15)
			.keepAliveTime(50)
			.timeUnit(TimeUnit.MILLISECONDS)
			.workQueue(QueueTypeEnum.SYNCHRONOUS_QUEUE.getName(), null, false)
			.waitForTasksToCompleteOnShutdown(true)
			.awaitTerminationSeconds(5)
			.buildDynamic();
	}

}
