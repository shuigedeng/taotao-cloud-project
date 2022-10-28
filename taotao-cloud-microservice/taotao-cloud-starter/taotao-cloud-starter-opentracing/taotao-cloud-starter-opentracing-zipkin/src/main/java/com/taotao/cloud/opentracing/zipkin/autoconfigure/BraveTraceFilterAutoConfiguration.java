package com.taotao.cloud.opentracing.zipkin.autoconfigure;

import brave.dubbo.TracingFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = "dubbo",
	name = {"enabled"},
	matchIfMissing = true
)
@AutoConfigureAfter(DubboAutoConfiguration.class)
@ConditionalOnClass(TracingFilter.class)
@ConditionalOnBean({ProviderConfig.class, ConsumerConfig.class})
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class BraveTraceFilterAutoConfiguration implements InitializingBean {

	public static final String TRACING = "tracing";
	public static final String ADD_TRACING_FILTER = "Initializing providerConfig and consumerConfig add tracing filter";

	private final ProviderConfig providerConfig;

	private final ConsumerConfig consumerConfig;

	@Override
	public void afterPropertiesSet() {
		log.debug(ADD_TRACING_FILTER);
		providerConfig.setFilter(TRACING);
		consumerConfig.setFilter(TRACING);
	}
}
