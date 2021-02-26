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

import cn.hutool.json.JSONUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.file.base.AbstractFileUpload;
import com.taotao.cloud.file.constant.FileConstant;
import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import com.taotao.cloud.file.propeties.QiniuProperties;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
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
@ConditionalOnProperty(name = "taotao.cloud.file.type", havingValue = FileConstant.DFS_QINIU)
public class QiniuAutoConfiguration {

	private final QiniuProperties properties;

	public QiniuAutoConfiguration(QiniuProperties properties) {
		super();
		Assert.notNull(properties, "QiniuProperties为null");
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public Zone zone() {
		Zone zone;
		switch (properties.getZone()) {
			case "zone0":
				zone = Zone.zone0();
				break;
			case "zone1":
				zone = Zone.zone1();
				break;
			case "zone2":
				zone = Zone.zone2();
				break;
			default:
				zone = Zone.autoZone();
				break;
		}

		return zone;
	}

	/**
	 * 华南机房
	 */
	@Bean
	public com.qiniu.storage.Configuration config(@Autowired Zone zone) {
		return new com.qiniu.storage.Configuration(zone);
	}

	/**
	 * 构建一个七牛上传工具实例
	 *
	 * @author dengtao
	 * @version 1.0.0
	 * @since 2020/10/26 11:36
	 */
	@Bean
	public UploadManager uploadManager(@Autowired com.qiniu.storage.Configuration config) {
		return new UploadManager(config);
	}

	/**
	 * 认证信息实例
	 *
	 * @author dengtao
	 * @version 1.0.0
	 * @since 2020/10/26 11:36
	 */
	@Bean
	public Auth auth() {
		return Auth.create(properties.getAccessKey(), properties.getSecretKey());
	}

	/**
	 * 构建七牛空间管理实例
	 *
	 * @author dengtao
	 * @version 1.0.0
	 * @since 2020/10/26 11:36
	 */
	@Bean
	public BucketManager bucketManager(@Autowired com.qiniu.storage.Configuration config,
		@Autowired Auth auth) {
		return new BucketManager(auth, config);
	}

	@Bean
	public QiniuFileUpload fileUpload(UploadManager uploadManager, BucketManager bucketManager,
		Auth auth) {
		return new QiniuFileUpload(uploadManager, bucketManager, auth);
	}

	public class QiniuFileUpload extends AbstractFileUpload {

		private final UploadManager uploadManager;
		private final BucketManager bucketManager;
		private final Auth auth;

		public QiniuFileUpload(UploadManager uploadManager, BucketManager bucketManager,
			Auth auth) {
			this.uploadManager = uploadManager;
			this.bucketManager = bucketManager;
			this.auth = auth;
		}

		@Override
		protected FileInfo uploadFile(MultipartFile file, FileInfo fileInfo) {
			try {
				Response response = uploadManager.put(file.getBytes(), fileInfo.getName(),
					auth.uploadToken(properties.getBucketName(), fileInfo.getName()));
				DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
				fileInfo.setUrl(properties.getDomain() + "/" + fileInfo.getName());
				return fileInfo;
			} catch (IOException e) {
				LogUtil.error("[qiniu]文件上传失败:", e);
				throw new FileUploadException("[qiniu]文件上传失败");
			}
		}

		@Override
		protected FileInfo uploadFile(File file, FileInfo fileInfo) {
			try {
				Response response = uploadManager.put(file, fileInfo.getName(),
					auth.uploadToken(properties.getBucketName(), fileInfo.getName()));
				DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
				fileInfo.setUrl(properties.getDomain() + "/" + fileInfo.getName());
				return fileInfo;
			} catch (QiniuException e) {
				LogUtil.error("[qiniu]文件上传失败:", e);
				throw new FileUploadException("[qiniu]文件上传失败");
			}
		}

		@Override
		public FileInfo delete(FileInfo fileInfo) {
			try {
				bucketManager.delete(properties.getBucketName(), fileInfo.getUrl());
			} catch (QiniuException e) {
				LogUtil.error("[qiniu]文件删除失败:", e);
				throw new FileUploadException("[qiniu]文件删除失败");
			}
			return fileInfo;
		}
	}

}
