package com.taotao.cloud.oss.ftp;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * ftp oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:09
 */
@RefreshScope
@ConfigurationProperties(FtpOssProperties.PREFIX)
public class FtpOssProperties extends FtpOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.ftposs";

    private Map<String, FtpOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(FtpOssConfig::init);
        }
    }

	public Map<String, FtpOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, FtpOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
