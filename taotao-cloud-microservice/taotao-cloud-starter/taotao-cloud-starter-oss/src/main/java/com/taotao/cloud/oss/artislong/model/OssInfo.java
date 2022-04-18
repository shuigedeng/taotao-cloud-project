package com.taotao.cloud.oss.artislong.model;


import com.taotao.cloud.oss.model.UploadFileInfo;

public class OssInfo {

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 存储路径
	 */
	private String path;
	/**
	 * 访问路径
	 */
	private String url;
	/**
	 * 对象大小
	 */
	private Long length;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 最新修改时间
	 */
	private String lastUpdateTime;

	private UploadFileInfo uploadFileInfo;

	public OssInfo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "OssInfo{" +
			"name='" + name + '\'' +
			", path='" + path + '\'' +
			", url='" + url + '\'' +
			", length=" + length +
			", createTime='" + createTime + '\'' +
			", lastUpdateTime='" + lastUpdateTime + '\'' +
			'}';
	}

	public UploadFileInfo getUploadFileInfo() {
		return uploadFileInfo;
	}

	public void setUploadFileInfo(UploadFileInfo uploadFileInfo) {
		this.uploadFileInfo = uploadFileInfo;
	}
}
