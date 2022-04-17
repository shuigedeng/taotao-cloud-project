package com.taotao.cloud.oss.artislong.model;


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
	 * 对象大小
	 */
	private String length;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 最新修改时间
	 */
	private String lastUpdateTime;

	public OssInfo() {
	}

	public OssInfo(String name, String path, String length, String createTime,
		String lastUpdateTime) {
		this.name = name;
		this.path = path;
		this.length = length;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
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

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
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
}
