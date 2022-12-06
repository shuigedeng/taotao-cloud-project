package com.taotao.cloud.tracing.skywalking;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.tracing.skywalking.config.TraceInterceptorConfigurer;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

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
