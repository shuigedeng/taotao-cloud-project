package com.taotao.cloud.oss.artislong.core.minio;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.minio.model.MinioOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * minio oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:57
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.MINIO)
public class MinioOssProperties extends MinioOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, MinioOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(MinioOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, MinioOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, MinioOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
