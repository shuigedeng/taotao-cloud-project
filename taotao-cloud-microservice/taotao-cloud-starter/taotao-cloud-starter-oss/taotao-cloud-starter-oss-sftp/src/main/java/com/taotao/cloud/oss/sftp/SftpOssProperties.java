package com.taotao.cloud.oss.sftp;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * sftp oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:02
 */
@RefreshScope
@ConfigurationProperties(SftpOssProperties.PREFIX)
public class SftpOssProperties extends SftpOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.sftp";

    private Map<String, SftpOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(SftpOssConfig::init);
        }
    }

	public Map<String, SftpOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, SftpOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
