package com.taotao.cloud.oss.aws;

import com.taotao.cloud.oss.common.model.SliceConfig;
import com.taotao.cloud.oss.common.util.OssPathUtil;
import software.amazon.awssdk.awscore.defaultsmode.DefaultsMode;

/**
 * aws oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:27
 */
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
