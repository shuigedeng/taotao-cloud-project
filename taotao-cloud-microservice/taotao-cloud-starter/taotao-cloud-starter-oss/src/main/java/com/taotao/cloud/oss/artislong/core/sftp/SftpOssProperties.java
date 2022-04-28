package com.taotao.cloud.oss.artislong.core.sftp;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.sftp.model.SftpOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * sftp oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:02
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.SFTP)
public class SftpOssProperties extends SftpOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, SftpOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(SftpOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, SftpOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, SftpOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
