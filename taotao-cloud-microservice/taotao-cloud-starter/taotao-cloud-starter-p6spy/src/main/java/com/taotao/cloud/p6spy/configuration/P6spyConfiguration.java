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
package com.taotao.cloud.p6spy.configuration;

import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.P6SpyDriver;
import com.p6spy.engine.spy.P6SpyOptions;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.p6spy.properties.P6spyProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * P6spyAutoConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/14 09:18
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(P6SpyDriver.class)
@EnableConfigurationProperties({P6spyProperties.class})
@ConditionalOnProperty(prefix = P6spyProperties.PREFIX, name = "enabled", havingValue = "true")
public class P6spyConfiguration implements ApplicationRunner , InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(P6spyConfiguration.class, StarterName.P6SPY_STARTER);
	}

	@Autowired
	private P6spyProperties p6spyProperties;

	@Override
	public void run(ApplicationArguments args) {
		Map<String, String> defaults = P6SpyOptions.getActiveInstance().getDefaults();
		Map<String, String> options = new HashMap<>();
		defaults.forEach((k,v)->{
			Object value = ReflectionUtil.tryGetValue(p6spyProperties, k);
			if(Objects.nonNull(value)){
				options.put(k, value.toString());
			}else {
				options.put(k,v);
			}
		});

		P6SpyOptions.getActiveInstance().load(options);
		P6ModuleManager.getInstance().reload();
	}
}
