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
package com.taotao.cloud.oss.service;

import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 10:42
 */
public interface UploadFileService {

	/**
	 * 文件上传接口
	 *
	 * @param file 文件对象
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author shuigedeng
	 * @since 2020/11/12 16:01
	 */
	UploadFileInfo upload(File file) throws UploadFileException;

	/**
	 * 文件上传接口
	 *
	 * @param file    file
	 * @param fileKey fileKey
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author shuigedeng
	 * @since 2020/11/12 17:03
	 */
	UploadFileInfo upload(File file, String fileKey) throws UploadFileException;

	/**
	 * 文件上传接口
	 *
	 * @param file file
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author shuigedeng
	 * @since 2020/11/12 17:47
	 */
	UploadFileInfo upload(MultipartFile file) throws UploadFileException;

	/**
	 * 文件上传接口
	 *
	 * @param file    file
	 * @param fileKey fileKey
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author shuigedeng
	 * @since 2020/11/12 17:47
	 */
	UploadFileInfo upload(MultipartFile file, String fileKey) throws UploadFileException;

	/**
	 * 删除文件
	 *
	 * @param uploadFileInfo fileInfo
	 * @return com.taotao.cloud.file.pojo.FileInfo
	 * @author shuigedeng
	 * @since 2020/11/12 17:47
	 */
	UploadFileInfo delete(UploadFileInfo uploadFileInfo) throws UploadFileException;
}
