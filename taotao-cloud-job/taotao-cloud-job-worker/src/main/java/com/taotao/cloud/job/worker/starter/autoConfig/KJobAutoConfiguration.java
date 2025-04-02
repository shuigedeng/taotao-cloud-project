package com.taotao.cloud.job.worker.starter.autoConfig;

//import org.kjob.common.utils.NetUtils;

import com.taotao.cloud.job.worker.KJobSpringWorker;
import com.taotao.cloud.job.worker.common.KJobWorkerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(KJobProperties.class)
@ConditionalOnProperty(prefix = "kjob.worker", name = "enabled", havingValue = "true", matchIfMissing = true)
public class KJobAutoConfiguration {

	@Autowired
	KJobProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public KJobSpringWorker initKJobWorker() {

		KJobProperties.Worker worker = properties.getWorker();

		List<String> serverAddress = Arrays.asList(worker.getServerAddress().split(","));

		KJobWorkerConfig config = new KJobWorkerConfig();

		if (worker.getPort() != null) {
			config.setPort(worker.getPort());
		}
		if (worker.getServerPort() != null) {
			config.setServerPort(worker.getServerPort());
		}

		config.setAppName(worker.getAppName());
		config.setServerAddress(serverAddress);
		config.setNameServerAddress(worker.getNameServerAddress());
		config.setMaxHeavyweightTaskNum(worker.getMaxHeavyweightTaskNum());
		config.setMaxLightweightTaskNum(worker.getMaxLightweightTaskNum());
		config.setHealthReportInterval(worker.getHealthReportInterval());
		return new KJobSpringWorker(config);
	}


}
