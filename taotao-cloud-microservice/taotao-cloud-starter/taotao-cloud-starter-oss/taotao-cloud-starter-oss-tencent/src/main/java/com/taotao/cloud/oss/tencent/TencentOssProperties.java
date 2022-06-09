package com.taotao.cloud.oss.tencent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:16
 */
@RefreshScope
@ConfigurationProperties(TencentOssProperties.PREFIX)
public class TencentOssProperties extends TencentOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.tencentoss";

    private Map<String, TencentOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(TencentOssConfig::init);
        }
    }

	public Map<String, TencentOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, TencentOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
