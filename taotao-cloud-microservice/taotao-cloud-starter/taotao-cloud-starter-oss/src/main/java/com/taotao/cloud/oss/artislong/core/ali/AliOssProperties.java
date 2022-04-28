package com.taotao.cloud.oss.artislong.core.ali;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.ali.model.AliOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:15
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.ALI)
public class AliOssProperties extends AliOssConfig implements InitializingBean {

	private Boolean enable = false;

	private Map<String, AliOssConfig> ossConfig = new HashMap<>();

	@Override
	public void afterPropertiesSet() {
		if (ossConfig.isEmpty()) {
			this.init();
		} else {
			ossConfig.values().forEach(AliOssConfig::init);
		}
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, AliOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, AliOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
