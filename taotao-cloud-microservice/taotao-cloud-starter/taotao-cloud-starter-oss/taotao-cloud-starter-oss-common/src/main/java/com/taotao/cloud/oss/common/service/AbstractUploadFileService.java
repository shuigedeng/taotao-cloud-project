/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.oss.common.service;

import com.taotao.cloud.oss.common.exception.UploadFileException;
import com.taotao.cloud.oss.common.model.UploadFileInfo;
import com.taotao.cloud.oss.common.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件上传抽象类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/3 07:47
 */
public abstract class AbstractUploadFileService implements UploadFileService {

	public static final String UPLOAD_FILE_SPLIT = ".";
	public static final String UPLOAD_FILE_ERROR_MESSAGE = "缺少后缀名";

	@Override
	public UploadFileInfo upload(File file) throws UploadFileException {
		UploadFileInfo uploadFileInfo = FileUtil.getFileInfo(file);
		if (!uploadFileInfo.getName().contains(UPLOAD_FILE_SPLIT)) {
			throw new UploadFileException(UPLOAD_FILE_ERROR_MESSAGE);
		} else {
			return uploadFile(file, uploadFileInfo);
		}
	}

	@Override
	public UploadFileInfo upload(File file, String fileName) throws UploadFileException {
		UploadFileInfo uploadFileInfo = FileUtil.getFileInfo(file);
		if (!uploadFileInfo.getName().contains(UPLOAD_FILE_SPLIT)) {
			throw new UploadFileException(UPLOAD_FILE_ERROR_MESSAGE);
		} else {
			uploadFileInfo.setName(fileName);
			return uploadFile(file, uploadFileInfo);
		}
	}

	@Override
	public UploadFileInfo upload(MultipartFile file) throws UploadFileException {
		UploadFileInfo uploadFileInfo = FileUtil.getMultipartFileInfo(file);
		if (!uploadFileInfo.getName().contains(UPLOAD_FILE_SPLIT)) {
			throw new UploadFileException(UPLOAD_FILE_ERROR_MESSAGE);
		}
		return uploadFile(file, uploadFileInfo);
	}

	@Override
	public UploadFileInfo upload(MultipartFile file, String fileName) throws UploadFileException {
		UploadFileInfo uploadFileInfo = FileUtil.getMultipartFileInfo(file);
		if (!uploadFileInfo.getName().contains(UPLOAD_FILE_SPLIT)) {
			throw new UploadFileException(UPLOAD_FILE_ERROR_MESSAGE);
		}
		uploadFileInfo.setName(fileName);
		return uploadFile(file, uploadFileInfo);
	}

	/**
	 * 上传文件
	 *
	 * @param file           文件
	 * @param uploadFileInfo 上传文件信息
	 * @return {@link UploadFileInfo }
	 * @throws UploadFileException 上传异常
	 * @since 2022-09-23 10:42:37
	 */
	protected abstract UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo)
		throws UploadFileException;

	/**
	 * 上传文件
	 *
	 * @param file           文件
	 * @param uploadFileInfo 上传文件信息
	 * @return {@link UploadFileInfo }
	 * @throws UploadFileException 上传异常
	 * @since 2022-04-27 17:33:37
	 */
	protected abstract UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo)
		throws UploadFileException;

}
