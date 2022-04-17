package com.taotao.cloud.oss.artislong.core.jdbc;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.jdbc.model.JdbcOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.JDBC)
public class JdbcOssProperties extends JdbcOssConfig implements InitializingBean {
    private Boolean enable = false;

    private Map<String, JdbcOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(JdbcOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, JdbcOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, JdbcOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
