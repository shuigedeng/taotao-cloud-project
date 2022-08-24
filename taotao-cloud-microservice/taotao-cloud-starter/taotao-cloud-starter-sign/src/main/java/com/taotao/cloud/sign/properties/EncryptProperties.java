package com.taotao.cloud.sign.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 加密数据基础配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:43:11
 */
@RefreshScope
@ConfigurationProperties(prefix = EncryptProperties.PREFIX)
public class EncryptProperties {

	public static final String PREFIX = "taotao.cloud.sign.encrypt";

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
