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

import com.aliyun.oss.OSS;
import com.luhuiguo.fastdfs.conn.*;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.*;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.file.base.AbstractFileUpload;
import com.taotao.cloud.file.constant.FileConstant;
import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import com.taotao.cloud.file.propeties.FastdfsProperties;
import com.taotao.cloud.file.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConditionalOnProperty(name = "taotao.cloud.file.type", havingValue = FileConstant.DFS_FASTDFS)
public class FdfsDfsAutoConfiguration {
	private final FastdfsProperties properties;

	public FdfsDfsAutoConfiguration(FastdfsProperties properties) {
		super();
		Assert.notNull(properties, "FastdfsProperties为null");
		this.properties = properties;
	}

	@Bean
	public PooledConnectionFactory pooledConnectionFactory() {
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setSoTimeout(properties.getSoTimeout());
		pooledConnectionFactory.setConnectTimeout(properties.getConnectTimeout());
		return pooledConnectionFactory;
	}


	@Bean
	@ConfigurationProperties(prefix = "taotao.cloud.file.fastdfs.pool")
	public ConnectionPoolConfig connectionPoolConfig() {
		return new ConnectionPoolConfig();
	}

	@Bean
	public FdfsConnectionPool fdfsConnectionPool(PooledConnectionFactory pooledConnectionFactory,
												 ConnectionPoolConfig connectionPoolConfig) {
		return new FdfsConnectionPool(pooledConnectionFactory, connectionPoolConfig);
	}

	@Bean
	public TrackerConnectionManager trackerConnectionManager(FdfsConnectionPool fdfsConnectionPool) {
		return new TrackerConnectionManager(fdfsConnectionPool, properties.getTrackerList());
	}

	@Bean
	public TrackerClient trackerClient(TrackerConnectionManager trackerConnectionManager) {
		return new DefaultTrackerClient(trackerConnectionManager);
	}

	@Bean
	public ConnectionManager connectionManager(FdfsConnectionPool fdfsConnectionPool) {
		return new ConnectionManager(fdfsConnectionPool);
	}

	@Bean
	public FastFileStorageClient fastFileStorageClient(TrackerClient trackerClient,
													   ConnectionManager connectionManager) {
		return new DefaultFastFileStorageClient(trackerClient, connectionManager);
	}

	@Bean
	public AppendFileStorageClient appendFileStorageClient(TrackerClient trackerClient,
														   ConnectionManager connectionManager) {
		return new DefaultAppendFileStorageClient(trackerClient, connectionManager);
	}

	@Bean
	public FastDfsFileUpload fileUpload(FastFileStorageClient fastFileStorageClient){
		return new FastDfsFileUpload(fastFileStorageClient);
	}

	public static class FastDfsFileUpload extends AbstractFileUpload {

		private final FastFileStorageClient fastFileStorageClient;

		public FastDfsFileUpload(FastFileStorageClient fastFileStorageClient) {
			super();
			this.fastFileStorageClient = fastFileStorageClient;
		}

		@Override
		protected FileInfo uploadFile(MultipartFile file, FileInfo fileInfo) {
			try {
				StorePath storePath = fastFileStorageClient.uploadFile(file.getBytes(), fileInfo.getName());
				fileInfo.setUrl(storePath.getFullPath());
				return fileInfo;
			} catch (Exception e) {
				LogUtil.error("[fastdfs]文件上传失败:", e);
				throw new FileUploadException("[fastdfs]文件上传失败");
			}
		}

		@Override
		protected FileInfo uploadFile(File file, FileInfo fileInfo)  {
			try {
				StorePath storePath = fastFileStorageClient.uploadFile(FileUtil.getFileByteArray(file), fileInfo.getName());
				fileInfo.setUrl(storePath.getFullPath());
				return fileInfo;
			} catch (Exception e) {
				LogUtil.error("[fastdfs]文件上传失败:", e);
				throw new FileUploadException("[fastdfs]文件上传失败");
			}
		}

		@Override
		public FileInfo delete(FileInfo fileInfo) {
			try {
				fastFileStorageClient.deleteFile(fileInfo.getUrl());
			} catch (Exception e) {
				LogUtil.error("[fastdfs]文件删除失败:", e);
				throw new FileUploadException("[fastdfs]文件删除失败");
			}
			return fileInfo;
		}
	}
}
