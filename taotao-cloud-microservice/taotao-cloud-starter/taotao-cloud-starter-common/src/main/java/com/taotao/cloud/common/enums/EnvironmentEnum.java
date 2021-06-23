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
package com.taotao.cloud.common.enums;

import com.taotao.cloud.common.base.CoreProperties;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import lombok.val;

/**
 * 默认环境枚举
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:25
 **/
public enum EnvironmentEnum {
	JOB_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "job_DEV", ""),
		"taotao_cloud_job_url"),
	APOLLO_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "apollo_DEV",
			""),
		"taotao_cloud_apollo_url"),
	APOLLO_PRD(EnvironmentTypeEnum.PRD, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "apollo_prd",
			""),
		"taotao_cloud_apollo_url"),
	CAT_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "cat_DEV", ""),
		"taotao_cloud_cat_url"),
	ELK_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "elk_DEV", ""),
		"taotao_cloud_elk_url"),
	RocketMQ_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "rocketmq_DEV",
			""),
		"taotao_cloud_rocketmq_url"),
	EUREKA_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "eureka_DEV",
			""),
		"taotao_cloud_eureka_url"),
	REDIS_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "redis_DEV",
			""),
		"taotao_cloud_redis_url"),
	ES_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "es_DEV", ""),
		"taotao_cloud_es_url"),
	FILE_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.taotao.cloud.common.base.TaoTaoCloudBaseConfig", "file_DEV",
			""),
		"taotao_cloud_file_url");
	private EnvironmentTypeEnum env;
	private String url;
	private String serverkey;

	EnvironmentEnum(EnvironmentTypeEnum env, String url, String serverkey) {
		this.env = env;
		this.url = url;
		this.serverkey = serverkey;
	}

	public EnvironmentTypeEnum getEnv() {
		return env;
	}

	public String getUrl() {
		return url;
	}

	public String getServerkey() {
		return serverkey;
	}

	public static EnvironmentEnum get(String serverKey, EnvironmentEnum defaultValue) {
		for (val e : EnvironmentEnum.values()) {
			if (e.getServerkey().equalsIgnoreCase(serverKey) && e.getEnv().toString()
				.equalsIgnoreCase(PropertyUtil.getPropertyCache(
					CoreProperties.TaoTaoCloudEnv, ""))) {
				return e;
			}
		}
		return defaultValue;
	}
}
