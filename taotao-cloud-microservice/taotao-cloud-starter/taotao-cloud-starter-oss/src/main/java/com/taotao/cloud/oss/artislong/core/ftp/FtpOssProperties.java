package com.taotao.cloud.oss.artislong.core.ftp;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.ftp.model.FtpOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.FTP)
public class FtpOssProperties extends FtpOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, FtpOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(FtpOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, FtpOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, FtpOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
