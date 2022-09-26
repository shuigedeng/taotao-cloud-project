package com.taotao.cloud.oss.minio.support;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.io.ByteStreams;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.constant.OssConstant;
import com.taotao.cloud.oss.common.exception.OssException;
import com.taotao.cloud.oss.common.model.DirectoryOssInfo;
import com.taotao.cloud.oss.common.model.FileOssInfo;
import com.taotao.cloud.oss.common.model.OssInfo;
import com.taotao.cloud.oss.common.model.download.DownloadCheckPoint;
import com.taotao.cloud.oss.common.model.download.DownloadObjectStat;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import com.taotao.cloud.oss.common.util.OssPathUtil;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import okhttp3.Headers;
import org.apache.http.HttpHeaders;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@link <a href="http://docs.minio.org.cn/docs/master/minio-monitoring-guide">...</a>" }
 * <p>
 * {@link <a href="https://docs.minio.org.cn/docs/master/minio-monitoring-guide"></a>}
 * <p>
 * {@link <a href="https://docs.min.io/">...</a>}
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:49
 */
public class MinioOssClient implements StandardOssClient {

	public static final String MINIO_OBJECT_NAME = "minioClient";

	private MinioClient minioClient;
	private MinioOssConfig minioOssConfig;

	public MinioOssClient(MinioClient minioClient, MinioOssConfig minioOssConfig) {
		this.minioClient = minioClient;
		this.minioOssConfig = minioOssConfig;
	}

