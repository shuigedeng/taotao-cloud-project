/**
 *
 */
package com.taotao.cloud.file.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * oss 客户端工具类
 *
 * @author dengtao
 * @date 2020/11/12 16:36
 * @since v1.0
 */
public class OSSClientUtil {
	/**
	 * 允许上传的文件格式
	 */
	private static final String[] FILE_TYPE = new String[]{".bmp", ".jpg",
		".jpeg", ".gif", ".png", ".mp4", ".ppt"};

	/**
	 * 每个part大小 最小为1M
	 */
	private static final long PART_SIZE = 1024 * 1024L;


	public static String upload(MultipartFile uploadFile,
								OSS ossClient,
								String bucketName,
								String urlPrefix) {
		String fileName = uploadFile.getOriginalFilename();
		// 校验图片格式
		boolean isLegal = Arrays.stream(FILE_TYPE).anyMatch(type -> StringUtils.endsWithIgnoreCase(fileName, type));
		if (!isLegal) {
			throw new RuntimeException("不允许上传的文件格式");
		}

		String md5;
		try {
			md5 = getMd5(uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("获取文件md5失败");
		}

		//文件新路径
		String filePath = getFilePath(fileName, md5);

		if (checkFileSize(uploadFile.getSize(), 10, "M")) {
			// 超过10M 大文件上传
			return uploadBigFile(filePath, uploadFile, ossClient, bucketName, urlPrefix);
		}

		try {
			ossClient.putObject(bucketName, filePath, new
				ByteArrayInputStream(uploadFile.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("上传失败");
		}
		return urlPrefix + filePath;
	}


	public static String uploadBigFile(String filePath,
									   MultipartFile uploadFile,
									   OSS ossClient,
									   String bucketName,
									   String urlPrefix) {
		int part = calPartCount(uploadFile);
		String uploadId = initMultipartFileUpload(filePath, bucketName, ossClient);

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(128);
		executor.setMaxPoolSize(2147483647);
		executor.setQueueCapacity(2147483647);
		executor.setThreadNamePrefix("robot_upload_big_file_threadpool_");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setKeepAliveSeconds(300);
		executor.initialize();

		List<PartETag> eTags = Collections.synchronizedList(new ArrayList<>());

		final CountDownLatch latch = new CountDownLatch(part);
		for (int i = 0; i < part; i++) {
			long start = PART_SIZE * i;
			long curPartSize = Math.min(PART_SIZE, (uploadFile.getSize() - start));

			executor.execute(new UploadPartRunnable(latch,
				ossClient, bucketName, filePath, uploadFile,
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
			new CompleteMultipartUploadRequest(bucketName, filePath, uploadId, eTags);
		completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);
		try {
			ossClient.completeMultipartUpload(completeMultipartUploadRequest);
		} catch (OSSException | ClientException e) {
			e.printStackTrace();
			throw new RuntimeException("大文件上传失败, 文件合并未成功");
		}

		return urlPrefix + "/" + filePath;
	}

	public static String initMultipartFileUpload(String filePath,
												 String bucketName,
												 OSS ossClient) {
		InitiateMultipartUploadRequest multipartUploadRequest = new InitiateMultipartUploadRequest(
			bucketName, filePath);
		InitiateMultipartUploadResult initiateMultipartUploadResult = ossClient.initiateMultipartUpload(multipartUploadRequest);
		return initiateMultipartUploadResult.getUploadId();
	}

	/**
	 * 获取上传文件的md5
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getMd5(MultipartFile file) throws IOException {
		byte[] uploadBytes = file.getBytes();
		return DigestUtils.md5Hex(uploadBytes);
	}

	/**
	 * 生成路径以及文件名 例如：//robot/2019/04/28/15564277465972939.jpg
	 *
	 * @author dengtao
	 */
	private static String getFilePath(String sourceFileName, String md5) {
		LocalDateTime dateTime = LocalDateTime.now();
		String format = dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		return "robot" +
			"/" +
			format +
			"/" +
			md5 +
			"/" +
			sourceFileName;
	}


	/**
	 * 检查文件大小
	 *
	 * @author dengtao
	 */
	private static boolean checkFileSize(Long len, int size, String unit) {
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
	public static int calPartCount(MultipartFile uploadFile) {
		int part = (int) (uploadFile.getSize() / PART_SIZE);
		if (uploadFile.getSize() % PART_SIZE != 0) {
			part++;
		}
		return part;
	}

	public static class UploadPartRunnable implements Runnable {
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
								  OSS ossClient,
								  String bucketName,
								  String filePath,
								  MultipartFile uploadFile,
								  String uploadId,
								  int partNumber,
								  long start,
								  long partSize,
								  List<PartETag> eTags) {
			this.latch = latch;
			this.ossClient = ossClient;
			this.bucketName = bucketName;
			this.filePath = filePath;
			this.uploadFile = uploadFile;
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
}
