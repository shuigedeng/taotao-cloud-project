package com.taotao.cloud.oss.huawei;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 华魏oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:26
 */
@RefreshScope
@ConfigurationProperties(HuaWeiOssProperties.PREFIX)
public class HuaWeiOssProperties extends HuaweiOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.huawei";

	private Map<String, HuaweiOssConfig> ossConfig = new HashMap<>();

	@Override
	public void afterPropertiesSet() {
		if (ossConfig.isEmpty()) {
			this.init();
		} else {
			ossConfig.values().forEach(HuaweiOssConfig::init);
		}
	}

	public Map<String, HuaweiOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, HuaweiOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
