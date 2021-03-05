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
import com.taotao.cloud.file.propeties.LocalProperties;
import com.taotao.cloud.file.util.FileUtil;
import java.io.File;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author dengtao
 * @version 1.0.0
 * @since 2020/10/26 10:28
 */
@ConditionalOnProperty(
	prefix = UploadFileConstant.BASE_UPLOAD_FILE_PREFIX,
	name = UploadFileConstant.TYPE,
	havingValue = UploadFileConstant.DFS_LOCAL
)
public class LocalAutoConfiguration {

	private final LocalProperties properties;

	public LocalAutoConfiguration(LocalProperties properties) {
		super();
		Assert.notNull(properties, "LocalProperties为null");
		this.properties = properties;
	}

	@Bean
	public LocalUploadFile fileUpload() {
		return new LocalUploadFile();
	}

	public class LocalUploadFile extends AbstractUploadFile {

		@Override
		protected UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo) {
			try {
				String filePath = properties.getFilePath() + "/" + uploadFileInfo.getName();
				String s = FileUtil.saveFile(file, filePath);
				if (s == null) {
					throw new UploadFileException("[local]文件上传失败");
				}
				uploadFileInfo.setUrl(uploadFileInfo.getName());
				return uploadFileInfo;
			} catch (Exception e) {
				LogUtil.error("[local]文件上传失败:", e);
				throw new UploadFileException("[local]文件上传失败");
			}
		}

		@Override
		protected UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo) {
			try {
				String filePath = properties.getFilePath() + "/" + uploadFileInfo.getName();
				File newFile = cn.hutool.core.io.FileUtil.newFile(filePath);
				File copy = cn.hutool.core.io.FileUtil.copy(file, newFile, true);
				if (copy == null) {
					throw new UploadFileException("[local]文件上传失败");
				}
				uploadFileInfo.setUrl(uploadFileInfo.getName());
				return uploadFileInfo;
			} catch (Exception e) {
				LogUtil.error("[local]文件上传失败:", e);
				throw new UploadFileException("[local]文件上传失败");
			}
		}

		@Override
		public UploadFileInfo delete(UploadFileInfo uploadFileInfo) {
			try {
				String filePath = properties.getFilePath() + uploadFileInfo.getName();
				boolean b = FileUtil.deleteFile(filePath);
				if (!b) {
					throw new UploadFileException("[local]文件删除失败");
				}
			} catch (Exception e) {
				LogUtil.error("[local]文件删除失败:", e);
				throw new UploadFileException("[local]文件删除失败");
			}
			return uploadFileInfo;
		}
	}
}
