package com.taotao.cloud.oss.artislong.core.tencent;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.tencent.model.TencentOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈敏
 * @version TencentProperties.java, v 1.1 2021/11/24 15:22 chenmin Exp $
 * Created on 2021/11/24
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.TENCENT)
public class TencentOssProperties extends TencentOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, TencentOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(TencentOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, TencentOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, TencentOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
