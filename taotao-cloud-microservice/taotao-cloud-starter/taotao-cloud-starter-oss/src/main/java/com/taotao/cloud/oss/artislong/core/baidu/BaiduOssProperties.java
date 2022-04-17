package com.taotao.cloud.oss.artislong.core.baidu;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.baidu.model.BaiduOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.BAIDU)
public class BaiduOssProperties extends BaiduOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, BaiduOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(BaiduOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, BaiduOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, BaiduOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
