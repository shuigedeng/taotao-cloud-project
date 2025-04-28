package com.taotao.cloud.ccsr.client.starter;

import com.taotao.cloud.ccsr.client.client.CcsrClient;
import com.taotao.cloud.ccsr.client.option.GrpcOption;
import com.taotao.cloud.ccsr.common.config.CcsrConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Conditional(EnableCcsrCondition.class) // 核心控制
public class CcsrClientAutoConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "ccsr")
	public CcsrConfig ccsrConfig() {
		return new CcsrConfig();
	}


	@Bean
	@Scope("singleton") // 显式声明单例
	public CcsrService ccsrClient(CcsrConfig config) {
		GrpcOption option = new GrpcOption();
		// 初始化服务端集群地址
		option.initServers(config.getClusterAddress());
		// 构建ccsrClient
		CcsrClient ccsrClient = CcsrClient.builder(config.getNamespace(), option).build();
		return new CcsrService(ccsrClient);
	}

}
