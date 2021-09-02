/*
 * Copyright ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.model;


import static com.taotao.cloud.dingtalk.constant.DingerConstant.DINGER_PROPERTIES_PREFIX;
import static com.taotao.cloud.dingtalk.constant.DingerConstant.SPOT_SEPERATOR;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.RESOURCE_CONFIG_EXCEPTION;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.dingtalk.enums.DingerType;
import com.taotao.cloud.dingtalk.exception.DingerException;
import com.taotao.cloud.dingtalk.listeners.DingerListenersProperty;
import com.taotao.cloud.dingtalk.utils.DingerUtils;
import java.io.IOException;
import java.util.List;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Default DingerDefinition Resolver
 *
 * @author Jaemon
 * @since 1.2
 */
public class DefaultDingerDefinitionResolver extends DingerListenersProperty implements
	EnvironmentAware {

	private final DingerDefinitionResolver xmlDingerDefinitionResolver;
	private final DingerDefinitionResolver annotaDingerDefinitionResolver;
	private Environment environment;

	public DefaultDingerDefinitionResolver() {
		xmlDingerDefinitionResolver = new XmlDingerDefinitionResolver();
		annotaDingerDefinitionResolver = new AnnotationDingerDefinitionResolver();
	}

	/**
	 * 解析处理
	 *
	 * @param dingerClasses Dinger类集合
	 */
	protected void resolver(List<Class<?>> dingerClasses) {
		registerDefaultDingerConfig(environment);

		// deal with xml
		dingerXmlResolver();

		// deal with annotation
		annotaDingerDefinitionResolver.resolver(dingerClasses);
	}

	/**
	 * Xml定义Dinger解析处理
	 */
	protected void dingerXmlResolver() {
		String dingerLocationsProp = DINGER_PROPERTIES_PREFIX + "dinger-locations";
		String dingerLocations = environment.getProperty(dingerLocationsProp);
		if (dingerLocations == null) {
			LogUtil.debug("dinger xml is not configured.");
			return;
		}

		// 处理xml配置转为dingerDefinition
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources;
		try {
			resources = resolver.getResources(dingerLocations);
		} catch (IOException e) {
			throw new DingerException(RESOURCE_CONFIG_EXCEPTION, dingerLocations);
		}
		if (resources.length == 0) {
			LogUtil.warn("dinger xml is empty under {}.", dingerLocations);
			return;
		}

		xmlDingerDefinitionResolver.resolver(resources);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * 注册默认的Dinger机器人信息, 即配置文件内容
	 *
	 * @param environment environment
	 */
	private void registerDefaultDingerConfig(Environment environment) {
		if (environment == null) {
			LogUtil.warn("environment is null.");
			return;
		}
		for (DingerType dingerType : enabledDingerTypes) {
			String dingers =
				DINGER_PROPERTIES_PREFIX + "dingers" + SPOT_SEPERATOR + dingerType.name()
					.toLowerCase() + SPOT_SEPERATOR;
			String tokenIdProp = dingers + "token-id";
			String secretProp = dingers + "secret";
			String decryptProp = dingers + "decrypt";
			String decryptKeyProp = dingers + "decryptKey";
			String asyncExecuteProp = dingers + "async";

			if (DingerUtils.isEmpty(tokenIdProp)) {
				LogUtil.debug("dinger={} is not open.", dingerType);
				continue;
			}
			String tokenId = environment.getProperty(tokenIdProp);
			String secret = environment.getProperty(secretProp);
			boolean decrypt = getProperty(environment, decryptProp);
			boolean async = getProperty(environment, asyncExecuteProp);
			DingerConfig defaultDingerConfig = DingerConfig.instance(tokenId);
			defaultDingerConfig.setDingerType(dingerType);
			defaultDingerConfig.setSecret(secret);
			if (decrypt) {
				defaultDingerConfig.setDecryptKey(
					environment.getProperty(decryptKeyProp)
				);
			}
			defaultDingerConfig.setAsyncExecute(async);

			defaultDingerConfig.check();
			defaultDingerConfigs.put(dingerType, defaultDingerConfig);
		}
	}

	/**
	 * getProperty
	 *
	 * @param environment environment
	 * @param prop        prop
	 * @return prop value
	 */
	private boolean getProperty(Environment environment, String prop) {
		if (environment.getProperty(prop) != null) {
			return environment.getProperty(prop, boolean.class);
		}
		return false;
	}
}
