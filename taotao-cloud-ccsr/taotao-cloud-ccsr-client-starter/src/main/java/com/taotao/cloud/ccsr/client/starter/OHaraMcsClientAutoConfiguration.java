package com.taotao.cloud.ccsr.client.starter;

import com.taotao.cloud.ccsr.client.client.OHaraMcsClient;
import com.taotao.cloud.ccsr.client.option.GrpcOption;
import com.taotao.cloud.ccsr.common.config.OHaraMcsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Conditional(EnableOHaraMcsCondition.class) // 核心控制
public class OHaraMcsClientAutoConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "ohara-mcs")
	public OHaraMcsConfig oHaraMcsConfig() {
		return new OHaraMcsConfig();
	}


	@Bean
	@Scope("singleton") // 显式声明单例
	public OHaraMcsService oHaraMcsClient(OHaraMcsConfig config) {
		GrpcOption option = new GrpcOption();
		// 初始化服务端集群地址
		option.initServers(config.getClusterAddress());
		// 构建OHaraMcsClient
		OHaraMcsClient mcsClient = OHaraMcsClient.builder(config.getNamespace(), option).build();
		return new OHaraMcsService(mcsClient);
	}

}
