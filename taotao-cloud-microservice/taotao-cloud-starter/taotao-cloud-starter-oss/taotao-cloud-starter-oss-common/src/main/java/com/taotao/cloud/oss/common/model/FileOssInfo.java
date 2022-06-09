package com.taotao.cloud.oss.common.model;


/**
 * 文件信息对象
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:34:17
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
