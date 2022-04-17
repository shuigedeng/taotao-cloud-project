package com.taotao.cloud.oss.artislong.core.qiniu;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.qiniu.model.QiNiuOssConfig;
import com.taotao.cloud.oss.artislong.exception.OssException;
import com.taotao.cloud.oss.artislong.model.DirectoryOssInfo;
import com.taotao.cloud.oss.artislong.model.FileOssInfo;
import com.taotao.cloud.oss.artislong.model.OssInfo;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.taotao.cloud.oss.artislong.model.download.DownloadCheckPoint;
import com.taotao.cloud.oss.artislong.model.download.DownloadObjectStat;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://developer.qiniu.com/kodo
 *
 */
public class QiNiuOssClient implements StandardOssClient {

	public static final String AUTH_OBJECT_NAME = "auth";
	public static final String UPLOAD_OBJECT_NAME = "uploadManager";
	public static final String BUCKET_OBJECT_NAME = "bucketManager";

	private Auth auth;
	private UploadManager uploadManager;
	private BucketManager bucketManager;
	private QiNiuOssConfig qiNiuOssConfig;

	public QiNiuOssClient(Auth auth, UploadManager uploadManager, BucketManager bucketManager,
		QiNiuOssConfig qiNiuOssConfig) {
		this.auth = auth;
		this.uploadManager = uploadManager;
		this.bucketManager = bucketManager;
		this.qiNiuOssConfig = qiNiuOssConfig;
	}

