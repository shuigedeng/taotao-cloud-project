package com.taotao.cloud.oss.common.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.constant.OssConstant;
import com.taotao.cloud.oss.common.exception.OssException;
import com.taotao.cloud.oss.common.model.OssInfo;
import com.taotao.cloud.oss.common.model.SliceConfig;
import com.taotao.cloud.oss.common.model.UploadFileInfo;
import com.taotao.cloud.oss.common.model.download.DownloadCheckPoint;
import com.taotao.cloud.oss.common.model.download.DownloadObjectStat;
import com.taotao.cloud.oss.common.model.download.DownloadPart;
import com.taotao.cloud.oss.common.model.download.DownloadPartResult;
import com.taotao.cloud.oss.common.model.upload.UpLoadCheckPoint;
import com.taotao.cloud.oss.common.model.upload.UpLoadPartEntityTag;
import com.taotao.cloud.oss.common.model.upload.UpLoadPartResult;
import com.taotao.cloud.oss.common.model.upload.UploadPart;
import com.taotao.cloud.oss.common.util.FileUtil;
import com.taotao.cloud.oss.common.util.OssPathUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 标准操作系统客户端
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:35:46
 */
public interface StandardOssClient {

	/**
	 * 上传文件
	 *
	 * @param multipartFile 多部分文件
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:35:53
	 */
	default OssInfo upLoad(MultipartFile multipartFile) {
		try {
			UploadFileInfo uploadFileInfo = FileUtil.getMultipartFileInfo(
				multipartFile);
			OssInfo ossInfo = upLoad(multipartFile.getInputStream(), uploadFileInfo.getName());
			ossInfo.setUploadFileInfo(uploadFileInfo);
			return ossInfo;
		} catch (IOException e) {
			LogUtils.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 上传文件
	 *
	 * @param path 路径
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:35:55
	 */
	default OssInfo upLoad(String path) {
		return upLoad(new File(path));
	}

	/**
	 * 上传文件
	 *
	 * @param file 文件
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:35:58
	 */
	default OssInfo upLoad(File file) {
		try {
			UploadFileInfo uploadFileInfo = FileUtil.getFileInfo(
				file);
			OssInfo ossInfo = upLoad(new FileInputStream(file), uploadFileInfo.getName());
			ossInfo.setUploadFileInfo(uploadFileInfo);
			return ossInfo;
		} catch (IOException e) {
			LogUtils.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 上传文件，默认覆盖
	 *
	 * @param is         输入流
	 * @param targetName 目标文件路径
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:36:18
	 */
	default OssInfo upLoad(InputStream is, String targetName) {
		return upLoad(is, targetName, true);
	}

	/**
	 * 上传文件
	 *
	 * @param is         输入流
	 * @param targetName 目标文件路径
	 * @param isOverride 是否覆盖
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:36:21
	 */
	OssInfo upLoad(InputStream is, String targetName, Boolean isOverride);


	/**
	 * 断点续传
	 *
	 * @param file       本地文件路径
	 * @param targetName 目标文件路径
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:36:24
	 */
	default OssInfo upLoadCheckPoint(String file, String targetName) {
		return upLoadCheckPoint(new File(file), targetName);
	}

	/**
	 * 断点续传
	 *
	 * @param file       本地文件
	 * @param targetName 目标文件路径
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:36:27
	 */
	OssInfo upLoadCheckPoint(File file, String targetName);

	/**
	 * 断点续传上传
	 *
	 * @param upLoadFile 上传文件
	 * @param targetName 目标对象路径
	 * @param slice      分片参数
	 * @param ossType    OSS类型
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:36:29
	 */
	default OssInfo uploadFile(File upLoadFile, String targetName, SliceConfig slice,
							   String ossType) {
		String checkpointFile = upLoadFile.getPath() + StrUtil.DOT + ossType;

		UpLoadCheckPoint upLoadCheckPoint = new UpLoadCheckPoint();
		try {
			upLoadCheckPoint.load(checkpointFile);
		} catch (Exception e) {
			FileUtil.deleteFile(checkpointFile);
		}

		if (!upLoadCheckPoint.isValid()) {
			prepareUpload(upLoadCheckPoint, upLoadFile, targetName, checkpointFile, slice);
			FileUtil.deleteFile(checkpointFile);
		}

		ExecutorService executorService = Executors.newFixedThreadPool(slice.getTaskNum());
		List<Future<UpLoadPartResult>> futures = new ArrayList<>();

		for (int i = 0; i < upLoadCheckPoint.getUploadParts().size(); i++) {
			if (!upLoadCheckPoint.getUploadParts().get(i).isCompleted()) {
				futures.add(executorService.submit(new UploadPartTask(this, upLoadCheckPoint, i)));
			}
		}

		executorService.shutdown();

		for (Future<UpLoadPartResult> future : futures) {
			try {
				UpLoadPartResult partResult = future.get();
				if (partResult.isFailed()) {
					throw partResult.getException();
				}
			} catch (Exception e) {
				throw new OssException(e);
			}
		}

		try {
			if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			throw new OssException("关闭线程池失败", e);
		}

		List<UpLoadPartEntityTag> partEntityTags = upLoadCheckPoint.getPartEntityTags();
		completeUpload(upLoadCheckPoint, partEntityTags);

		return getInfo(targetName);
	}

	/**
	 * 完成上传
	 *
	 * @param upLoadCheckPoint 检查负载点
	 * @param partEntityTags   实体标记部分
	 * @since 2022-04-27 17:36:35
	 */
	default void completeUpload(UpLoadCheckPoint upLoadCheckPoint,
								List<UpLoadPartEntityTag> partEntityTags) {
		FileUtil.deleteFile(upLoadCheckPoint.getCheckpointFile());
	}

	/**
	 * 准备上传
	 *
	 * @param uploadCheckPoint 上传检查
	 * @param upLoadFile       加载文件
	 * @param targetName       目标名称
	 * @param checkpointFile   检查点文件
	 * @param slice            片
	 * @since 2022-04-27 17:36:37
	 */
	default void prepareUpload(UpLoadCheckPoint uploadCheckPoint, File upLoadFile,
							   String targetName, String checkpointFile, SliceConfig slice) {
		throw new OssException("初始化断点续传对象未实现，默认不支持此方法");
	}

	/**
	 * 拆分上传分片
	 *
	 * @param fileSize 文件大小
	 * @param partSize 分片大小
	 * @return {@link ArrayList }<{@link UploadPart }>
	 * @since 2022-04-27 17:36:44
	 */
	default ArrayList<UploadPart> splitUploadFile(long fileSize, long partSize) {
		ArrayList<UploadPart> parts = new ArrayList<>();

		long partNum = fileSize / partSize;
		if (partNum >= OssConstant.DEFAULT_PART_NUM) {
			partSize = fileSize / (OssConstant.DEFAULT_PART_NUM - 1);
			partNum = fileSize / partSize;
		}

		for (long i = 0; i < partNum; i++) {
			UploadPart part = new UploadPart();
			part.setNumber((int) (i + 1));
			part.setOffset(i * partSize);
			part.setSize(partSize);
			part.setCompleted(false);
			parts.add(part);
		}

		if (fileSize % partSize > 0) {
			UploadPart part = new UploadPart();
			part.setNumber(parts.size() + 1);
			part.setOffset(parts.size() * partSize);
			part.setSize(fileSize % partSize);
			part.setCompleted(false);
			parts.add(part);
		}

		return parts;
	}

	/**
	 * 分片上传Task
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-04-27 17:36:49
	 */
	class UploadPartTask implements Callable<UpLoadPartResult> {

		/**
		 * OSS客户端
		 */
		private StandardOssClient ossClient;
		/**
		 * 断点续传对象
		 */
		private UpLoadCheckPoint upLoadCheckPoint;
		/**
		 * 分片索引
		 */
		private int partNum;

		public UploadPartTask(StandardOssClient ossClient, UpLoadCheckPoint upLoadCheckPoint,
							  int partNum) {
			this.ossClient = ossClient;
			this.upLoadCheckPoint = upLoadCheckPoint;
			this.partNum = partNum;
		}

		@Override
		public UpLoadPartResult call() {
			InputStream inputStream =  cn.hutool.core.io.FileUtil.getInputStream(upLoadCheckPoint.getUploadFile());
			UpLoadPartResult upLoadPartResult = ossClient.uploadPart(upLoadCheckPoint, partNum,
				inputStream);
			if (!upLoadPartResult.isFailed()) {
				upLoadCheckPoint.update(partNum, upLoadPartResult.getEntityTag(), true);
				upLoadCheckPoint.dump();
			}
			return upLoadPartResult;
		}

		public StandardOssClient getOssClient() {
			return ossClient;
		}

		public void setOssClient(StandardOssClient ossClient) {
			this.ossClient = ossClient;
		}

		public UpLoadCheckPoint getUpLoadCheckPoint() {
			return upLoadCheckPoint;
		}

		public void setUpLoadCheckPoint(
			UpLoadCheckPoint upLoadCheckPoint) {
			this.upLoadCheckPoint = upLoadCheckPoint;
		}

		public int getPartNum() {
			return partNum;
		}

		public void setPartNum(int partNum) {
			this.partNum = partNum;
		}
	}

	/**
	 * 上传分片
	 *
	 * @param upLoadCheckPoint 断点续传对象
	 * @param partNum          分片索引
	 * @param inputStream      输入流
	 * @return {@link UpLoadPartResult }
	 * @since 2022-04-27 17:36:54
	 */
	default UpLoadPartResult uploadPart(UpLoadCheckPoint upLoadCheckPoint, int partNum,
										InputStream inputStream) {
		UploadPart uploadPart = upLoadCheckPoint.getUploadParts().get(partNum);
		long partSize = uploadPart.getSize();
		UpLoadPartResult partResult = new UpLoadPartResult(partNum + 1, uploadPart.getOffset(),
			partSize);
		partResult.setFailed(true);
		return partResult;
	}

	/**
	 * 下载文件
	 *
	 * @param os         输出流
	 * @param targetName 目标文件路径
	 * @since 2022-04-27 17:36:58
	 */
	void downLoad(OutputStream os, String targetName);

	/**
	 * 断点续传
	 *
	 * @param localFile  本地文件路径
	 * @param targetName 目标文件路径
	 * @since 2022-04-27 17:37:00
	 */
	default void downLoadCheckPoint(String localFile, String targetName) {
		downLoadCheckPoint(new File(localFile), targetName);
	}

	/**
	 * 断点续传
	 *
	 * @param localFile  本地文件
	 * @param targetName 目标文件路径
	 * @since 2022-04-27 17:37:02
	 */
	void downLoadCheckPoint(File localFile, String targetName);

	/**
	 * 断点续传下载
	 *
	 * @param localFile  本地文件
	 * @param targetName 目标对象
	 * @param slice      分片参数
	 * @param ossType    OSS类型
	 * @since 2022-04-27 17:37:05
	 */
	default void downLoadFile(File localFile, String targetName, SliceConfig slice,
							  String ossType) {

		String checkpointFile = localFile.getPath() + StrUtil.DOT + ossType;

		DownloadCheckPoint downloadCheckPoint = new DownloadCheckPoint();
		try {
			downloadCheckPoint.load(checkpointFile);
		} catch (Exception e) {
			FileUtil.deleteFile(checkpointFile);
		}

		DownloadObjectStat downloadObjectStat = getDownloadObjectStat(targetName);
		if (!downloadCheckPoint.isValid(downloadObjectStat)) {
			prepareDownload(downloadCheckPoint, localFile, targetName, checkpointFile);
			FileUtil.deleteFile(checkpointFile);
		}

		ExecutorService executorService = Executors.newFixedThreadPool(slice.getTaskNum());
		List<Future<DownloadPartResult>> futures = new ArrayList<>();

		for (int i = 0; i < downloadCheckPoint.getDownloadParts().size(); i++) {
			if (!downloadCheckPoint.getDownloadParts().get(i).isCompleted()) {
				futures.add(
					executorService.submit(new DownloadPartTask(this, downloadCheckPoint, i)));
			}
		}

		executorService.shutdown();

		for (Future<DownloadPartResult> future : futures) {
			try {
				DownloadPartResult partResult = future.get();
				if (partResult.isFailed()) {
					throw partResult.getException();
				}
			} catch (Exception e) {
				throw new OssException(e);
			}
		}

		try {
			if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			throw new OssException("关闭线程池失败", e);
		}

		cn.hutool.core.io.FileUtil.rename(new File(downloadCheckPoint.getTempDownloadFile()),
			downloadCheckPoint.getDownloadFile(), true);
		cn.hutool.core.io.FileUtil.del(downloadCheckPoint.getCheckPointFile());
	}

	/**
	 * 分片下载Task
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-04-27 17:37:11
	 */
	public class DownloadPartTask implements Callable<DownloadPartResult> {

		/**
		 * Oss客户端
		 */
		StandardOssClient ossClient;
		/**
		 * 断点续传对象
		 */
		DownloadCheckPoint downloadCheckPoint;
		/**
		 * 分片索引
		 */
		int partNum;

		public DownloadPartTask(StandardOssClient ossClient, DownloadCheckPoint downloadCheckPoint,
								int partNum) {
			this.ossClient = ossClient;
			this.downloadCheckPoint = downloadCheckPoint;
			this.partNum = partNum;
		}

		@Override
		public DownloadPartResult call() {
			DownloadPartResult partResult = null;
			RandomAccessFile output = null;
			InputStream content = null;
			try {
				DownloadPart downloadPart = downloadCheckPoint.getDownloadParts().get(partNum);

				partResult = new DownloadPartResult(partNum + 1, downloadPart.getStart(),
					downloadPart.getEnd());

				output = new RandomAccessFile(downloadCheckPoint.getTempDownloadFile(), "rw");
				output.seek(downloadPart.getFileStart());

				content = ossClient.downloadPart(downloadCheckPoint.getKey(),
					downloadPart.getStart(), downloadPart.getEnd());

				long partSize = downloadPart.getEnd() - downloadPart.getStart();
				byte[] buffer = new byte[Convert.toInt(partSize)];
				int bytesRead = 0;
				while ((bytesRead = content.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}

				partResult.setLength(downloadPart.getLength());
				downloadCheckPoint.update(partNum, true);
				downloadCheckPoint.dump();
			} catch (Exception e) {
				partResult.setException(e);
				partResult.setFailed(true);
			} finally {
				IoUtil.close(output);
				IoUtil.close(content);
			}
			return partResult;
		}

		public StandardOssClient getOssClient() {
			return ossClient;
		}

		public void setOssClient(StandardOssClient ossClient) {
			this.ossClient = ossClient;
		}

		public DownloadCheckPoint getDownloadCheckPoint() {
			return downloadCheckPoint;
		}

		public void setDownloadCheckPoint(
			DownloadCheckPoint downloadCheckPoint) {
			this.downloadCheckPoint = downloadCheckPoint;
		}

		public int getPartNum() {
			return partNum;
		}

		public void setPartNum(int partNum) {
			this.partNum = partNum;
		}
	}

	/**
	 * 获取目标文件状态
	 *
	 * @param targetName 目标对象路径
	 * @return {@link DownloadObjectStat }
	 * @since 2022-04-27 17:37:17
	 */
	default DownloadObjectStat getDownloadObjectStat(String targetName) {
		throw new OssException("获取下载对象状态方法未实现，默认不支持此方法");
	}

	/**
	 * 初始化断点续传下载对象
	 *
	 * @param downloadCheckPoint 断点对象
	 * @param localFile          本地文件
	 * @param targetName         目标对象路径
	 * @param checkpointFile     下载进度缓存文件
	 * @since 2022-04-27 17:37:19
	 */
	default void prepareDownload(DownloadCheckPoint downloadCheckPoint, File localFile,
								 String targetName, String checkpointFile) {
		throw new OssException("初始化断点续传下载对象方法未实现，默认不支持此方法");
	}

	/**
	 * 拆分文件分片
	 *
	 * @param start      开始字节数
	 * @param objectSize 对象大小
	 * @param partSize   预设分片大小
	 * @return {@link ArrayList }<{@link DownloadPart }>
	 * @since 2022-04-27 17:37:22
	 */
	default ArrayList<DownloadPart> splitDownloadFile(long start, long objectSize, long partSize) {
		ArrayList<DownloadPart> parts = new ArrayList<>();

		long partNum = objectSize / partSize;
		if (partNum >= OssConstant.DEFAULT_PART_NUM) {
			partSize = objectSize / (OssConstant.DEFAULT_PART_NUM - 1);
		}

		long offset = 0L;
		for (int i = 0; offset < objectSize; offset += partSize, i++) {
			DownloadPart part = new DownloadPart();
			part.setIndex(i);
			part.setStart(offset + start);
			part.setEnd(getPartEnd(offset, objectSize, partSize) + start);
			part.setFileStart(offset);
			parts.add(part);
		}

		return parts;
	}

	/**
	 * 获取分片结束字节数
	 *
	 * @param begin 开始字节数
	 * @param total 目标对象大小
	 * @param per   预设分片大小
	 * @return long
	 * @since 2022-04-27 17:37:25
	 */
	default long getPartEnd(long begin, long total, long per) {
		if (begin + per > total) {
			return total - 1;
		}
		return begin + per - 1;
	}

	/**
	 * 拆分单独一个文件分片
	 *
	 * @return {@link ArrayList }<{@link DownloadPart }>
	 * @since 2022-04-27 17:37:27
	 */
	default ArrayList<DownloadPart> splitDownloadOneFile() {
		ArrayList<DownloadPart> parts = new ArrayList<>();
		DownloadPart part = new DownloadPart();
		part.setIndex(0);
		part.setStart(0);
		part.setEnd(-1);
		part.setFileStart(0);
		parts.add(part);
		return parts;
	}

	/**
	 * 获取下载分片范围
	 *
	 * @param range     分片
	 * @param totalSize 目标文件大小
	 * @return {@link long[] }
	 * @since 2022-04-27 17:37:30
	 */
	default long[] getDownloadSlice(long[] range, long totalSize) {
		long start = 0;
		long size = totalSize;

		if ((range == null) ||
			(range.length != 2) ||
			(totalSize < 1) ||
			(range[0] < 0 && range[1] < 0) ||
			(range[0] > 0 && range[1] > 0 && range[0] > range[1]) ||
			(range[0] >= totalSize)) {
			//download all
		} else {
			//dwonload part by range & total size
			long begin = range[0];
			long end = range[1];
			if (range[0] < 0) {
				begin = 0;
			}
			if (range[1] < 0 || range[1] >= totalSize) {
				end = totalSize - 1;
			}
			start = begin;
			size = end - begin + 1;
		}

		return new long[]{start, size};
	}

	/**
	 * 创建下载缓存文件
	 *
	 * @param downloadTempFile 下载缓存文件
	 * @param length           文件大小
	 * @since 2022-04-27 17:37:34
	 */
	default void createDownloadTemp(String downloadTempFile, long length) {
		File file = new File(downloadTempFile);
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(file, "rw");
			rf.setLength(length);
		} catch (Exception e) {
			throw new OssException("创建下载缓存文件失败");
		} finally {
			IoUtil.close(rf);
		}
	}

	/**
	 * 下载分片
	 *
	 * @param key   目标文件
	 * @param start 文件开始字节
	 * @param end   文件结束字节
	 * @return {@link InputStream }
	 * @since 2022-04-27 17:37:36
	 */
	default InputStream downloadPart(String key, long start, long end) throws Exception {
		throw new OssException("下载文件分片方法未实现，默认不支持此方法");
	}

	/**
	 * 删除文件
	 *
	 * @param targetName 目标文件路径
	 * @since 2022-04-27 17:37:39
	 */
	void delete(String targetName);

	/**
	 * 复制文件，默认覆盖
	 *
	 * @param sourceName 源文件路径
	 * @param targetName 目标文件路径
	 * @since 2022-04-27 17:37:41
	 */
	default void copy(String sourceName, String targetName) {
		copy(sourceName, targetName, true);
	}

	/**
	 * 复制文件
	 *
	 * @param sourceName 源文件路径
	 * @param targetName 目标文件路径
	 * @param isOverride 是否覆盖
	 * @since 2022-04-27 17:37:43
	 */
	void copy(String sourceName, String targetName, Boolean isOverride);

	/**
	 * 移动文件，默认覆盖
	 *
	 * @param sourceName 源文件路径
	 * @param targetName 目标路径
	 * @since 2022-04-27 17:37:45
	 */
	default void move(String sourceName, String targetName) {
		move(sourceName, targetName, true);
	}

	/**
	 * 移动文件
	 *
	 * @param sourceName 源文件路径
	 * @param targetName 目标路径
	 * @param isOverride 是否覆盖
	 * @since 2022-04-27 17:37:47
	 */
	default void move(String sourceName, String targetName, Boolean isOverride) {
		copy(sourceName, targetName, isOverride);
		delete(sourceName);
	}

	/**
	 * 重命名文件
	 *
	 * @param sourceName 源文件路径
	 * @param targetName 目标文件路径
	 * @since 2022-04-27 17:37:50
	 */
	default void rename(String sourceName, String targetName) {
		rename(sourceName, targetName, true);
	}

	/**
	 * 重命名文件
	 *
	 * @param sourceName 源文件路径
	 * @param targetName 目标路径
	 * @param isOverride 是否覆盖
	 * @since 2022-04-27 17:37:52
	 */
	default void rename(String sourceName, String targetName, Boolean isOverride) {
		move(sourceName, targetName, isOverride);
	}

	/**
	 * 获取文件信息，默认获取目标文件信息
	 *
	 * @param targetName 目标文件路径
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:37:54
	 */
	default OssInfo getInfo(String targetName) {
		return getInfo(targetName, false);
	}

	/**
	 * 获取文件信息 isRecursion传false，则只获取当前对象信息； isRecursion传true，且当前对象为目录时，会递归获取当前路径下所有文件及目录，按层级返回
	 *
	 * @param targetName  目标文件路径
	 * @param isRecursion 是否递归
	 * @return {@link OssInfo }
	 * @since 2022-04-27 17:37:56
	 */
	OssInfo getInfo(String targetName, Boolean isRecursion);

	/**
	 * 是否存在
	 *
	 * @param targetName 目标文件路径
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:37:58
	 */
	default Boolean isExist(String targetName) {
		OssInfo info = getInfo(targetName);
		return ObjectUtil.isNotEmpty(info) && ObjectUtil.isNotEmpty(info.getName());
	}

	/**
	 * 是否为文件 默认根据路径最后一段名称是否有后缀名来判断是否为文件，此方式不准确，当存储平台不提供类似方法时，可使用此方法
	 *
	 * @param targetName 目标文件路径
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:38:01
	 */
	default Boolean isFile(String targetName) {
		String name = FileNameUtil.getName(targetName);
		return StrUtil.indexOf(name, StrUtil.C_DOT) > 0;
	}

	/**
	 * 是否为目录 与判断是否为文件相反
	 *
	 * @param targetName 目标文件路径
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:38:03
	 */
	default Boolean isDirectory(String targetName) {
		return !isFile(targetName);
	}

	/**
	 * 获取客户端对象
	 *
	 * @return {@link Map }<{@link String }, {@link Object }>
	 * @since 2022-04-27 17:38:05
	 */
	Map<String, Object> getClientObject();

	/**
	 * 获取完整Key
	 *
	 * @param targetName 目标地址
	 * @param isAbsolute 是否绝对路径 true：绝对路径；false：相对路径
	 * @return {@link String }
	 * @since 2022-04-27 17:38:08
	 */
	default String getKey(String targetName, Boolean isAbsolute) {
		String key = OssPathUtil.convertPath(getBasePath() + targetName, isAbsolute);
		if (cn.hutool.core.io.FileUtil.isWindows() && isAbsolute) {
			if (key.contains(StrUtil.COLON) && key.startsWith(StrUtil.SLASH)) {
				key = key.substring(1);
			}
		}
		return key;
	}

	/**
	 * 获取文件存储根路径
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:38:13
	 */
	String getBasePath();

}
