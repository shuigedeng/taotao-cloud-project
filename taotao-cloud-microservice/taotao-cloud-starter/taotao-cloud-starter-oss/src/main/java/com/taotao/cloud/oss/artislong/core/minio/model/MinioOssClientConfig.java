package com.taotao.cloud.oss.artislong.core.minio.model;


import com.taotao.cloud.oss.artislong.constant.OssConstant;

public class MinioOssClientConfig {
    private Long connectTimeout = OssConstant.DEFAULT_CONNECTION_TIMEOUT;
    private Long writeTimeout = OssConstant.DEFAULT_CONNECTION_TIMEOUT;
    private Long readTimeout = OssConstant.DEFAULT_CONNECTION_TIMEOUT;

	public Long getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Long connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Long getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(Long writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public Long getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Long readTimeout) {
		this.readTimeout = readTimeout;
	}
}
