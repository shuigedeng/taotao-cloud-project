/**
 * $Id: WangYiOssProperties.java,v 1.0 2022/3/4 9:51 PM chenmin Exp $
 */
package com.taotao.cloud.oss.artislong.core.wangyi;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.wangyi.model.WangYiOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.WANGYI)
public class WangYiOssProperties extends WangYiOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, WangYiOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(WangYiOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, WangYiOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, WangYiOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
