package com.taotao.cloud.oss.qingyun;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 清云操作系统属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:22
 */
@RefreshScope
@ConfigurationProperties(QingYunOssProperties.PREFIX)
public class QingYunOssProperties extends QingYunOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.qingyun";

    private Map<String, QingYunOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(QingYunOssConfig::init);
        }
    }

	public Map<String, QingYunOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, QingYunOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
