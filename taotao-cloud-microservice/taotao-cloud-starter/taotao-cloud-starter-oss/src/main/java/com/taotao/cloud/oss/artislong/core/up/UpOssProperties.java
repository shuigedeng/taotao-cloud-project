package com.taotao.cloud.oss.artislong.core.up;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.up.model.UpOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈敏
 * @version UpOssProperties.java, v 1.1 2021/11/30 12:03 chenmin Exp $
 * Created on 2021/11/30
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.UP)
public class UpOssProperties extends UpOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, UpOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(UpOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, UpOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, UpOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
