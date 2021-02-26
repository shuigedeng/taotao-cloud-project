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
package com.taotao.cloud.file.pojo;

import lombok.Data;

/**
 * file实体类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/11/12 15:54
 */
@Data
public class FileInfo {

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
}
