package com.taotao.cloud.oss.qiniu;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 气妞妞oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:48
 */
@RefreshScope
@ConfigurationProperties(QiNiuOssProperties.PREFIX)
public class QiNiuOssProperties extends QiNiuOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.qiniuoss";

    private Map<String, QiNiuOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(QiNiuOssConfig::init);
        }
    }

	public Map<String, QiNiuOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, QiNiuOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
