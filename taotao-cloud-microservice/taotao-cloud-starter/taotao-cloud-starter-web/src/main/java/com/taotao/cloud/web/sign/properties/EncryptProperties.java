package com.taotao.cloud.web.sign.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 加密数据基础配置
 *
 * @since 2021年8月9日13:46:33
 */
@RefreshScope
@ConfigurationProperties(prefix = EncryptProperties.PREFIX)
public class EncryptProperties {

	public static final String PREFIX = "taotao.cloud.web.sign.encrypt";
	/**
	 * 返回对象中需要加密字段的name
	 */
	private String resultName = "result";

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}
}
