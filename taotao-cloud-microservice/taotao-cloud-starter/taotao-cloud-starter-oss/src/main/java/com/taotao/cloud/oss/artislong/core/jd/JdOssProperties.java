package com.taotao.cloud.oss.artislong.core.jd;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.jd.model.JdOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈敏
 * @version JdOssProperties.java, v 1.1 2021/11/25 10:44 chenmin Exp $
 * Created on 2021/11/25
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.JD)
public class JdOssProperties extends JdOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, JdOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(JdOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, JdOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, JdOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
