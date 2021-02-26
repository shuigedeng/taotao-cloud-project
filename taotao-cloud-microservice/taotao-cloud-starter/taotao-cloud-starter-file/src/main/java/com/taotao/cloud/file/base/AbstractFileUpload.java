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
package com.taotao.cloud.file.base;

import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import com.taotao.cloud.file.util.FileUtil;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传抽象类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/3 07:47
 */
public abstract class AbstractFileUpload implements FileUpload {

	private static final String FILE_SPLIT = ".";

	@Override
	public FileInfo upload(File file) throws FileUploadException {
		FileInfo fileInfo = FileUtil.getFileInfo(file);
		if (!fileInfo.getName().contains(FILE_SPLIT)) {
			throw new FileUploadException("缺少后缀名");
		} else {
			return uploadFile(file, fileInfo);
		}
	}

	@Override
	public FileInfo upload(File file, String fileName) throws FileUploadException {
		FileInfo fileInfo = FileUtil.getFileInfo(file);
		if (!fileInfo.getName().contains(FILE_SPLIT)) {
			throw new FileUploadException("缺少后缀名");
		} else {
			fileInfo.setName(fileName);
			return uploadFile(file, fileInfo);
		}
	}

	@Override
	public FileInfo upload(MultipartFile file) throws FileUploadException {
		FileInfo fileInfo = FileUtil.getMultipartFileInfo(file);
		if (!fileInfo.getName().contains(FILE_SPLIT)) {
			throw new FileUploadException("缺少后缀名");
		}
		return uploadFile(file, fileInfo);
	}

	@Override
	public FileInfo upload(MultipartFile file, String fileName) throws FileUploadException {
		FileInfo fileInfo = FileUtil.getMultipartFileInfo(file);
		if (!fileInfo.getName().contains(FILE_SPLIT)) {
			throw new FileUploadException("缺少后缀名");
		}
		fileInfo.setName(fileName);
		return uploadFile(file, fileInfo);
	}

	protected abstract FileInfo uploadFile(MultipartFile file, FileInfo fileInfo)
		throws FileUploadException;

	protected abstract FileInfo uploadFile(File file, FileInfo fileInfo) throws FileUploadException;

}
