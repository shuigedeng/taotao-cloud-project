/**
 * $Id: WangYiOssConfig.java,v 1.0 2022/3/4 9:54 PM chenmin Exp $
 */
package com.taotao.cloud.oss.artislong.core.wangyi.model;

import com.netease.cloud.ClientConfiguration;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

/**
 * 王毅oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:58
 */
public class WangYiOssConfig {

    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String bucketName;
    private String basePath;

    private ClientConfiguration clientConfig;
    /**
     * 断点续传参数
     */
    private SliceConfig sliceConfig = new SliceConfig();

    public void init() {
        this.sliceConfig.init();
        basePath = OssPathUtil.valid(basePath);
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

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public ClientConfiguration getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(ClientConfiguration clientConfig) {
		this.clientConfig = clientConfig;
	}

	public SliceConfig getSliceConfig() {
		return sliceConfig;
	}

	public void setSliceConfig(SliceConfig sliceConfig) {
		this.sliceConfig = sliceConfig;
	}
}
