package com.taotao.cloud.opentracing.skywalking.config;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.opentracing.skywalking.ClusterTrace;
import com.taotao.cloud.opentracing.skywalking.interceptor.TraceInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnWebApplication
public class TraceInterceptorConfigurer implements WebMvcConfigurer {

	private final ClusterTrace clusterTrace;

	public TraceInterceptorConfigurer(ClusterTrace clusterTrace) {
		this.clusterTrace = clusterTrace;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LogUtils.info("Enabled SkyWalking Tracing...");
		registry.addInterceptor(new TraceInterceptor(clusterTrace)).addPathPatterns("/**").order(1);
	}
}
