package com.taotao.cloud.oss.ali;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:15
 */
@RefreshScope
@ConfigurationProperties(AliOssProperties.PREFIX)
public class AliOssProperties extends AliOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.ali";

	private Map<String, AliOssConfig> ossConfig = new HashMap<>();

	@Override
	public void afterPropertiesSet() {
		if (ossConfig.isEmpty()) {
			this.init();
		} else {
			ossConfig.values().forEach(AliOssConfig::init);
		}
	}

	public Map<String, AliOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, AliOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
