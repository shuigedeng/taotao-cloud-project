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
package com.taotao.cloud.job.component;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.core.utils.AddrUtil;
import com.taotao.cloud.job.constant.XxlJobConstant;
import com.taotao.cloud.job.properties.XxlProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * XxlJob配置类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/6/16 11:36
 */
@Slf4j
@ConditionalOnProperty(prefix = XxlJobConstant.BASE_XXL_JOB_PREFIX,
	name = XxlJobConstant.ENABLED, havingValue = XxlJobConstant.TRUE)
public class XxlJobComponent {

	@Bean
	public XxlJobSpringExecutor xxlJobExecutor(XxlProperties xxlProperties) {
		String appName = xxlProperties.getAppName().length() == 0 ? xxlProperties.getSpringAppName()
			: xxlProperties.getAppName();
		if (appName.length() == 0) {
			throw new BaseException("缺少参数：taotao.cloud.xxl.job.executor.appName");
		}
		String adminAddresses = xxlProperties.getAdminAddresses();
		if (StrUtil.isBlank(adminAddresses)) {
			throw new BaseException("缺少参数：taotao.cloud.xxl.job.executor.adminAddresses");
		}

		XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
		executor.setAdminAddresses(adminAddresses);
		executor.setAppname(appName);
		executor.setAddress(xxlProperties.getAddress());
		executor.setIp(xxlProperties.getIp());

		if (StrUtil.isEmpty(xxlProperties.getIp())) {
			executor.setIp(AddrUtil.getLocalAddr());
		}
		executor.setPort(xxlProperties.getPort());
		executor.setLogPath(xxlProperties.getLogPath());
		executor.setAccessToken(xxlProperties.getAccessToken());
		executor.setLogPath(xxlProperties.getLogPath());
		executor.setLogRetentionDays(xxlProperties.getLogRetentionDays());
		log.info(
			"[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_JOB_STARTER + "]" + "job模块已启动");
		return executor;
	}

}
