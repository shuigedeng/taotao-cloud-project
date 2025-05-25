package com.taotao.cloud.modulith;

import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.web.annotation.TaoTaoBootApplication;
import com.taotao.cloud.bootstrap.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@TaoTaoBootApplication
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
