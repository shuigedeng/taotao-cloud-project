package com.taotao.cloud.oss.artislong.core.ali.model;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

/**
 * @author 陈敏
 * @version AliGroupInfo.java, v 1.1 2022/2/18 17:48 chenmin Exp $
 * Created on 2022/2/18
 */
public class AliOssConfig {
    /**
     * 数据存储路径
     */
    private String basePath;
    /**
     * Bucket名称
     */
    private String bucketName;
    /**
     * OSS地址
     */
    private String endpoint;
    /**
     * AccessKey ID
     */
    private String accessKeyId;
    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    private String securityToken;

    private ClientBuilderConfiguration clientConfig;

    /**
     * 断点续传参数
     */
    private SliceConfig sliceConfig = new SliceConfig();

    public void init() {
        this.sliceConfig.init();
        basePath = OssPathUtil.valid(basePath);
    }

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public ClientBuilderConfiguration getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(ClientBuilderConfiguration clientConfig) {
		this.clientConfig = clientConfig;
	}

	public SliceConfig getSliceConfig() {
		return sliceConfig;
	}

	public void setSliceConfig(SliceConfig sliceConfig) {
		this.sliceConfig = sliceConfig;
	}
}
