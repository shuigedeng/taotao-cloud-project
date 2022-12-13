package com.taotao.cloud.tracing.micrometer.autoconfigure;

import brave.dubbo.TracingFilter;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义 Brave TraceFilter 自动装配
 */
@ConditionalOnProperty(prefix = "dubbo", name = {"enabled"}, matchIfMissing = true)
@AutoConfigureAfter(DubboAutoConfiguration.class)
@ConditionalOnClass(TracingFilter.class)
@ConditionalOnBean({ProviderConfig.class, ConsumerConfig.class})
@Configuration(proxyBeanMethods = false)
public class BraveDubboAutoConfiguration implements InitializingBean {

	public static final String TRACING = "tracing";
	public static final String ADD_TRACING_FILTER = "Initializing providerConfig and consumerConfig add tracing filter";

	private final ProviderConfig providerConfig;
	private final ConsumerConfig consumerConfig;

	public BraveDubboAutoConfiguration(ProviderConfig providerConfig,
		ConsumerConfig consumerConfig) {
		this.providerConfig = providerConfig;
		this.consumerConfig = consumerConfig;
	}

	@Override
	public void afterPropertiesSet() {
		LogUtils.started(BraveDubboAutoConfiguration.class,
			StarterName.TRACING_MICROMETER_STARTER);
		providerConfig.setFilter(TRACING);
		consumerConfig.setFilter(TRACING);
	}
}
