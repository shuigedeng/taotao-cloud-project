package com.taotao.cloud.media.biz.media.vo;


/**
 *
 */
public class CameraVo {

	private String id;
	/**
	 * 播放地址
	 */
	private String url;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 启用flv
	 */
	private boolean enabledFlv = false;

	/**
	 * 启用hls
	 */
	private boolean enabledHls = false;

	/**
	 * javacv/ffmpeg
	 */
	private String mode = "未开启";


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isEnabledFlv() {
		return enabledFlv;
	}

	public void setEnabledFlv(boolean enabledFlv) {
		this.enabledFlv = enabledFlv;
	}

	public boolean isEnabledHls() {
		return enabledHls;
	}

	public void setEnabledHls(boolean enabledHls) {
		this.enabledHls = enabledHls;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
