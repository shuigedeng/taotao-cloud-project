package com.taotao.cloud.oss.jinshan;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 金山开源软件属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:22
 */
@RefreshScope
@ConfigurationProperties(JinShanOssProperties.PREFIX)
public class JinShanOssProperties extends JinShanOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.jinshan";

    private Map<String, JinShanOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(JinShanOssConfig::init);
        }
    }

	public Map<String, JinShanOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, JinShanOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
