package com.taotao.cloud.oss.huawei;

import com.obs.services.ObsConfiguration;
import com.taotao.cloud.oss.common.model.SliceConfig;
import com.taotao.cloud.oss.common.util.OssPathUtil;

/**
 * 华为oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:14
 */
public class HuaweiOssConfig {

    private String basePath;

    private String accessKey;
    private String secretKey;
    private String bucketName;

    private ObsConfiguration clientConfig;

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

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public ObsConfiguration getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(ObsConfiguration clientConfig) {
		this.clientConfig = clientConfig;
	}

	public SliceConfig getSliceConfig() {
		return sliceConfig;
	}

	public void setSliceConfig(SliceConfig sliceConfig) {
		this.sliceConfig = sliceConfig;
	}
}
