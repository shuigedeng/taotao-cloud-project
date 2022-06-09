package com.taotao.cloud.oss.jd;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.common.constant.OssConstant;
import com.taotao.cloud.oss.jd.JdOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * jd oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:42
 */
@RefreshScope
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.JD)
public class JdOssProperties extends JdOssConfig implements InitializingBean {
	public static final String PREFIX = "taotao.cloud.oss.jd";

    private Map<String, JdOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(JdOssConfig::init);
        }
    }

	public Map<String, JdOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, JdOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
