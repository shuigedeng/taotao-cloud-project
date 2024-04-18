package com.taotao.cloud.modulith;

import com.taotao.cloud.core.startup.StartupSpringApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaoTaoCloudModulithApplication {

	public static void main(String[] args) {
		new StartupSpringApplication(TaoTaoCloudModulithApplication.class)
			.setTtcBanner()
			.setTtcProfileIfNotExists("dev")
			.setTtcApplicationProperty("taotao-cloud-modulith")
			.setTtcAllowBeanDefinitionOverriding(true)
			.run(args);
	}

}
