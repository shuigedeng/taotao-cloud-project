package com.taotao.cloud.workflow.biz.common.config.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Minio属性配置
 *
 */
@ConfigurationProperties(prefix = "config.minio")
public class MinioConfigurationProperties {
    /**
     * 服务端地址
     */
    private String endpoint;
    /**
     * 账号
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 地址
     */
    private String fileHost;
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

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

    public String getFileHost() {
        return fileHost;
    }

    public void setFileHost(String fileHost) {
        this.fileHost = fileHost;
    }
}
