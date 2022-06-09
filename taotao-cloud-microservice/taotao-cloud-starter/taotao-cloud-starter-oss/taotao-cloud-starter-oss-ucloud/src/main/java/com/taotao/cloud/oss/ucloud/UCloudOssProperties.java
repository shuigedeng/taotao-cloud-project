package com.taotao.cloud.oss.ucloud;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * ucloud oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:31
 */
@RefreshScope
@ConfigurationProperties(UCloudOssProperties.PREFIX)
public class UCloudOssProperties extends UCloudOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.ucloud";

    private Map<String, UCloudOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(UCloudOssConfig::init);
        }
    }

	public Map<String, UCloudOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, UCloudOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
