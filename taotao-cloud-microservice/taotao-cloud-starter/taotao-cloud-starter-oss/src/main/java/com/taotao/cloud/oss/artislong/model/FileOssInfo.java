package com.taotao.cloud.oss.artislong.model;


/**
 * 文件信息对象
 */
public class FileOssInfo extends OssInfo {

	private String id;

	public FileOssInfo() {
	}

	public FileOssInfo(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
