package com.taotao.cloud.file.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 阿里云文件服务Properties
 *
 * @author dengtao
 * @date 2020/10/26 09:39
 * @since v1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.file.aliyun")
public class AliyunOssProperties {
	/**
	 * 阿里云绑定的域名
	 */
	private String domain;

	/**
	 * 阿里云EndPoint
	 */
	private String endPoint;

	/**
	 * 阿里云AccessKeyId
	 */
	private String accessKeyId;

	/**
	 * 阿里云AccessKeySecret
	 */
	private String accessKeySecret;

	/**
	 * 阿里云BucketName
	 */
	private String bucketName;

	/**
	 * 阿里云urlPrefix
	 */
	private String urlPrefix;
}
