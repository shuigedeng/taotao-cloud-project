package com.taotao.cloud.oss.artislong.core.ali;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.HttpHeaders;
import com.aliyun.oss.model.DownloadFileRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.UploadFileRequest;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.ali.model.AliOssConfig;
import com.taotao.cloud.oss.artislong.exception.OssException;
import com.taotao.cloud.oss.artislong.model.DirectoryOssInfo;
import com.taotao.cloud.oss.artislong.model.FileOssInfo;
import com.taotao.cloud.oss.artislong.model.OssInfo;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://help.aliyun.com/product/31815.html
 */
public class AliOssClient implements StandardOssClient {

	public static final String OSS_OBJECT_NAME = "oss";

	private OSS oss;
	private AliOssConfig aliOssConfig;

	public AliOssClient(OSS oss, AliOssConfig aliOssConfig) {
		this.oss = oss;
		this.aliOssConfig = aliOssConfig;
	}

	@Override
	public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
		String bucketName = getBucketName();
		String key = getKey(targetName, false);

		if (isOverride || !oss.doesObjectExist(bucketName, key)) {
			oss.putObject(bucketName, key, is, new ObjectMetadata());
		}
		return getInfo(targetName);
	}

	/**
	 * 断点续传，使用SDK断点续传API实现，底层通过分块上传实现
	 *
	 * @param file       本地文件
	 * @param targetName 目标文件路径
	 * @return 文件信息
	 */
	@Override
	public OssInfo upLoadCheckPoint(File file, String targetName) {
		try {
			String bucketName = getBucketName();
			String key = getKey(targetName, false);

			UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
			String filePath = file.getPath();
			uploadFileRequest.setUploadFile(filePath);

			SliceConfig slice = aliOssConfig.getSliceConfig();
			uploadFileRequest.setTaskNum(slice.getTaskNum());
			uploadFileRequest.setPartSize(slice.getPartSize());

			uploadFileRequest.setEnableCheckpoint(true);

			String checkpointFile = filePath + StrUtil.DOT + OssConstant.OssType.ALI;
			uploadFileRequest.setCheckpointFile(checkpointFile);

			oss.uploadFile(uploadFileRequest);
		} catch (Throwable e) {
			throw new OssException(e);
		}
		return getInfo(targetName);
	}

	@Override
	public void downLoad(OutputStream os, String targetName) {
		String bucketName = getBucketName();
		String key = getKey(targetName, false);
		OSSObject ossObject = oss.getObject(bucketName, key);
		IoUtil.copy(ossObject.getObjectContent(), os);
		IoUtil.close(ossObject);
	}

	@Override
	public void downLoadCheckPoint(File localFile, String targetName) {
		String bucketName = getBucketName();
		String key = getKey(targetName, false);
		String filePath = localFile.getPath();

		DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName, key);
		downloadFileRequest.setDownloadFile(filePath);

		SliceConfig sliceConfig = aliOssConfig.getSliceConfig();
		downloadFileRequest.setPartSize(sliceConfig.getPartSize());
		downloadFileRequest.setTaskNum(sliceConfig.getTaskNum());
		downloadFileRequest.setEnableCheckpoint(true);

		String checkpointFile = filePath + StrUtil.DOT + OssConstant.OssType.ALI;
		downloadFileRequest.setCheckpointFile(checkpointFile);

		try {
			oss.downloadFile(downloadFileRequest);
		} catch (Throwable e) {
			LogUtil.error(e);
		}
	}

	@Override
	public void delete(String targetName) {
		oss.deleteObject(getBucketName(), getKey(targetName, false));
	}

	@Override
	public void copy(String sourceName, String targetName, Boolean isOverride) {
		String bucketName = getBucketName();
		String targetKey = getKey(targetName, false);
		if (isOverride || !oss.doesObjectExist(bucketName, targetKey)) {
			oss.copyObject(bucketName, getKey(sourceName, false), bucketName, targetKey);
		}
	}

	@Override
	public OssInfo getInfo(String targetName, Boolean isRecursion) {
		String bucketName = getBucketName();
		String key = getKey(targetName, false);

		OssInfo ossInfo = getBaseInfo(bucketName, key);
		ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName
			: FileNameUtil.getName(targetName));
		ossInfo.setPath(OssPathUtil.replaceKey(targetName, ossInfo.getName(), true));

		if (isRecursion && isDirectory(key)) {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
			listObjectsRequest.setDelimiter("/");
			String prefix = OssPathUtil.convertPath(key, false);
			listObjectsRequest.setPrefix(prefix.endsWith("/") ? prefix : prefix + CharPool.SLASH);
			ObjectListing listing = oss.listObjects(listObjectsRequest);

			List<OssInfo> fileOssInfos = new ArrayList<>();
			List<OssInfo> directoryInfos = new ArrayList<>();
			for (OSSObjectSummary ossObjectSummary : listing.getObjectSummaries()) {
				if (FileNameUtil.getName(ossObjectSummary.getKey())
					.equals(FileNameUtil.getName(key))) {
					ossInfo.setLastUpdateTime(DateUtil.date(ossObjectSummary.getLastModified())
						.toString(DatePattern.NORM_DATETIME_PATTERN));
					ossInfo.setCreateTime(DateUtil.date(ossObjectSummary.getLastModified())
						.toString(DatePattern.NORM_DATETIME_PATTERN));
					ossInfo.setLength(ossObjectSummary.getSize());
					ossInfo.setUrl(aliOssConfig.getEndpoint() + "/" + bucketName + "/" + key);
				} else {
					fileOssInfos.add(getInfo(
						OssPathUtil.replaceKey(ossObjectSummary.getKey(), getBasePath(), false),
						false));
				}
			}

			for (String commonPrefix : listing.getCommonPrefixes()) {
				String target = OssPathUtil.replaceKey(commonPrefix, getBasePath(), false);
				if (isDirectory(commonPrefix)) {
					directoryInfos.add(getInfo(target, true));
				} else {
					fileOssInfos.add(getInfo(target, false));
				}
			}
			if (ObjectUtil.isNotEmpty(fileOssInfos) && fileOssInfos.get(0) instanceof FileOssInfo) {
				ReflectUtil.setFieldValue(ossInfo, "fileInfos", fileOssInfos);
			}
			if (ObjectUtil.isNotEmpty(directoryInfos) && directoryInfos.get(
				0) instanceof DirectoryOssInfo) {
				ReflectUtil.setFieldValue(ossInfo, "directoryInfos", directoryInfos);
			}
		}

		return ossInfo;
	}

	@Override
	public Boolean isExist(String targetName) {
		return oss.doesObjectExist(getBucketName(), getKey(targetName, false));
	}

	@Override
	public String getBasePath() {
		return aliOssConfig.getBasePath();
	}

	@Override
	public Map<String, Object> getClientObject() {
		return new HashMap<String, Object>() {
			{
				put(OSS_OBJECT_NAME, getOss());
			}
		};
	}

	public String getBucketName() {
		return aliOssConfig.getBucketName();
	}

	public OssInfo getBaseInfo(String bucketName, String key) {
		OssInfo ossInfo;

		if (isFile(key)) {
			ossInfo = new FileOssInfo();
			try {
				ObjectMetadata objectMetadata = oss.getObjectMetadata(bucketName,
					OssPathUtil.replaceKey(key, "", false));
				ossInfo.setLastUpdateTime(DateUtil.date(
						(Date) objectMetadata.getRawMetadata().get(HttpHeaders.LAST_MODIFIED))
					.toString(DatePattern.NORM_DATETIME_PATTERN));
				ossInfo.setCreateTime(
					DateUtil.date((Date) objectMetadata.getRawMetadata().get(HttpHeaders.DATE))
						.toString(DatePattern.NORM_DATETIME_PATTERN));
				ossInfo.setLength(objectMetadata.getContentLength());
				ossInfo.setUrl(aliOssConfig.getEndpoint() + "/" + bucketName + "/" + key);
			} catch (Exception e) {
				LogUtil.error("获取{}文件属性失败", key, e);
			}
		} else {
			ossInfo = new DirectoryOssInfo();
		}
		return ossInfo;
	}


	public OSS getOss() {
		return oss;
	}

	public void setOss(OSS oss) {
		this.oss = oss;
	}

	public AliOssConfig getAliOssConfig() {
		return aliOssConfig;
	}

	public void setAliOssConfig(AliOssConfig aliOssConfig) {
		this.aliOssConfig = aliOssConfig;
	}
}
