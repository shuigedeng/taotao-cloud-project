package com.taotao.cloud.oss.pingan;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 平安oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:10
 */
@RefreshScope
@ConfigurationProperties(PingAnOssProperties.PREFIX)
public class PingAnOssProperties extends PingAnOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.pingan";

    private Map<String, PingAnOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(PingAnOssConfig::init);
        }
    }

	public Map<String, PingAnOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, PingAnOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
