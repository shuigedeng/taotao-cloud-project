package com.taotao.cloud.job.server.jobserver;

import com.taotao.cloud.server.common.config.TtcJobServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TtcJobServerConfig.class)
public class TtcJobServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtcJobServerApplication.class, args);
	}

}
