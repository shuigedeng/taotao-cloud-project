package com.taotao.cloud.file.configuration;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.SetBucketAclRequest;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.file.base.AbstractFileUpload;
import com.taotao.cloud.file.constant.FileConstant;
import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import com.taotao.cloud.file.propeties.AliyunOssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 阿里云oss自动配置
 *
 * @author dengtao
 * @date 2020/10/26 10:49
 * @since v1.0
 */
@ConditionalOnProperty(prefix = "taotao.cloud.file", name = "type", havingValue = FileConstant.DFS_ALIYUN)
public class AliyunOssAutoConfiguration {

	private final AliyunOssProperties properties;

	public AliyunOssAutoConfiguration(AliyunOssProperties properties) {
		super();
		Assert.notNull(properties, "AliyunOssProperties为null");
		this.properties = properties;
	}

	@Bean
	public OSS oss() {
		String endpoint = properties.getEndPoint();
		String accessKey = properties.getAccessKeyId();
		String secretKey = properties.getAccessKeySecret();
		return new OSSClientBuilder().build(endpoint, accessKey, secretKey);
	}

	@Bean
	public AliossFileUpload fileUpload(OSS oss){
		return new AliossFileUpload(oss);
	}

	public class AliossFileUpload extends AbstractFileUpload {
		/**
		 * 每个part大小 最小为1M
		 */
		private static final long PART_SIZE = 1024 * 1024L;

		private final OSS oss;

		public AliossFileUpload(OSS oss) {
			super();
			this.oss = oss;
		}

		@Override
		protected FileInfo uploadFile(MultipartFile file, FileInfo fileInfo) {
			if (checkFileSize(fileInfo.getSize(), 10, "M")) {
				// 超过10M 大文件上传
				return uploadBigFile(fileInfo, file);
			}

			try {
				oss.setBucketAcl(new SetBucketAclRequest(properties.getBucketName()).withCannedACL(CannedAccessControlList.PublicRead));
				oss.putObject(properties.getBucketName(), fileInfo.getName(), new ByteArrayInputStream(file.getBytes()));
				fileInfo.setUrl(properties.getUrlPrefix() + "/" + fileInfo.getName());
				return fileInfo;
			} catch (Exception e) {
				LogUtil.error("[aliyun]文件上传失败:", e);
				throw new FileUploadException("[aliyun]文件上传失败");
			} finally {
				oss.shutdown();
			}
		}

		@Override
		protected FileInfo uploadFile(File file, FileInfo fileInfo) throws FileUploadException {
			try {
				oss.putObject(properties.getBucketName(), fileInfo.getName(), file);
				fileInfo.setUrl(properties.getUrlPrefix() + "/" + fileInfo.getName());
				return fileInfo;
			} catch (Exception e) {
				LogUtil.error("[aliyun]文件上传失败:", e);
				throw new FileUploadException("[aliyun]文件上传失败");
			} finally {
				oss.shutdown();
			}
		}

		@Override
		public FileInfo delete(FileInfo fileInfo) {
			try {
				oss.deleteObject(properties.getBucketName(), fileInfo.getName());
			} catch (Exception e) {
				LogUtil.error("[aliyun]文件删除失败:", e);
				throw new FileUploadException("[aliyun]文件删除失败");
			} finally {
				oss.shutdown();
			}
			return fileInfo;
		}


		public FileInfo uploadBigFile(FileInfo fileInfo,
									  MultipartFile file) {
			int part = calPartCount(file);
			String uploadId = initMultipartFileUpload(fileInfo);

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
				long curPartSize = Math.min(PART_SIZE, (fileInfo.getSize() - start));

				executor.execute(new UploadPartRunnable(latch, fileInfo, file,
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
				new CompleteMultipartUploadRequest(properties.getBucketName(), fileInfo.getName(), uploadId, eTags);
			completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);

			try {
				oss.completeMultipartUpload(completeMultipartUploadRequest);
			} catch (OSSException | ClientException e) {
				e.printStackTrace();
				throw new RuntimeException("大文件上传失败, 文件合并未成功");
			} finally {
				oss.shutdown();
			}

			fileInfo.setUrl(properties.getUrlPrefix() + "/" + fileInfo.getName());
			return fileInfo;
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
									  FileInfo fileInfo,
									  MultipartFile file,
									  String uploadId,
									  int partNumber,
									  long start,
									  long partSize,
									  List<PartETag> eTags) {
				this.latch = latch;
				this.ossClient = oss;
				this.bucketName = properties.getBucketName();
				this.filePath = fileInfo.getName();
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

		public String initMultipartFileUpload(FileInfo fileInfo) {
			InitiateMultipartUploadRequest multipartUploadRequest = new InitiateMultipartUploadRequest(
				properties.getBucketName(), fileInfo.getName());
			InitiateMultipartUploadResult initiateMultipartUploadResult = oss.initiateMultipartUpload(multipartUploadRequest);
			return initiateMultipartUploadResult.getUploadId();
		}

		/**
		 * 检查文件大小
		 *
		 * @author dengtao
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
		 * @author dengtao
		 * @date 2020/9/9 11:44
		 */
		public int calPartCount(MultipartFile uploadFile) {
			int part = (int) (uploadFile.getSize() / PART_SIZE);
			if (uploadFile.getSize() % PART_SIZE != 0) {
				part++;
			}
			return part;
		}
	}

}
