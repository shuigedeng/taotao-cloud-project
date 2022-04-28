package com.taotao.cloud.oss.artislong.core.qingyun.model;

import com.qingstor.sdk.config.EnvContext;
import com.qingstor.sdk.config.EnvContext.HttpConfig;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

/**
 * 清云操作系统配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:15
 */
public class QingYunOssConfig {

    private String endpoint;
    private String accessKey;
    private String accessSecret;

    private String bucketName;
    private String zone;
    private String basePath;

    private Boolean cnameSupport = false;
    private String additionalUserAgent;
    private Boolean virtualHostEnabled = false;

    private EnvContext.HttpConfig clientConfig = new EnvContext.HttpConfig();

    /**
     * 断点续传参数
     */
    private SliceConfig sliceConfig = new SliceConfig();

    public void init() {
        this.sliceConfig.init();
        basePath = OssPathUtil.valid(basePath);
    }

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

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public Boolean getCnameSupport() {
		return cnameSupport;
	}

	public void setCnameSupport(Boolean cnameSupport) {
		this.cnameSupport = cnameSupport;
	}

	public String getAdditionalUserAgent() {
		return additionalUserAgent;
	}

	public void setAdditionalUserAgent(String additionalUserAgent) {
		this.additionalUserAgent = additionalUserAgent;
	}

	public Boolean getVirtualHostEnabled() {
		return virtualHostEnabled;
	}

	public void setVirtualHostEnabled(Boolean virtualHostEnabled) {
		this.virtualHostEnabled = virtualHostEnabled;
	}

	public HttpConfig getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(HttpConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	public SliceConfig getSliceConfig() {
		return sliceConfig;
	}

	public void setSliceConfig(SliceConfig sliceConfig) {
		this.sliceConfig = sliceConfig;
	}
}
