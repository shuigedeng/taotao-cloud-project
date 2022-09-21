package com.taotao.cloud.wechat.biz.wechat.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信公众平台配置
 *
 * @author xxm
 * @date 2022/7/15
 */
@Getter
@Setter
@ConfigurationProperties("bootx.starter.third.wechat")
public class WeChatProperties {

	/**
	 * AppKey
	 */
	private String appId;

	/**
	 * AppSecret
	 */
	private String appSecret;

	/**
	 * token
	 */
	private String token;

	/**
	 * 消息加解密密钥
	 */
	private String encodingAesKey;
}
