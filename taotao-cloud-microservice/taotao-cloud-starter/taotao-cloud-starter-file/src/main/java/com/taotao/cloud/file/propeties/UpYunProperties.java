package com.taotao.cloud.file.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * UpYunProperties
 *
 * @author dengtao
 * @date 2020/10/26 09:39
 * @since v1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.file.upyun")
public class UpYunProperties {
	/**
	 * 服务名
	 */
	private String bucketName;

	/**
	 * 操作员名称
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 图片对外域名
	 */
	private String domain;
}
