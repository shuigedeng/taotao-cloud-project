package com.taotao.cloud.oss.artislong.core.qiniu;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.qiniu.model.QiNiuOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 气妞妞oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:48
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.QINIU)
public class QiNiuOssProperties extends QiNiuOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, QiNiuOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(QiNiuOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, QiNiuOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, QiNiuOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
