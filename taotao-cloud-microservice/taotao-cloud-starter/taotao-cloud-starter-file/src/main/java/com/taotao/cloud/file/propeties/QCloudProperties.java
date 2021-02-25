package com.taotao.cloud.file.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 腾讯云服务Properties
 *
 * @author dengtao
 * @date 2020/10/26 09:39
 * @since v1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.file.qcloud")
public class QCloudProperties {
	/**
	 * 腾讯云绑定的域名
	 */
	private String domain;
	/**
	 * 腾讯云AppId
	 */
	private Integer appId;
	/**
	 * 腾讯云SecretId
	 */
	private String secretId;
	/**
	 * 腾讯云SecretKey
	 */
	private String secretKey;
	/**
	 * 腾讯云BucketName
	 */
	private String bucketName;
	/**
	 * 腾讯云COS所属地区
	 */
	private String region;
}
