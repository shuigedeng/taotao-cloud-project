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
import com.taotao.cloud.file.base.AbstractFileUpload;
import com.taotao.cloud.file.constant.FileConstant;
import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import com.taotao.cloud.file.propeties.LocalProperties;
import com.taotao.cloud.file.util.FileUtil;
import com.taotao.cloud.file.util.FtpClientUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author dengtao
 * @date 2020/10/26 10:28
 * @since v1.0
 */
@ConditionalOnProperty(name = "taotao.cloud.file.type", havingValue = FileConstant.DFS_LOCAL)
public class LocalAutoConfiguration {

	private final LocalProperties properties;

	public LocalAutoConfiguration(LocalProperties properties) {
		super();
		Assert.notNull(properties, "LocalProperties为null");
		this.properties = properties;
	}

	@Bean
	public LocalFileUpload fileUpload(){
		return new LocalFileUpload();
	}

	public class LocalFileUpload extends AbstractFileUpload {

		@Override
		protected FileInfo uploadFile(MultipartFile file, FileInfo fileInfo) {
			try {
				String filePath = properties.getFilePath() + "/" + fileInfo.getName();
				String s = FileUtil.saveFile(file, filePath);
				if (s == null) {
					throw new FileUploadException("[local]文件上传失败");
				}
				fileInfo.setUrl(fileInfo.getName());
				return fileInfo;
			} catch (Exception e) {
				LogUtil.error("[local]文件上传失败:", e);
				throw new FileUploadException("[local]文件上传失败");
			}
		}

		@Override
		protected FileInfo uploadFile(File file, FileInfo fileInfo)  {
			try {
				String filePath = properties.getFilePath() + "/" + fileInfo.getName();
				File newFile = cn.hutool.core.io.FileUtil.newFile(filePath);
				File copy = cn.hutool.core.io.FileUtil.copy(file, newFile, true);
				if (copy == null) {
					throw new FileUploadException("[local]文件上传失败");
				}
				fileInfo.setUrl(fileInfo.getName());
				return fileInfo;
			} catch (Exception e) {
				LogUtil.error("[local]文件上传失败:", e);
				throw new FileUploadException("[local]文件上传失败");
			}
		}

		@Override
		public FileInfo delete(FileInfo fileInfo) {
			try {
				String filePath = properties.getFilePath() + fileInfo.getName();
				boolean b = FileUtil.deleteFile(filePath);
				if (!b) {
					throw new FileUploadException("[local]文件删除失败");
				}
			} catch (Exception e) {
				LogUtil.error("[local]文件删除失败:", e);
				throw new FileUploadException("[local]文件删除失败");
			}
			return fileInfo;
		}
	}
}