	@Override
	public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
		try {
			String bucket = getBucket();
			String key = getKey(targetName, false);
			ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
				.bucket(bucket)
				.object(key)
				.stream(is, is.available(), -1)
				.build());
			LogUtils.info("minio objectWriteResponse ----- etag: {}, object: {}, versionId:{}, headers: {}", objectWriteResponse.etag(), objectWriteResponse.object(), objectWriteResponse.versionId(), objectWriteResponse.headers());
		} catch (Exception e) {
			throw new OssException(e);
		}
		return getInfo(targetName);
	}

	@Override
	public OssInfo upLoadCheckPoint(File file, String targetName) {
		try (InputStream inputStream = FileUtil.getInputStream(file)) {
			upLoad(inputStream, targetName, true);
		} catch (Exception e) {
			throw new OssException(e);
		}
		return getInfo(targetName);
	}

	@Override
	public void downLoad(OutputStream os, String targetName) {
		GetObjectResponse is = null;
		try {
			GetObjectArgs getObjectArgs = GetObjectArgs.builder()
				.bucket(getBucket())
				.object(getKey(targetName, true))
				.build();
			is = minioClient.getObject(getObjectArgs);
			ByteStreams.copy(is, os);
		} catch (Exception e) {
			throw new OssException(e);
		} finally {
			IoUtil.close(is);
		}
	}

	@Override
	public void downLoadCheckPoint(File localFile, String targetName) {
		downLoadFile(localFile, targetName, minioOssConfig.getSliceConfig(),
			OssConstant.OssType.MINIO);
	}

	@Override
	public DownloadObjectStat getDownloadObjectStat(String targetName) {
		try {
			StatObjectArgs statObjectArgs = StatObjectArgs.builder().bucket(getBucket())
				.object(getKey(targetName, true)).build();
			StatObjectResponse statObjectResponse = minioClient.statObject(statObjectArgs);
			long contentLength = statObjectResponse.size();
			String eTag = statObjectResponse.etag();
			DownloadObjectStat downloadObjectStat = new DownloadObjectStat();
			downloadObjectStat.setSize(contentLength);
			downloadObjectStat.setDigest(eTag);
			downloadObjectStat.setLastModified(
				Date.from(statObjectResponse.lastModified().toInstant()));
			return downloadObjectStat;
		} catch (Exception e) {
			throw new OssException(e);
		}
	}

	@Override
	public void prepareDownload(DownloadCheckPoint downloadCheckPoint, File localFile,
								String targetName, String checkpointFile) {
		downloadCheckPoint.setMagic(DownloadCheckPoint.DOWNLOAD_MAGIC);
		downloadCheckPoint.setDownloadFile(localFile.getPath());
		downloadCheckPoint.setBucketName(getBucket());
		downloadCheckPoint.setKey(getKey(targetName, false));
		downloadCheckPoint.setCheckPointFile(checkpointFile);

		downloadCheckPoint.setObjectStat(getDownloadObjectStat(targetName));

		long downloadSize;
		if (downloadCheckPoint.getObjectStat().getSize() > 0) {
			Long partSize = minioOssConfig.getSliceConfig().getPartSize();
			long[] slice = getDownloadSlice(new long[0],
				downloadCheckPoint.getObjectStat().getSize());
			downloadCheckPoint.setDownloadParts(splitDownloadFile(slice[0], slice[1], partSize));
			downloadSize = slice[1];
		} else {
			//download whole file
			downloadSize = 0;
			downloadCheckPoint.setDownloadParts(splitDownloadOneFile());
		}
		downloadCheckPoint.setOriginPartSize(downloadCheckPoint.getDownloadParts().size());
		createDownloadTemp(downloadCheckPoint.getTempDownloadFile(), downloadSize);
	}

	@Override
	public InputStream downloadPart(String key, long start, long end) throws Exception {
		GetObjectArgs getObjectArgs = GetObjectArgs.builder()
			.bucket(getBucket())
			.object(key)
			// 起始字节的位置
			.offset(start)
			// 要读取的长度 (可选，如果无值则代表读到文件结尾)。
			.length(end)
			.build();
		return minioClient.getObject(getObjectArgs);
	}

	@Override
	public void delete(String targetName) {
		try {
			RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
				.bucket(getBucket())
				.object(getKey(targetName, true))
				.build();
			minioClient.removeObject(removeObjectArgs);
		} catch (Exception e) {
			throw new OssException(e);
		}
	}

	@Override
	public void copy(String sourceName, String targetName, Boolean isOverride) {
		try {
			CopyObjectArgs copyObjectArgs = CopyObjectArgs.builder()
				.bucket(getBucket())
				.object(getKey(targetName, true))
				.source(CopySource.builder()
					.bucket(getBucket())
					.object(getKey(sourceName, true))
					.build())
				.build();
			minioClient.copyObject(copyObjectArgs);
		} catch (Exception e) {
			throw new OssException(e);
		}
	}

	@Override
	public OssInfo getInfo(String targetName, Boolean isRecursion) {
		try {
			String key = getKey(targetName, false);

			OssInfo ossInfo = getBaseInfo(targetName);
			if (isRecursion && isDirectory(key)) {

				String prefix = OssPathUtil.convertPath(key, true);
				ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
					.bucket(getBucket())
					.delimiter("/")
					.prefix(prefix.endsWith("/") ? prefix : prefix + CharPool.SLASH)
					.build();
				Iterable<Result<Item>> results = minioClient.listObjects(listObjectsArgs);

				List<OssInfo> fileOssInfos = new ArrayList<>();
				List<OssInfo> directoryInfos = new ArrayList<>();

				for (Result<Item> result : results) {
					Item item = result.get();
					String childKey = OssPathUtil.replaceKey(item.objectName(), getBasePath(), true);
					if (item.isDir()) {
						directoryInfos.add(getInfo(childKey, true));
					} else {
						fileOssInfos.add(getInfo(childKey, false));
					}
				}

				if (ObjectUtil.isNotEmpty(fileOssInfos) && fileOssInfos.get(0) instanceof FileOssInfo) {
					ReflectUtil.setFieldValue(ossInfo, "fileInfos", fileOssInfos);
				}
				if (ObjectUtil.isNotEmpty(directoryInfos) && directoryInfos.get(0) instanceof DirectoryOssInfo) {
					ReflectUtil.setFieldValue(ossInfo, "directoryInfos", directoryInfos);
				}
			}
			return ossInfo;
		} catch (Exception e) {
			throw new OssException(e);
		}
	}

	@Override
	public String getBasePath() {
		return minioOssConfig.getBasePath();
	}

	@Override
	public Map<String, Object> getClientObject() {
		return new HashMap<>() {
			{
				put(MINIO_OBJECT_NAME, getMinioClient());
			}
		};
	}

	private String getBucket() {
		return minioOssConfig.getBucketName();
	}

	public OssInfo getBaseInfo(String targetName) {
		String key = getKey(targetName, false);
		OssInfo ossInfo;
		String bucketName = getBucket();
		if (isFile(key)) {
			ossInfo = new FileOssInfo();
			try {
				GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(key)
					.build();
				GetObjectResponse objectResponse = minioClient.getObject(getObjectArgs);
				Headers headers = objectResponse.headers();

				ossInfo.setCreateTime(DateUtil.date(headers.getDate(HttpHeaders.DATE))
					.toString(DatePattern.NORM_DATETIME_PATTERN));
				ossInfo.setLastUpdateTime(DateUtil.date(headers.getDate(HttpHeaders.LAST_MODIFIED))
					.toString(DatePattern.NORM_DATETIME_PATTERN));
				ossInfo.setLength(Long.valueOf(Objects.requireNonNull(headers.get(HttpHeaders.CONTENT_LENGTH))));
				ossInfo.setUrl(minioOssConfig.getEndpoint() + "/" + bucketName + key);
			} catch (Exception e) {
				LogUtils.error("获取{}文件属性失败", key, e);
			}
		} else {
			ossInfo = new DirectoryOssInfo();
		}
		ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName : FileNameUtil.getName(targetName));
		ossInfo.setPath(OssPathUtil.replaceKey(targetName, minioOssConfig.getBasePath(), true));
		return ossInfo;
	}

	public MinioClient getMinioClient() {
		return minioClient;
	}

	public void setMinioClient(MinioClient minioClient) {
		this.minioClient = minioClient;
	}

	public MinioOssConfig getMinioOssConfig() {
		return minioOssConfig;
	}

	public void setMinioOssConfig(
		MinioOssConfig minioOssConfig) {
		this.minioOssConfig = minioOssConfig;
	}
}
