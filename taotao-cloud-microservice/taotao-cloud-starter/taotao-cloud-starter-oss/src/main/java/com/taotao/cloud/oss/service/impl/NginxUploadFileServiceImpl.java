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
package com.taotao.cloud.oss.service.impl;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import com.taotao.cloud.oss.propeties.NginxProperties;
import com.taotao.cloud.oss.service.AbstractUploadFileService;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * NginxUploadFileService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/08/24 16:12
 */
public class NginxUploadFileServiceImpl extends AbstractUploadFileService {

	private final NginxProperties properties;

	public NginxUploadFileServiceImpl(NginxProperties properties) {
		this.properties = properties;
	}

	@Override
	protected UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo) {
		try {
			String f = properties.getUploadPath() + "/" + uploadFileInfo.getName();
			f = f.substring(0, f.lastIndexOf("/"));
			File localFile = new File(f);
			try {
				if (!localFile.exists() && !localFile.isDirectory()) {
					localFile.mkdirs();
				}
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(f));
				// file.transferTo(new File(config.getNginxUploadPath() + path));
			} catch (Exception e) {
				LogUtil.error("[nginx]文件上传错误:", e);
				throw new UploadFileException("[nginx]文件上传错误");
			}
			String s = properties.getDownPath() + "/" + uploadFileInfo.getName();
			uploadFileInfo.setUrl(s);
			return uploadFileInfo;
		} catch (Exception e) {
			LogUtil.error("[nginx]文件上传失败:", e);
			throw new UploadFileException("[nginx]文件上传失败");
		}
	}

	@Override
	protected UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo) {
		try {
			String f = properties.getUploadPath() + "/" + uploadFileInfo.getName();
			f = f.substring(0, f.lastIndexOf("/"));
			File localFile = new File(f);
			try {
				if (!localFile.exists() && !localFile.isDirectory()) {
					localFile.mkdirs();
				}
				FileUtils.copyInputStreamToFile(new FileInputStream(file), new File(f));
				// file.transferTo(new File(config.getNginxUploadPath() + path));
			} catch (Exception e) {
				LogUtil.error("[nginx]文件上传错误:", e);
				throw new UploadFileException("[nginx]文件上传错误");
			}
			String s = properties.getDownPath() + "/" + uploadFileInfo.getName();
			uploadFileInfo.setUrl(s);
			return uploadFileInfo;
		} catch (Exception e) {
			LogUtil.error("[nginx]文件上传失败:", e);
			throw new UploadFileException("[nginx]文件上传失败");
		}
	}

	@Override
	public UploadFileInfo delete(UploadFileInfo uploadFileInfo) {
		try {
			FileUtils
				.forceDelete(new File(properties.getUploadPath() + "/" + uploadFileInfo.getName()));
		} catch (Exception e) {
			LogUtil.error("[nginx]文件删除失败:", e);
			throw new UploadFileException("[nginx]文件删除失败");
		}
		return uploadFileInfo;
	}
}

