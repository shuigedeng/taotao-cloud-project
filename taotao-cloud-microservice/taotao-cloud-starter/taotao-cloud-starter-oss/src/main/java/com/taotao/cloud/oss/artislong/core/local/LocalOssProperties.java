package com.taotao.cloud.oss.artislong.core.local;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.local.model.LocalOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地操作系统属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:35
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.LOCAL)
public class LocalOssProperties extends LocalOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, LocalOssConfig> ossConfig = new HashMap<>();


	@Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(LocalOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, LocalOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, LocalOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
