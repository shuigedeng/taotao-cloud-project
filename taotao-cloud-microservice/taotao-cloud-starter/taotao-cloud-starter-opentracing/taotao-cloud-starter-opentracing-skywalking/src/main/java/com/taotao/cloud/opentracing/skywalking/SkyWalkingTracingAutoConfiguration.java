package com.taotao.cloud.opentracing.skywalking;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.opentracing.skywalking.config.TraceInterceptorConfigurer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@AutoConfiguration
public class SkyWalkingTracingAutoConfiguration {

	@PostConstruct
	public void init() {
		LogUtils.info("Load Auto Configuration : {}", this.getClass().getName());
	}

	@Bean
	public ClusterTrace skyWalkingClusterTrace() {
		return new SkyWalkingClusterTrace();
	}


	@Bean
	public TraceInterceptorConfigurer traceInterceptorConfigurer(ClusterTrace clusterTrace) {
		return new TraceInterceptorConfigurer(clusterTrace);
	}

}
