package com.taotao.cloud.oss.aws;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.aws.AwsOssConfig;
import com.taotao.cloud.oss.common.constant.OssConstant;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * aws oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:39
 */
@RefreshScope
@ConfigurationProperties(AwsOssProperties.PREFIX)
public class AwsOssProperties extends AwsOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.aws";

    private Map<String, AwsOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(AwsOssConfig::init);
        }
    }

	public Map<String, AwsOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, AwsOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
