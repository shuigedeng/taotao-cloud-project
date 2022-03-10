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

import com.UpYun;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import com.taotao.cloud.oss.propeties.UpYunProperties;
import com.taotao.cloud.oss.service.AbstractUploadFileService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * UpYunUploadFileService
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/08/24 16:17
 */
public class UpYunUploadFileServiceImpl extends AbstractUploadFileService {

	private final UpYun upyun;
	private final UpYunProperties properties;

	public UpYunUploadFileServiceImpl(UpYun upyun, UpYunProperties properties) {
		this.upyun = upyun;
		this.properties = properties;
	}

	@Override
	protected UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo) {
		boolean bFlag;
		try {
			InputStream inputStream = file.getInputStream();
			String fileName = uploadFileInfo.getName();
			String filePath =
				properties.getDomain() + "/" + properties.getBucketName() + "/" + UpYun
					.md5(fileName);
			byte[] buffer;

			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = inputStream.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			inputStream.close();
			bos.close();
			buffer = bos.toByteArray();

			bFlag = upyun.writeFile(filePath, buffer);
			uploadFileInfo.setUrl(filePath);
		} catch (Exception e) {
			LogUtil.error("[UpYun]文件上传失败:", e);
			throw new UploadFileException("[UpYun]文件上传失败");
		}
		if (!bFlag) {
			throw new UploadFileException("[UpYun]文件上传失败");
		}
		return uploadFileInfo;
	}

	@Override
	protected UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo) {
		boolean bFlag;
		try {
			InputStream inputStream = new FileInputStream(file);
			String fileName = uploadFileInfo.getName();
			String filePath =
				properties.getDomain() + "/" + properties.getBucketName() + "/" + UpYun
					.md5(fileName);
			byte[] buffer;

			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = inputStream.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			inputStream.close();
			bos.close();
			buffer = bos.toByteArray();

			bFlag = upyun.writeFile(filePath, buffer);
			uploadFileInfo.setUrl(filePath);
		} catch (Exception e) {
			LogUtil.error("[UpYun]文件上传失败:", e);
			throw new UploadFileException("[UpYun]文件上传失败");
		}

		if (!bFlag) {
			throw new UploadFileException("[UpYun]文件上传失败");
		}
		return uploadFileInfo;
	}

	@Override
	public UploadFileInfo delete(UploadFileInfo uploadFileInfo) {
		try {
			upyun.deleteFile(uploadFileInfo.getUrl(), new HashMap<>());
		} catch (Exception e) {
			LogUtil.error("[UpYun]文件删除失败:", e);
			throw new UploadFileException("[UpYun]文件删除失败");
		}
		return uploadFileInfo;
	}
}
