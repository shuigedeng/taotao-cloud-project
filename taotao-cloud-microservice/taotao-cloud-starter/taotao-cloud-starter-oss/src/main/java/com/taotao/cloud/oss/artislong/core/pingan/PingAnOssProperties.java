package com.taotao.cloud.oss.artislong.core.pingan;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.pingan.model.PingAnOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 平安oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:10
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.PINGAN)
public class PingAnOssProperties extends PingAnOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, PingAnOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(PingAnOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, PingAnOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, PingAnOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
