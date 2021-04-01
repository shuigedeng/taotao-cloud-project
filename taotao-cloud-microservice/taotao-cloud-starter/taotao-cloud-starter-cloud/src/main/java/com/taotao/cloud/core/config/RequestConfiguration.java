package com.taotao.cloud.core.config;

import com.taotao.cloud.core.filter.TenantContextHolderFilter;
import com.taotao.cloud.core.filter.TraceFilter;
import com.taotao.cloud.core.props.MateRequestProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 请求配置，包括tracId和其他网络请求
 */
@Configuration
@EnableConfigurationProperties(MateRequestProperties.class)
public class RequestConfiguration {

	@Bean
	public TenantContextHolderFilter tenantContextHolderFilter() {
		return new TenantContextHolderFilter();
	}

	@Bean
	public TraceFilter traceFilter() {
		return new TraceFilter();
	}
}
