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

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import com.taotao.cloud.oss.service.AbstractUploadFileService;
import com.taotao.cloud.oss.util.FtpClientUtil;
import java.io.File;
import java.io.FileInputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 * FtpUploadFileService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/08/24 16:09
 */
public class FtpUploadFileServiceImpl extends AbstractUploadFileService {

	private final FtpClientUtil ftpClientUtil;

	public FtpUploadFileServiceImpl(FtpClientUtil ftpClientUtil) {
		this.ftpClientUtil = ftpClientUtil;
	}

	@Override
	protected UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo) {
		try {
			boolean upload = ftpClientUtil.upload(file.getName(), file.getInputStream());
			if (!upload) {
				throw new UploadFileException("[Ftp]文件上传失败");
			}
			// todo 此处需要修改
			uploadFileInfo.setUrl("");
			return uploadFileInfo;
		} catch (Exception e) {
			LogUtil.error("[Ftp]文件上传失败:", e);
			throw new UploadFileException("[Ftp]文件上传失败");
		}
	}

	@Override
	protected UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo) {
		try {
			boolean upload = ftpClientUtil
				.upload(uploadFileInfo.getName(), new FileInputStream(file));
			if (!upload) {
				throw new UploadFileException("[Ftp]文件上传失败");
			}
			// todo 此处需要修改
			uploadFileInfo.setUrl("");
			return uploadFileInfo;
		} catch (Exception e) {
			LogUtil.error("[Ftp]文件上传失败", e);
			throw new UploadFileException("[Ftp]文件上传失败");
		}
	}

	@Override
	public UploadFileInfo delete(UploadFileInfo uploadFileInfo) {
		try {
			ftpClientUtil.remove(uploadFileInfo.getUrl());
		} catch (Exception e) {
			LogUtil.error("[Ftp]文件删除失败:", e);
			throw new UploadFileException("[Ftp]文件删除失败");
		}
		return uploadFileInfo;
	}
}
