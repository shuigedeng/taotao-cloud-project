/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.server;

import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.cloud.job.server.common.config.TtcJobServerConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(TtcJobServerConfig.class)
public class JobServerApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JobServerApplication.class);
	}

	public static void main(String[] args) {
		new StartupSpringApplication(JobServerApplication.class)
			.setTtcBanner()
			.setTtcProfileIfNotExists("dev")
			.setTtcApplicationProperty("taotao-cloud-job-server")
			.setTtcAllowBeanDefinitionOverriding(true)
			.run(args);
	}

}
