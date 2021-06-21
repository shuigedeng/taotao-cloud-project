package com.taotao.cloud.common.enums;

import com.taotao.cloud.common.base.CoreProperties;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import lombok.val;

/**
 * @author: chejiangyi 默认环境枚举
 * @version: 2019-05-27 13:38
 **/
public enum EnvironmentEnum {
	JOB_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "job_DEV", ""),
		"bsf_job_url"),//DEV.job.b2bcsx.com
	APOLLO_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "apollo_DEV", ""),
		"bsf_apollo_url"),
	APOLLO_PRD(EnvironmentTypeEnum.PRD, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "apollo_prd", ""),
		"bsf_apollo_url"),
	CAT_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "cat_DEV", ""),
		"bsf_cat_url"),
	ELK_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "elk_DEV", ""),
		"bsf_elk_url"),
	RocketMQ_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "rocketmq_DEV", ""),
		"bsf_rocketmq_url"),
	EUREKA_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "eureka_DEV", ""),
		"bsf_eureka_url"),
	REDIS_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "redis_DEV", ""),
		"bsf_redis_url"),
	ES_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "es_DEV", ""),
		"bsf_es_url"),
	FILE_DEV(EnvironmentTypeEnum.DEV, ReflectionUtil
		.tryGetStaticFieldValue("com.yh.csx.bsf.core.base.BsfBaseConfig", "file_DEV", ""),
		"bsf_file_url");
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
					CoreProperties.BsfEnv, ""))) {
				return e;
			}
		}
		return defaultValue;
	}
}
