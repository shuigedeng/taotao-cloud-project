package com.taotao.cloud.oss.up;

import com.taotao.cloud.oss.up.up.model.UpOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:52
 */
@RefreshScope
@ConfigurationProperties(UpOssProperties.PREFIX)
public class UpOssProperties extends UpOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.uposs";

    private Map<String, UpOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(UpOssConfig::init);
        }
    }

	public Map<String, UpOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, UpOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
