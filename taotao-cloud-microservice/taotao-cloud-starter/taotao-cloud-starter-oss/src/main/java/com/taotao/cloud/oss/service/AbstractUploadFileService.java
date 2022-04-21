/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import com.taotao.cloud.oss.util.FileUtil;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

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

	protected abstract UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo)
		throws UploadFileException;

	protected abstract UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo)
		throws UploadFileException;

}
