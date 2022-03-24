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
package com.taotao.cloud.oss.service.impl;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import com.taotao.cloud.oss.service.AbstractUploadFileService;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * FastDfsFileUpload
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/08/24 16:05
 */
public class FastDfsFileUploadServiceImpl extends AbstractUploadFileService {

	private final FastFileStorageClient fastFileStorageClient;

	public FastDfsFileUploadServiceImpl(FastFileStorageClient fastFileStorageClient) {
		this.fastFileStorageClient = fastFileStorageClient;
	}

	//@Override
	//protected FileInfo uploadFile(MultipartFile file, FileInfo fileInfo) {
	//	try {
	//		StorePath storePath = fastFileStorageClient
	//			.uploadFile(file.getBytes(), fileInfo.getName());
	//		fileInfo.setUrl(storePath.getFullPath());
	//		return fileInfo;
	//	} catch (Exception e) {
	//		LogUtil.error("[fastdfs]文件上传失败:", e);
	//		throw new FileUploadException("[fastdfs]文件上传失败");
	//	}
	//}
	//
	//@Override
	//protected FileInfo uploadFile(File file, FileInfo fileInfo) {
	//	try {
	//		StorePath storePath = fastFileStorageClient
	//			.uploadFile(FileUtil.getFileByteArray(file), fileInfo.getName());
	//		fileInfo.setUrl(storePath.getFullPath());
	//		return fileInfo;
	//	} catch (Exception e) {
	//		LogUtil.error("[fastdfs]文件上传失败:", e);
	//		throw new FileUploadException("[fastdfs]文件上传失败");
	//	}
	//}
	//
	//@Override
	//public FileInfo delete(FileInfo fileInfo) {
	//	try {
	//		fastFileStorageClient.deleteFile(fileInfo.getUrl());
	//	} catch (Exception e) {
	//		LogUtil.error("[fastdfs]文件删除失败:", e);
	//		throw new FileUploadException("[fastdfs]文件删除失败");
	//	}
	//	return fileInfo;
	//}

	@Override
	protected UploadFileInfo uploadFile(
		MultipartFile file, UploadFileInfo uploadFileInfo) throws UploadFileException {
		return null;
	}

	@Override
	protected UploadFileInfo uploadFile(File file,
		UploadFileInfo uploadFileInfo) throws UploadFileException {
		return null;
	}

	@Override
	public UploadFileInfo delete(UploadFileInfo uploadFileInfo) throws UploadFileException {
		return null;
	}
}
