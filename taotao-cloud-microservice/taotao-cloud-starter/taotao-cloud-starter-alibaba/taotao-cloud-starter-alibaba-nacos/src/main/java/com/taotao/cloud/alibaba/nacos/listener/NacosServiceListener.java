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
package com.taotao.cloud.alibaba.nacos.listener;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * ConfigService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/04/06 11:20
 */
@AutoConfiguration
public class NacosServiceListener implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(NacosServiceListener.class, StarterName.CORE_STARTER);
	}

	@Configuration
	public static class StandardNacosServiceListener implements InitializingBean {

		@Autowired
		private NacosServiceManager nacosServiceManager;
		@Autowired
		private NacosDiscoveryProperties properties;
		@Autowired
		private NacosDiscoveryClient discoveryClient;

		@Override
		public void afterPropertiesSet() throws Exception {
			nacosServiceManager.getNamingService()
				.subscribe(properties.getService(), properties.getGroup(),
					Collections.singletonList(properties.getClusterName()), event -> {
						if (event instanceof NamingEvent) {
							List<Instance> instances = ((NamingEvent) event).getInstances();

							LogUtils.info("");

							//Optional instanceOptional = NacosWatch.this.selectCurrentInstance(instances);
							//instanceOptional.ifPresent((currentInstance) -> {
							//	NacosWatch.this.resetIfNeeded(currentInstance);
							//});
						}

					});

			List<String> services = discoveryClient.getServices();
			if (!services.isEmpty()) {
				for (String service : services) {
					nacosServiceManager.getNamingService()
						.subscribe(service,
							this.properties.getGroup(),
							List.of(this.properties.getClusterName()),
							event -> {
								if (event instanceof NamingEvent) {
									List<Instance> instances = ((NamingEvent) event).getInstances();

									LogUtils.info("");

									//Optional instanceOptional = NacosWatch.this.selectCurrentInstance(instances);
									//instanceOptional.ifPresent((currentInstance) -> {
									//	NacosWatch.this.resetIfNeeded(currentInstance);
									//});
								}

							});
				}
			}

		}
	}
}
