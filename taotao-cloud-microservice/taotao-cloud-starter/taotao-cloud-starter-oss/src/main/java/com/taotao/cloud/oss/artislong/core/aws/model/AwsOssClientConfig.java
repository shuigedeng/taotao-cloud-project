package com.taotao.cloud.oss.artislong.core.aws.model;


/**
 * aws oss客户端配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:24
 */
public class AwsOssClientConfig {

    private Boolean accelerateModeEnabled = false;
    private Boolean checksumValidationEnabled = false;
    private Boolean multiRegionEnabled = false;
    private Boolean chunkedEncodingEnabled = false;
    private Boolean pathStyleAccessEnabled = false;
    private Boolean useArnRegionEnabled = false;
    private Boolean fipsEnabled = false;
    private Boolean dualstackEnabled = false;

	public Boolean getAccelerateModeEnabled() {
		return accelerateModeEnabled;
	}

	public void setAccelerateModeEnabled(Boolean accelerateModeEnabled) {
		this.accelerateModeEnabled = accelerateModeEnabled;
	}

	public Boolean getChecksumValidationEnabled() {
		return checksumValidationEnabled;
	}

	public void setChecksumValidationEnabled(Boolean checksumValidationEnabled) {
		this.checksumValidationEnabled = checksumValidationEnabled;
	}

	public Boolean getMultiRegionEnabled() {
		return multiRegionEnabled;
	}

	public void setMultiRegionEnabled(Boolean multiRegionEnabled) {
		this.multiRegionEnabled = multiRegionEnabled;
	}

	public Boolean getChunkedEncodingEnabled() {
		return chunkedEncodingEnabled;
	}

	public void setChunkedEncodingEnabled(Boolean chunkedEncodingEnabled) {
		this.chunkedEncodingEnabled = chunkedEncodingEnabled;
	}

	public Boolean getPathStyleAccessEnabled() {
		return pathStyleAccessEnabled;
	}

	public void setPathStyleAccessEnabled(Boolean pathStyleAccessEnabled) {
		this.pathStyleAccessEnabled = pathStyleAccessEnabled;
	}

	public Boolean getUseArnRegionEnabled() {
		return useArnRegionEnabled;
	}

	public void setUseArnRegionEnabled(Boolean useArnRegionEnabled) {
		this.useArnRegionEnabled = useArnRegionEnabled;
	}

	public Boolean getFipsEnabled() {
		return fipsEnabled;
	}

	public void setFipsEnabled(Boolean fipsEnabled) {
		this.fipsEnabled = fipsEnabled;
	}

	public Boolean getDualstackEnabled() {
		return dualstackEnabled;
	}

	public void setDualstackEnabled(Boolean dualstackEnabled) {
		this.dualstackEnabled = dualstackEnabled;
	}
}
