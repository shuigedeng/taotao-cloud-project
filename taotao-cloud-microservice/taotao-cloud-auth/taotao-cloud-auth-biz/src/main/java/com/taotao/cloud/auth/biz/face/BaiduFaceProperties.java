package com.taotao.cloud.auth.biz.face;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = BaiduFaceProperties.PREFIX)
public class BaiduFaceProperties {
	public static final String PREFIX = "auth.baidu.face";
	/**
	 * appId
	 */
	private String appId;
	/**
	 * api key
	 */
	private String apiKey;
	/**
	 * secret key
	 */
	private String secretKey;
	/**
	 * 用户组 可以没有
	 */
	private String groupId;


}
