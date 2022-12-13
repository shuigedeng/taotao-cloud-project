package com.taotao.cloud.tracing.skywalking;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.tracing.skywalking.config.TraceInterceptorConfigurer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SkyWalkingTracingAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(SkyWalkingTracingAutoConfiguration.class,
			StarterName.TRACING_SKYWALKING_STARTER);
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
