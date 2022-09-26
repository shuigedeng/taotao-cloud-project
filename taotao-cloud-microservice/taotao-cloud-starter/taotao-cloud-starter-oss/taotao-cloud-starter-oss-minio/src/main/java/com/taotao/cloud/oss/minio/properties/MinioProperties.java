package com.taotao.cloud.oss.minio.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * MinioConfiguration
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-13 09:07:11
 */
@RefreshScope
@ConfigurationProperties(prefix = MinioProperties.PREFIX)
public class MinioProperties {

	public static final String PREFIX = "taotao.cloud.oss.minio";

	private String accessKey;

	private String secretKey;

	private String url;

	private String bucketName;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
}
