package com.taotao.cloud.oss.artislong.core.jinshan;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.jinshan.model.JinShanOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈敏
 * @version JinShanOssProperties.java, v 1.1 2022/3/3 22:10 chenmin Exp $
 * Created on 2022/3/3
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.JINSHAN)
public class JinShanOssProperties extends JinShanOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, JinShanOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(JinShanOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, JinShanOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, JinShanOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
