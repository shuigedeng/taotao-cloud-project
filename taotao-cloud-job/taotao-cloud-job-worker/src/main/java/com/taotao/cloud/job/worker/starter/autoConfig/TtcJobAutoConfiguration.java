package com.taotao.cloud.job.worker.starter.autoConfig;

//import org.kjob.common.utils.NetUtils;

import com.taotao.cloud.job.worker.TtcJobSpringWorker;
import com.taotao.cloud.job.worker.common.TtcJobWorkerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(TtcJobProperties.class)
@ConditionalOnProperty(prefix = "ttcjob.worker", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TtcJobAutoConfiguration {

	@Autowired
	TtcJobProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public TtcJobSpringWorker initKJobWorker() {

		TtcJobProperties.Worker worker = properties.getWorker();

		List<String> serverAddress = Arrays.asList(worker.getServerAddress().split(","));

		TtcJobWorkerConfig config = new TtcJobWorkerConfig();

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
		return new TtcJobSpringWorker(config);
	}


}
