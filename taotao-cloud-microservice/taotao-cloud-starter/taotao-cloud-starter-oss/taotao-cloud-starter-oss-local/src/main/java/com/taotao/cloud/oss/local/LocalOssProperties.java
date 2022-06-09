package com.taotao.cloud.oss.local;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地操作系统属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:35
 */
@RefreshScope
@ConfigurationProperties(LocalOssProperties.PREFIX)
public class LocalOssProperties extends LocalOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.localoss";

    private Map<String, LocalOssConfig> ossConfig = new HashMap<>();

	@Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(LocalOssConfig::init);
        }
    }

	public Map<String, LocalOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, LocalOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
