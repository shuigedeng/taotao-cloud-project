package com.taotao.cloud.oss.artislong.core.ucloud.model;

import cn.ucloud.ufile.http.HttpClient;
import cn.ucloud.ufile.http.HttpClient.Config;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

/**
 * ucloud oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:20
 */
public class UCloudOssConfig {

    private String basePath;
    private String bucketName;
    private String publicKey;
    private String privateKey;
    private String region;
    private String proxySuffix;

    private HttpClient.Config clientConfig;

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

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProxySuffix() {
		return proxySuffix;
	}

	public void setProxySuffix(String proxySuffix) {
		this.proxySuffix = proxySuffix;
	}

	public Config getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(Config clientConfig) {
		this.clientConfig = clientConfig;
	}

	public SliceConfig getSliceConfig() {
		return sliceConfig;
	}

	public void setSliceConfig(SliceConfig sliceConfig) {
		this.sliceConfig = sliceConfig;
	}
}
