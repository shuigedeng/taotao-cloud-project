/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.demo.authorization.processor;

import cn.herodotus.engine.oauth2.core.properties.SecurityProperties;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 安全过滤配置处理器 </p>
 * <p>
 * 对静态资源、开放接口等静态配置进行处理。整合默认配置和配置文件中的配置
 *
 * @author : gengwei.zheng
 * @date : 2022/3/8 22:57
 */
@Component
public class SecurityMatcherConfigurer {

	private static final List<String> DEFAULT_IGNORED_STATIC_RESOURCES = Lists.newArrayList(
		"/error/**",
		"/plugins/**",
		"/authorization/**",
		"/static/**",
		"/webjars/**",
		"/swagger-ui.html",
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/v3/api-docs",
		"/v3/api-docs/**",
		"/openapi.json",
		"/favicon.ico");
	private static final List<String> DEFAULT_PERMIT_ALL_RESOURCES = Lists.newArrayList("/open/**",
		"/oauth2/sign-out");

	private List<String> staticResources;
	private List<String> permitAllResources;

	private final SecurityProperties securityProperties;

	public SecurityMatcherConfigurer(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
		this.staticResources = new ArrayList<>();
		this.permitAllResources = new ArrayList<>();
	}


	public List<String> getStaticResourceList() {
		if (CollectionUtils.isEmpty(this.staticResources)) {
			this.staticResources = merge(securityProperties.getMatcher().getStaticResources(),
				DEFAULT_IGNORED_STATIC_RESOURCES);
		}
		return this.staticResources;
	}

	public List<String> getPermitAllList() {
		if (CollectionUtils.isEmpty(this.permitAllResources)) {
			this.permitAllResources = merge(securityProperties.getMatcher().getPermitAll(),
				DEFAULT_PERMIT_ALL_RESOURCES);
		}
		return this.permitAllResources;
	}

	public String[] getStaticResourceArray() {
		return convertToArray(getStaticResourceList());
	}

	public String[] getPermitAllArray() {
		return convertToArray(getPermitAllList());
	}


	/**
	 * 合并默认配置和自定义配置
	 *
	 * @param customResources  自定义配置
	 * @param defaultResources 默认配置
	 * @return 合并后的配置
	 */
	private List<String> merge(List<String> customResources, List<String> defaultResources) {
		if (CollectionUtils.isEmpty(customResources)) {
			return defaultResources;
		} else {
			return CollectionUtils.collate(customResources, defaultResources);
		}
	}

	/**
	 * 将 List 转换为 String[]
	 *
	 * @param resources List
	 * @return String[]
	 */
	private String[] convertToArray(List<String> resources) {
		if (CollectionUtils.isNotEmpty(resources)) {
			String[] result = new String[resources.size()];
			return resources.toArray(result);
		} else {
			return new String[]{};
		}

	}
}
