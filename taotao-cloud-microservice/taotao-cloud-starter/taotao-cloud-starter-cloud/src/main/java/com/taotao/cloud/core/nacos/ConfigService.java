/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.core.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.listener.Listener;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.Properties;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * ConfigService
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/04/06 11:20
 */
@Configuration
public class ConfigService {

	@NacosConfigListener(dataId = "taotao-cloud", type = ConfigType.YAML)
	public void onReceived(Properties value) {
		LogUtil.info("onReceived(Properties) : {}", value);
	}


	@RefreshScope
	@Configuration
	public static class NacosListener implements InitializingBean {

		@Value("${spring.application.name}")
		private String appName;
		@Autowired
		private NacosConfigManager nacosConfigManager;
		@Autowired
		private NacosConfigProperties configProperties;

//    @NacosConfigListener(dataId = "${spring.application.name}.yaml")
//    public void onMessage(String config) {
//        System.out.println();
//    }

		@Override
		public void afterPropertiesSet() throws Exception {
			nacosConfigManager.getConfigService()
				.addListener(appName + ".yaml", configProperties.getGroup(),
					new Listener() {
						@Override
						public Executor getExecutor() {
							return null;
						}

						@Override
						public void receiveConfigInfo(String configInfo) {
							LogUtil.info(configInfo);
						}
					});
		}
	}
}
