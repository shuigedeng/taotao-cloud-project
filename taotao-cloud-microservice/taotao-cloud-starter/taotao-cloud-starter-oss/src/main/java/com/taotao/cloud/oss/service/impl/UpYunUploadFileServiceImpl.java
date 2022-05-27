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

import com.upyun.RestManager;
import com.upyun.UpYunUtils;
import okhttp3.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * UpYunUploadFileService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/08/24 16:17
 */
public class UpYunUploadFileServiceImpl extends AbstractUploadFileService {

	private final RestManager upyun;
	private final UpYunProperties properties;

	public UpYunUploadFileServiceImpl(RestManager upyun, UpYunProperties properties) {
		this.upyun = upyun;
		this.properties = properties;
	}

	@Override
	protected UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo) {
		Response response;
		try {
			InputStream inputStream = file.getInputStream();
			String fileName = uploadFileInfo.getName();
			String filePath =
				properties.getDomain() + "/" + properties.getBucketName() + "/" + UpYunUtils
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

			response = upyun.writeFile(filePath, buffer, new HashMap<>());
			uploadFileInfo.setUrl(filePath);
		} catch (Exception e) {
			LogUtil.error("[UpYun]文件上传失败:", e);
			throw new UploadFileException("[UpYun]文件上传失败");
		}
		if (!response.isSuccessful()) {
			throw new UploadFileException("[UpYun]文件上传失败");
		}
		return uploadFileInfo;
	}

	@Override
	protected UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo) {
		Response response;
		try {
			InputStream inputStream = new FileInputStream(file);
			String fileName = uploadFileInfo.getName();
			String filePath =
				properties.getDomain() + "/" + properties.getBucketName() + "/" + UpYunUtils
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

			response = upyun.writeFile(filePath, buffer, new HashMap<>());
			uploadFileInfo.setUrl(filePath);
		} catch (Exception e) {
			LogUtil.error("[UpYun]文件上传失败:", e);
			throw new UploadFileException("[UpYun]文件上传失败");
		}

		if (!response.isSuccessful()) {
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
