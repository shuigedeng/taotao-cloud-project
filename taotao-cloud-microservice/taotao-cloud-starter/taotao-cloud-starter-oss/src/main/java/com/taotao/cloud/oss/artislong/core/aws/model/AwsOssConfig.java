package com.taotao.cloud.oss.artislong.core.aws.model;

import com.taotao.cloud.oss.artislong.core.aws.constant.AwsRegion;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;
import software.amazon.awssdk.awscore.defaultsmode.DefaultsMode;

public class AwsOssConfig {
    /**
     * 数据存储路径
     */
    private String basePath;
    /**
     * Bucket名称
     */
    private String bucketName;

    private String accessKeyId;

    private String secretAccessKey;

    private AwsRegion region;

    private DefaultsMode mode;

    private AwsOssClientConfig clientConfig = new AwsOssClientConfig();
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

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

	public AwsRegion getRegion() {
		return region;
	}

	public void setRegion(AwsRegion region) {
		this.region = region;
	}

	public DefaultsMode getMode() {
		return mode;
	}

	public void setMode(DefaultsMode mode) {
		this.mode = mode;
	}

	public AwsOssClientConfig getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(
		AwsOssClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	public SliceConfig getSliceConfig() {
		return sliceConfig;
	}

	public void setSliceConfig(SliceConfig sliceConfig) {
		this.sliceConfig = sliceConfig;
	}
}
