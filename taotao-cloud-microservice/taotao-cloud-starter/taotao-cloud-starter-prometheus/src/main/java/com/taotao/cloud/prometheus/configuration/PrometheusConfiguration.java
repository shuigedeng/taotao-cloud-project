package com.taotao.cloud.prometheus.configuration;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Prometheus配置
 */
@AutoConfiguration
@ConditionalOnClass({MeterRegistry.class})
public class PrometheusConfiguration implements CommandLineRunner {

	@Value("${spring.application.name}")
	private String applicationName;

	@Bean
	MeterRegistryCustomizer<MeterRegistry> appMetricsCommonTags() {
		return registry -> registry.config().commonTags("application", applicationName);
	}

	@Bean(value = "meterRegistryCustomizer")
	public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
		return meterRegistry -> meterRegistry
			.config()
			.commonTags("application", PropertyUtil.getProperty(CommonConstant.SPRING_APP_NAME_KEY));
	}


	@Override
	public void run(String... args) {
		LogUtil.info("matecloud prometheus startup successfully! ");
	}
}
