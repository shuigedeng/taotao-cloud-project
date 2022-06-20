package com.taotao.cloud.oss.qiniu;


import com.taotao.cloud.oss.common.model.SliceConfig;
import com.taotao.cloud.oss.common.util.OssPathUtil;

/**
 * 气妞妞oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:35
 */
public class QiNiuOssConfig {

    private String basePath;
    private String accessKey;
    private String secretKey;
    private QiNiuRegion region = QiNiuRegion.AUTOREGION;
    private String bucketName;

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

	public QiNiuRegion getRegion() {
		return region;
	}

	public void setRegion(QiNiuRegion region) {
		this.region = region;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public SliceConfig getSliceConfig() {
		return sliceConfig;
	}

	public void setSliceConfig(SliceConfig sliceConfig) {
		this.sliceConfig = sliceConfig;
	}
}
