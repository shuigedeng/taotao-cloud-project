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
package com.taotao.cloud.file.configuration;

import com.UpYun;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.file.base.AbstractFileUpload;
import com.taotao.cloud.file.constant.FileConstant;
import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import com.taotao.cloud.file.propeties.UpYunProperties;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author dengtao
 * @version 1.0.0
 * @since 2020/10/26 10:28
 */
@ConditionalOnProperty(name = "taotao.cloud.file.type", havingValue = FileConstant.DFS_UPYUN)
public class UpYunAutoConfiguration {

	private final UpYunProperties properties;

	public UpYunAutoConfiguration(UpYunProperties properties) {
		super();
		Assert.notNull(properties, "UpYunProperties为null");
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public UpYun upYun() {
		// 创建实例
		UpYun upyun = new UpYun(properties.getBucketName(), properties.getUserName(),
			properties.getPassword());
		// 可选属性1，是否开启 debug 模式，默认不开启
		upyun.setDebug(false);
		// 可选属性2，超时时间，默认 30s
		upyun.setTimeout(30);
		return upyun;
	}

	@Bean
	public UpYunFileUpload fileUpload(UpYun upyun) {
		return new UpYunFileUpload(upyun);
	}

	public class UpYunFileUpload extends AbstractFileUpload {

		private final UpYun upyun;

		public UpYunFileUpload(UpYun upyun) {
			super();
			this.upyun = upyun;
		}

		@Override
		protected FileInfo uploadFile(MultipartFile file, FileInfo fileInfo) {
			boolean bFlag;
			try {
				InputStream inputStream = file.getInputStream();
				String fileName = fileInfo.getName();
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
				fileInfo.setUrl(filePath);
			} catch (Exception e) {
				LogUtil.error("[UpYun]文件上传失败:", e);
				throw new FileUploadException("[UpYun]文件上传失败");
			}
			if (!bFlag) {
				throw new FileUploadException("[UpYun]文件上传失败");
			}
			return fileInfo;
		}

		@Override
		protected FileInfo uploadFile(File file, FileInfo fileInfo) {
			boolean bFlag;
			try {
				InputStream inputStream = new FileInputStream(file);
				String fileName = fileInfo.getName();
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
				fileInfo.setUrl(filePath);
			} catch (Exception e) {
				LogUtil.error("[UpYun]文件上传失败:", e);
				throw new FileUploadException("[UpYun]文件上传失败");
			}

			if (!bFlag) {
				throw new FileUploadException("[UpYun]文件上传失败");
			}
			return fileInfo;
		}

		@Override
		public FileInfo delete(FileInfo fileInfo) {
			try {
				upyun.deleteFile(fileInfo.getUrl(), new HashMap<>());
			} catch (Exception e) {
				LogUtil.error("[UpYun]文件删除失败:", e);
				throw new FileUploadException("[UpYun]文件删除失败");
			}
			return fileInfo;
		}
	}
}
