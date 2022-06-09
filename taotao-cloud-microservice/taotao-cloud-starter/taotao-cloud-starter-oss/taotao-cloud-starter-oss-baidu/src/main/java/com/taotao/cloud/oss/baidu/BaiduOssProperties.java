package com.taotao.cloud.oss.baidu;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 百度oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:54
 */
@RefreshScope
@ConfigurationProperties(BaiduOssProperties.PREFIX)
public class BaiduOssProperties extends BaiduOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.baidu";

	private Map<String, BaiduOssConfig> ossConfig = new HashMap<>();

	@Override
	public void afterPropertiesSet() {
		if (ossConfig.isEmpty()) {
			this.init();
		} else {
			ossConfig.values().forEach(BaiduOssConfig::init);
		}
	}

	public Map<String, BaiduOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, BaiduOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