	@Override
	public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
		try {
			uploadManager.put(is, getKey(targetName, false), getUpToken(), null, null);
		} catch (QiniuException e) {
			String errorMsg = String.format("%s上传失败", targetName);
			LogUtil.error(errorMsg, e);
			throw new OssException(errorMsg, e);
		}
		return getInfo(targetName, false);
	}

	@Override
	public OssInfo upLoadCheckPoint(File file, String targetName) {
		String key = getKey(targetName, false);

		SliceConfig sliceConfig = qiNiuOssConfig.getSliceConfig();

		Configuration cfg = new Configuration(qiNiuOssConfig.getRegion().buildRegion());
		// 指定分片上传版本
		cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
		// 设置分片上传并发，1：采用同步上传；大于1：采用并发上传
		cfg.resumableUploadMaxConcurrentTaskCount = sliceConfig.getTaskNum();
		cfg.resumableUploadAPIV2BlockSize = sliceConfig.getPartSize().intValue();

		try {
			FileRecorder fileRecorder = new FileRecorder(file.getParent());
			UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
			uploadManager.put(file.getPath(), key, getUpToken());
		} catch (Exception e) {
			String errorMsg = String.format("%s上传失败", targetName);
			LogUtil.error(errorMsg, e);
			throw new OssException(errorMsg, e);
		}
		return getInfo(targetName);
	}

	@Override
	public void downLoad(OutputStream os, String targetName) {
		DownloadUrl downloadUrl = new DownloadUrl("qiniu.com", false, getKey(targetName, false));
		try {
			String url = downloadUrl.buildURL();
			HttpUtil.download(url, os, false);
		} catch (QiniuException e) {
			String errorMsg = String.format("%s下载失败", targetName);
			LogUtil.error(errorMsg, e);
			throw new OssException(errorMsg, e);
		}
	}

	@Override
	public void downLoadCheckPoint(File localFile, String targetName) {
		downLoadFile(localFile, targetName, qiNiuOssConfig.getSliceConfig(),
			OssConstant.OssType.QINIU);
	}

	@Override
	public DownloadObjectStat getDownloadObjectStat(String targetName) {
		try {
			FileInfo fileInfo = bucketManager.stat(getBucket(), getKey(targetName, false));
			DownloadObjectStat downloadObjectStat = new DownloadObjectStat();
			downloadObjectStat.setSize(fileInfo.fsize);
			downloadObjectStat.setLastModified(DateUtil.date(fileInfo.putTime / 10000));
			downloadObjectStat.setDigest(fileInfo.md5);
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
			Long partSize = qiNiuOssConfig.getSliceConfig().getPartSize();
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
	public InputStream downloadPart(String key, long start, long end) {
		try {
			DownloadUrl downloadUrl = new DownloadUrl("qiniu.com", false, key);
			String url = downloadUrl.buildURL();
			HttpResponse response = HttpUtil.createGet(url, true)
				.timeout(-1)
				.header("Range", "bytes=" + start + "-" + end)
				.execute();
			LogUtil.debug("start={}, end={}", start, end);
			return new ByteArrayInputStream(response.bodyBytes());
		} catch (Exception e) {
			throw new OssException(e);
		}
	}

	@Override
	public void delete(String targetName) {
		try {
			bucketManager.delete(getBucket(), getKey(targetName, false));
		} catch (QiniuException e) {
			String errorMsg = String.format("%s删除失败", targetName);
			LogUtil.error(errorMsg, e);
			throw new OssException(errorMsg, e);
		}
	}

	@Override
	public void copy(String sourceName, String targetName, Boolean isOverride) {
		try {
			bucketManager.copy(getBucket(), getKey(sourceName, false), getBucket(),
				getKey(targetName, false), isOverride);
		} catch (QiniuException e) {
			String errorMsg = String.format("%s复制失败", targetName);
			LogUtil.error(errorMsg, e);
			throw new OssException(errorMsg, e);
		}
	}

	@Override
	public void move(String sourceName, String targetName, Boolean isOverride) {
		try {
			bucketManager.move(getBucket(), getKey(sourceName, false), getBucket(),
				getKey(targetName, false), isOverride);
		} catch (QiniuException e) {
			String errorMsg = String.format("%s移动到%s失败", sourceName, targetName);
			LogUtil.error(errorMsg, e);
			throw new OssException(errorMsg, e);
		}
	}

	@Override
	public void rename(String sourceName, String targetName, Boolean isOverride) {
		try {
			bucketManager.rename(getBucket(), getKey(sourceName, false), getKey(targetName, false),
				isOverride);
		} catch (QiniuException e) {
			String errorMsg = String.format("%s重命名为%s失败", sourceName, targetName);
			LogUtil.error(errorMsg, e);
			throw new OssException(errorMsg, e);
		}
	}

	@Override
	public OssInfo getInfo(String targetName, Boolean isRecursion) {
		String key = getKey(targetName, false);

		OssInfo ossInfo = getBaseInfo(targetName);
		if (isRecursion && isDirectory(key)) {
			FileListing listFiles = null;
			try {
				listFiles = bucketManager.listFiles(getBucket(), key, "", 1000, "/");
			} catch (QiniuException e) {
				LogUtil.error(e);
			}

			System.out.println(listFiles);
			List<OssInfo> fileOssInfos = new ArrayList<>();
			List<OssInfo> directoryInfos = new ArrayList<>();
			if (ObjectUtil.isNotEmpty(listFiles.items)) {
				for (FileInfo fileInfo : listFiles.items) {
					fileOssInfos.add(getInfo(
						OssPathUtil.replaceKey(fileInfo.key, getBasePath(), false), false));
				}
			}

			if (ObjectUtil.isNotEmpty(listFiles.commonPrefixes)) {
				for (String commonPrefix : listFiles.commonPrefixes) {
					String target = OssPathUtil.replaceKey(commonPrefix, getBasePath(), true);
					directoryInfos.add(getInfo(target, true));
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
	public String getBasePath() {
		return qiNiuOssConfig.getBasePath();
	}

	@Override
	public Map<String, Object> getClientObject() {
		return new HashMap<String, Object>() {
			{
				put(AUTH_OBJECT_NAME, getAuth());
				put(UPLOAD_OBJECT_NAME, getUploadManager());
				put(BUCKET_OBJECT_NAME, getBucketManager());
			}
		};
	}

	private String getUpToken() {
		return auth.uploadToken(getBucket());
	}

	private String getBucket() {
		return qiNiuOssConfig.getBucketName();
	}

	private OssInfo getBaseInfo(String targetName) {
		String key = getKey(targetName, false);
		OssInfo ossInfo;
		if (isFile(targetName)) {
			ossInfo = new FileOssInfo();
			try {
				FileInfo fileInfo = bucketManager.stat(getBucket(), key);
				String putTime = DateUtil.date(fileInfo.putTime / 10000)
					.toString(DatePattern.NORM_DATETIME_PATTERN);
				ossInfo.setLength(Convert.toStr(fileInfo.fsize));
				ossInfo.setCreateTime(putTime);
				ossInfo.setLastUpdateTime(putTime);
			} catch (QiniuException e) {
				String errorMsg = String.format("获取%s信息失败", targetName);
				LogUtil.error(errorMsg, e);
				throw new OssException(errorMsg, e);
			}
		} else {
			ossInfo = new DirectoryOssInfo();
		}

		ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName
			: FileNameUtil.getName(targetName));
		ossInfo.setPath(OssPathUtil.replaceKey(targetName, ossInfo.getName(), true));

		return ossInfo;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public UploadManager getUploadManager() {
		return uploadManager;
	}

	public void setUploadManager(UploadManager uploadManager) {
		this.uploadManager = uploadManager;
	}

	public BucketManager getBucketManager() {
		return bucketManager;
	}

	public void setBucketManager(BucketManager bucketManager) {
		this.bucketManager = bucketManager;
	}

	public QiNiuOssConfig getQiNiuOssConfig() {
		return qiNiuOssConfig;
	}

	public void setQiNiuOssConfig(
		QiNiuOssConfig qiNiuOssConfig) {
		this.qiNiuOssConfig = qiNiuOssConfig;
	}
}
