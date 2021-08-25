/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.file.model;


/**
 * file实体类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/12 15:54
 */
public class UploadFileInfo {

	/**
	 * 原始文件名
	 */
	private String originalFileName;

	/**
	 * 编码之后文件名称
	 */
	private String name;

	/**
	 * 文件md5
	 */
	private String fileMd5;

	/**
	 * 是否图片
	 */
	private Boolean isImg;

	/**
	 * contentType
	 */
	private String contentType;

	/**
	 * 上传文件类型
	 */
	private String fileType;

	/**
	 * 文件大小
	 */
	private long size;

	/**
	 * 访问路径
	 */
	private String url;


	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public Boolean getImg() {
		return isImg;
	}

	public void setImg(Boolean img) {
		isImg = img;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
