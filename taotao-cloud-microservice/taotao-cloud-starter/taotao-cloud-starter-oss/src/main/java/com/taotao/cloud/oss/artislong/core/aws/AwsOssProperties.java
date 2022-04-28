package com.taotao.cloud.oss.artislong.core.aws;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.aws.model.AwsOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * aws oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:39
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.AWS)
public class AwsOssProperties extends AwsOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, AwsOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(AwsOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, AwsOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, AwsOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
