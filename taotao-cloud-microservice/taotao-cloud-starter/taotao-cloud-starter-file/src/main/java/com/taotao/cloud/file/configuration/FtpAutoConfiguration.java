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

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.file.base.AbstractUploadFile;
import com.taotao.cloud.file.constant.UploadFileConstant;
import com.taotao.cloud.file.exception.UploadFileException;
import com.taotao.cloud.file.pojo.UploadFileInfo;
import com.taotao.cloud.file.propeties.FtpProperties;
import com.taotao.cloud.file.util.FtpClientUtil;
import java.io.File;
import java.io.FileInputStream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 10:28
 */
@ConditionalOnProperty(
	prefix = UploadFileConstant.BASE_UPLOAD_FILE_PREFIX,
	name = UploadFileConstant.TYPE,
	havingValue = UploadFileConstant.DFS_FTP
)
public class FtpAutoConfiguration {

	private final FtpProperties properties;

	public FtpAutoConfiguration(FtpProperties properties) {
		super();
		Assert.notNull(properties, "FtpProperties为null");
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public FtpClientUtil ftpClient() {
		return new FtpClientUtil(properties.getHost(),
			properties.getPort(),
			properties.getUsername(),
			properties.getPassword(),
			properties.getRemoteDicrory());
	}

	@Bean
	public FtpUploadFile fileUpload(FtpClientUtil ftpClientUtil) {
		return new FtpUploadFile(ftpClientUtil);
	}

	public class FtpUploadFile extends AbstractUploadFile {

		private final FtpClientUtil ftpClientUtil;

		public FtpUploadFile(FtpClientUtil ftpClientUtil) {
			super();
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
}
