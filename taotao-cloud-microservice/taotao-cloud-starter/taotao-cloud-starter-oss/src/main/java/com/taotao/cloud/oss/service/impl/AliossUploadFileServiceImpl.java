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

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.SetBucketAclRequest;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import com.taotao.cloud.oss.propeties.AliyunOssProperties;
import com.taotao.cloud.oss.service.AbstractUploadFileService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.MultipartFile;

/**
 * AliossUploadFileService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/08/24 15:59
 */
public class AliossUploadFileServiceImpl extends AbstractUploadFileService {

	/**
	 * 每个part大小 最小为1M
	 */
	private static final long PART_SIZE = 1024 * 1024L;

	private final OSS oss;
	private final AliyunOssProperties properties;

	public AliossUploadFileServiceImpl(AliyunOssProperties properties, OSS oss) {
		this.oss = oss;
		this.properties = properties;
	}

	@Override
	protected UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo) {
		if (checkFileSize(uploadFileInfo.getSize(), 10, "M")) {
			// 超过10M 大文件上传
			return uploadBigFile(uploadFileInfo, file);
		}

		try {
			oss.setBucketAcl(new SetBucketAclRequest(properties.getBucketName())
				.withCannedACL(CannedAccessControlList.PublicRead));
			oss.putObject(properties.getBucketName(), uploadFileInfo.getName(),
				new ByteArrayInputStream(file.getBytes()));
			uploadFileInfo.setUrl(properties.getUrlPrefix() + "/" + uploadFileInfo.getName());
			return uploadFileInfo;
		} catch (Exception e) {
			LogUtil.error("[aliyun]文件上传失败:", e);
			throw new UploadFileException("[aliyun]文件上传失败");
		} finally {
			oss.shutdown();
		}
	}

	@Override
	protected UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo)
		throws UploadFileException {
		try {
			oss.putObject(properties.getBucketName(), uploadFileInfo.getName(), file);
			uploadFileInfo.setUrl(properties.getUrlPrefix() + "/" + uploadFileInfo.getName());
			return uploadFileInfo;
		} catch (Exception e) {
			LogUtil.error("[aliyun]文件上传失败:", e);
			throw new UploadFileException("[aliyun]文件上传失败");
		} finally {
			oss.shutdown();
		}
	}

	@Override
	public UploadFileInfo delete(UploadFileInfo uploadFileInfo) {
		try {
			oss.deleteObject(properties.getBucketName(), uploadFileInfo.getName());
		} catch (Exception e) {
			LogUtil.error("[aliyun]文件删除失败:", e);
			throw new UploadFileException("[aliyun]文件删除失败");
		} finally {
			oss.shutdown();
		}
		return uploadFileInfo;
	}


	public UploadFileInfo uploadBigFile(UploadFileInfo uploadFileInfo,
		MultipartFile file) {
		int part = calPartCount(file);
		String uploadId = initMultipartFileUpload(uploadFileInfo);

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(128);
		executor.setMaxPoolSize(2147483647);
		executor.setQueueCapacity(2147483647);
		executor.setThreadNamePrefix("tt_cloud_upload_big_file_threadpool_");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setKeepAliveSeconds(300);
		executor.initialize();

		List<PartETag> eTags = Collections.synchronizedList(new ArrayList<>());

		final CountDownLatch latch = new CountDownLatch(part);
		for (int i = 0; i < part; i++) {
			long start = PART_SIZE * i;
			long curPartSize = Math.min(PART_SIZE, (uploadFileInfo.getSize() - start));

			executor.execute(new UploadPartRunnable(latch, uploadFileInfo, file,
				uploadId, i + 1, PART_SIZE * i,
				curPartSize, eTags));
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		executor.shutdown();
		if (eTags.size() != part) {
			throw new RuntimeException("大文件上传失败, 有part未上传成功");
		}

		CompleteMultipartUploadRequest completeMultipartUploadRequest =
			new CompleteMultipartUploadRequest(properties.getBucketName(),
				uploadFileInfo.getName(),
				uploadId, eTags);
		completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);

		try {
			oss.completeMultipartUpload(completeMultipartUploadRequest);
		} catch (OSSException | ClientException e) {
			e.printStackTrace();
			throw new RuntimeException("大文件上传失败, 文件合并未成功");
		} finally {
			oss.shutdown();
		}

		uploadFileInfo.setUrl(properties.getUrlPrefix() + "/" + uploadFileInfo.getName());
		return uploadFileInfo;
	}

	public class UploadPartRunnable implements Runnable {

		private final CountDownLatch latch;
		private final OSS ossClient;
		private final String bucketName;
		private final String filePath;
		private final MultipartFile uploadFile;
		private final String uploadId;
		private final int partNumber;
		private final long start;
		private final long partSize;
		private final List<PartETag> eTags;

		public UploadPartRunnable(CountDownLatch latch,
			UploadFileInfo uploadFileInfo,
			MultipartFile file,
			String uploadId,
			int partNumber,
			long start,
			long partSize,
			List<PartETag> eTags) {
			this.latch = latch;
			this.ossClient = oss;
			this.bucketName = properties.getBucketName();
			this.filePath = uploadFileInfo.getName();
			this.uploadFile = file;
			this.uploadId = uploadId;
			this.start = start;
			this.partNumber = partNumber;
			this.partSize = partSize;
			this.eTags = eTags;
		}

		@Override
		public void run() {
			InputStream in = null;
			try {
				in = uploadFile.getInputStream();
				in.skip(start);

				UploadPartRequest uploadPartRequest = new UploadPartRequest();
				uploadPartRequest.setBucketName(bucketName);
				uploadPartRequest.setKey(filePath);
				uploadPartRequest.setUploadId(uploadId);
				uploadPartRequest.setInputStream(in);
				uploadPartRequest.setPartNumber(partNumber);
				uploadPartRequest.setPartSize(partSize);

				UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
				eTags.add(uploadPartResult.getPartETag());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				latch.countDown();
			}
		}
	}

	public String initMultipartFileUpload(UploadFileInfo uploadFileInfo) {
		InitiateMultipartUploadRequest multipartUploadRequest = new InitiateMultipartUploadRequest(
			properties.getBucketName(), uploadFileInfo.getName());
		InitiateMultipartUploadResult initiateMultipartUploadResult = oss
			.initiateMultipartUpload(multipartUploadRequest);
		return initiateMultipartUploadResult.getUploadId();
	}

	/**
	 * 检查文件大小
	 *
	 * @author shuigedeng
	 */
	private boolean checkFileSize(Long len, int size, String unit) {
		double fileSize;
		switch (unit.toUpperCase()) {
			case "B":
				fileSize = (double) len;
				break;
			case "K":
				fileSize = (double) len / 1024;
				break;
			case "M":
				fileSize = (double) len / 1048576;
				break;
			case "G":
				fileSize = (double) len / 1073741824;
				break;
			default:
				return false;
		}
		return fileSize > size;
	}

	/**
	 * 计算所需要的分片数量
	 *
	 * @param uploadFile uploadFile
	 * @since 2020/9/9 11:44
	 */
	public int calPartCount(MultipartFile uploadFile) {
		int part = (int) (uploadFile.getSize() / PART_SIZE);
		if (uploadFile.getSize() % PART_SIZE != 0) {
			part++;
		}
		return part;
	}
}

