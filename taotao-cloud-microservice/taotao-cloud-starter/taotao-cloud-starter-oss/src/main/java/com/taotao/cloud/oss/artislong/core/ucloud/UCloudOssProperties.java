package com.taotao.cloud.oss.artislong.core.ucloud;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.ucloud.model.UCloudOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.UCLOUD)
public class UCloudOssProperties extends UCloudOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, UCloudOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(UCloudOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, UCloudOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, UCloudOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
