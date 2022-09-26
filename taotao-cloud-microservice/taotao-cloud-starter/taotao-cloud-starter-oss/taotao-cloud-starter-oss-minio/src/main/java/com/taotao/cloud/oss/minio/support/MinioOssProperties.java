package com.taotao.cloud.oss.minio.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * minio oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:57
 */
@RefreshScope
@ConfigurationProperties(MinioOssProperties.PREFIX)
public class MinioOssProperties extends MinioOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.miniooss";

	private Map<String, MinioOssConfig> ossConfig = new HashMap<>();

	@Override
	public void afterPropertiesSet() {
		if (ossConfig.isEmpty()) {
			this.init();
		} else {
			ossConfig.values().forEach(MinioOssConfig::init);
		}
	}

	public Map<String, MinioOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(Map<String, MinioOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
