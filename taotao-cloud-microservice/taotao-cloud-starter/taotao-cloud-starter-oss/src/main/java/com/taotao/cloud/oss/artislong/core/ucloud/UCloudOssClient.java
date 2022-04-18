package com.taotao.cloud.oss.artislong.core.ucloud;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectApiBuilder;
import cn.ucloud.ufile.api.object.multi.MultiUploadInfo;
import cn.ucloud.ufile.api.object.multi.MultiUploadPartState;
import cn.ucloud.ufile.bean.DownloadFileBean;
import cn.ucloud.ufile.bean.ObjectInfoBean;
import cn.ucloud.ufile.bean.ObjectListBean;
import cn.ucloud.ufile.bean.ObjectProfile;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.util.StorageType;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.ucloud.model.UCloudOssConfig;
import com.taotao.cloud.oss.artislong.exception.OssException;
import com.taotao.cloud.oss.artislong.model.DirectoryOssInfo;
import com.taotao.cloud.oss.artislong.model.FileOssInfo;
import com.taotao.cloud.oss.artislong.model.OssInfo;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.taotao.cloud.oss.artislong.model.download.DownloadCheckPoint;
import com.taotao.cloud.oss.artislong.model.download.DownloadObjectStat;
import com.taotao.cloud.oss.artislong.model.upload.UpLoadCheckPoint;
import com.taotao.cloud.oss.artislong.model.upload.UpLoadFileStat;
import com.taotao.cloud.oss.artislong.model.upload.UpLoadPartEntityTag;
import com.taotao.cloud.oss.artislong.model.upload.UpLoadPartResult;
import com.taotao.cloud.oss.artislong.model.upload.UploadPart;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * https://docs.ucloud.cn/ufile/README
 */
public class UCloudOssClient implements StandardOssClient {

	public static final String OBJECT_OBJECT_NAME = "objectApiBuilder";

	private UfileClient ufileClient;
	private ObjectApiBuilder objectApiBuilder;
	private UCloudOssConfig uCloudOssConfig;

	public UCloudOssClient(UfileClient ufileClient, ObjectApiBuilder objectApiBuilder,
		UCloudOssConfig uCloudOssConfig) {
		this.ufileClient = ufileClient;
		this.objectApiBuilder = objectApiBuilder;
		this.uCloudOssConfig = uCloudOssConfig;
	}

	@Override
	public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
		String bucketName = getBucket();
		String key = getKey(targetName, false);

		if (isOverride || !isExist(targetName)) {
			try {
				objectApiBuilder.putObject(is, 0, "")
					.nameAs(key)
					.toBucket(bucketName)
					.execute();
			} catch (Exception e) {
				throw new OssException(e);
			}
		}
		return getInfo(targetName);
	}

	@Override
	public OssInfo upLoadCheckPoint(File file, String targetName) {
		uploadFile(file, targetName, uCloudOssConfig.getSliceConfig(), OssConstant.OssType.UCLOUD);
		return getInfo(targetName);
	}

	@Override
	public void completeUpload(UpLoadCheckPoint upLoadCheckPoint,
		List<UpLoadPartEntityTag> partEntityTags) {
		List<MultiUploadPartState> partStates = partEntityTags.stream()
			.sorted(Comparator.comparingInt(UpLoadPartEntityTag::getPartNumber))
			.map(partEntityTag -> {
				MultiUploadPartState multiUploadPartState = new MultiUploadPartState();
				// TODO
				return multiUploadPartState;
			}).collect(Collectors.toList());

		MultiUploadInfo multiUploadInfo = new MultiUploadInfo();
		objectApiBuilder.finishMultiUpload(multiUploadInfo, partStates);

		FileUtil.del(upLoadCheckPoint.getCheckpointFile());
	}

	@Override
	public void prepareUpload(UpLoadCheckPoint uploadCheckPoint, File upLoadFile, String targetName,
		String checkpointFile, SliceConfig slice) {
		String bucket = getBucket();
		String key = getKey(targetName, false);

		uploadCheckPoint.setMagic(UpLoadCheckPoint.UPLOAD_MAGIC);
		uploadCheckPoint.setUploadFile(upLoadFile.getPath());
		uploadCheckPoint.setKey(key);
		uploadCheckPoint.setBucket(bucket);
		uploadCheckPoint.setCheckpointFile(checkpointFile);
		uploadCheckPoint.setUploadFileStat(
			UpLoadFileStat.getFileStat(uploadCheckPoint.getUploadFile()));

		long partSize = slice.getPartSize();
		long fileLength = upLoadFile.length();
		int parts = (int) (fileLength / partSize);
		if (fileLength % partSize > 0) {
			parts++;
		}

		uploadCheckPoint.setUploadParts(
			splitUploadFile(uploadCheckPoint.getUploadFileStat().getSize(), partSize));
		uploadCheckPoint.setPartEntityTags(new ArrayList<>());
		uploadCheckPoint.setOriginPartSize(parts);

		try {
			MultiUploadInfo multiUploadInfo = objectApiBuilder.initMultiUpload(key, "", bucket)
				.withStorageType(StorageType.STANDARD).execute();
			uploadCheckPoint.setUploadId(multiUploadInfo.getUploadId());
		} catch (Exception e) {
			throw new OssException(e);
		}
	}

	@Override
	public UpLoadPartResult uploadPart(UpLoadCheckPoint upLoadCheckPoint, int partNum,
		InputStream inputStream) {
		UploadPart uploadPart = upLoadCheckPoint.getUploadParts().get(partNum);
		long partSize = uploadPart.getSize();
		UpLoadPartResult partResult = new UpLoadPartResult(partNum + 1, uploadPart.getOffset(),
			partSize);
		try {
			inputStream.skip(uploadPart.getOffset());

			MultiUploadInfo multiUploadInfo = new MultiUploadInfo();
			MultiUploadPartState multiUploadPartState = objectApiBuilder.multiUploadPart(
					multiUploadInfo, null, partNum)
				.from(null, Convert.toInt(uploadPart.getOffset()), Convert.toInt(partSize), partNum)
				.execute();

			partResult.setNumber(multiUploadPartState.getPartIndex());
			UpLoadPartEntityTag upLoadPartEntityTag = new UpLoadPartEntityTag();
			upLoadPartEntityTag.setETag(multiUploadPartState.geteTag());
			upLoadPartEntityTag.setPartNumber(multiUploadPartState.getPartIndex());
			partResult.setEntityTag(upLoadPartEntityTag);
		} catch (Exception e) {
			partResult.setFailed(true);
			partResult.setException(e);
		} finally {
			IoUtil.close(inputStream);
		}

		return partResult;
	}

	@Override
	public void downLoad(OutputStream os, String targetName) {
		ObjectProfile objectProfile = new ObjectProfile();
		objectProfile.setBucket(getBucket());
		objectProfile.setKeyName(getKey(targetName, false));
		try {
			DownloadFileBean downloadFileBean = objectApiBuilder.downloadFile(objectProfile)
				.execute();
			IoUtil.copy(FileUtil.getInputStream(downloadFileBean.getFile()), os);
		} catch (UfileClientException e) {
			throw new OssException(e);
		}
	}

	@Override
	public void downLoadCheckPoint(File localFile, String targetName) {
		downLoadFile(localFile, targetName, uCloudOssConfig.getSliceConfig(),
			OssConstant.OssType.UCLOUD);
	}

	@Override
	public DownloadObjectStat getDownloadObjectStat(String targetName) {
		try {
			ObjectProfile objectProfile = objectApiBuilder.objectProfile(getKey(targetName, false),
				getBucket()).execute();
			DateTime date = DateUtil.parse(objectProfile.getLastModified());
			long contentLength = objectProfile.getContentLength();
			String eTag = objectProfile.geteTag();
			DownloadObjectStat downloadObjectStat = new DownloadObjectStat();
			downloadObjectStat.setSize(contentLength);
			downloadObjectStat.setLastModified(date);
			downloadObjectStat.setDigest(eTag);
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
			Long partSize = uCloudOssConfig.getSliceConfig().getPartSize();
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
		ObjectProfile objectProfile = new ObjectProfile();
		objectProfile.setBucket(getBucket());
		objectProfile.setKeyName(key);
		try {
			DownloadFileBean downloadFileBean = objectApiBuilder.downloadFile(objectProfile)
				.withinRange(start, end).execute();
			return FileUtil.getInputStream(downloadFileBean.getFile());
		} catch (UfileClientException e) {
			throw new OssException(e);
		}
	}

	@Override
	public void delete(String targetName) {
		objectApiBuilder.deleteObject(getBucket(), getKey(targetName, false));
	}

	@Override
	public void copy(String sourceName, String targetName, Boolean isOverride) {
		String bucketName = getBucket();
		String targetKey = getKey(targetName, false);
		if (isOverride || !isExist(targetName)) {
			try {
				objectApiBuilder.copyObject(bucketName, getKey(sourceName, false))
					.copyTo(bucketName, targetKey).execute();
			} catch (Exception e) {
				throw new OssException(e);
			}
		}
	}

	@Override
	public OssInfo getInfo(String targetName, Boolean isRecursion) {
		String key = getKey(targetName, false);

		OssInfo ossInfo = getBaseInfo(key);
		ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName
			: FileNameUtil.getName(targetName));
		ossInfo.setPath(OssPathUtil.replaceKey(targetName, ossInfo.getName(), true));

		if (isRecursion && isDirectory(key)) {
			String prefix = OssPathUtil.convertPath(key, false);
			ObjectListBean objectListBean;
			try {
				objectListBean = objectApiBuilder.objectList(getBucket())
					.withPrefix(prefix.endsWith("/") ? prefix : prefix + CharPool.SLASH)
					.execute();
			} catch (Exception e) {
				throw new OssException(e);
			}

			List<ObjectInfoBean> objectList = objectListBean.getObjectList();

			List<OssInfo> fileOssInfos = new ArrayList<>();
			List<OssInfo> directoryInfos = new ArrayList<>();
			for (ObjectInfoBean objectInfoBean : objectList) {
				String fileName = objectInfoBean.getFileName();
				if (FileNameUtil.getName(fileName).equals(FileNameUtil.getName(key))) {
					ossInfo.setLastUpdateTime(DateUtil.date(objectInfoBean.getModifyTime())
						.toString(DatePattern.NORM_DATETIME_PATTERN));
					ossInfo.setCreateTime(DateUtil.date(objectInfoBean.getCreateTime())
						.toString(DatePattern.NORM_DATETIME_PATTERN));
					ossInfo.setLength(objectInfoBean.getSize());
				} else if (isDirectory(fileName)) {
					directoryInfos.add(
						getInfo(OssPathUtil.replaceKey(fileName, getBasePath(), false), true));
				} else {
					fileOssInfos.add(
						getInfo(OssPathUtil.replaceKey(fileName, getBasePath(), false), false));
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
		try {
			ObjectProfile objectProfile = objectApiBuilder.objectProfile(getKey(targetName, false),
				getBucket()).execute();
			return objectProfile.getContentLength() > 0;
		} catch (Exception e) {
			LogUtil.error("", e);
			return false;
		}
	}

	@Override
	public String getBasePath() {
		return uCloudOssConfig.getBasePath();
	}

	@Override
	public Map<String, Object> getClientObject() {
		return new HashMap<String, Object>() {
			{
				put(OBJECT_OBJECT_NAME, getObjectApiBuilder());
			}
		};
	}

	private String getBucket() {
		return uCloudOssConfig.getBucketName();
	}

	public OssInfo getBaseInfo(String key) {
		OssInfo ossInfo;

		if (isFile(key)) {
			ossInfo = new FileOssInfo();
			try {
				ObjectProfile objectProfile = getObjectApiBuilder().objectProfile(
					OssPathUtil.replaceKey(key, getBasePath(), false), getBucket()).execute();
				ossInfo.setLastUpdateTime(DateUtil.parse(objectProfile.getLastModified())
					.toString(DatePattern.NORM_DATETIME_PATTERN));
				ossInfo.setCreateTime(DateUtil.parse(objectProfile.getCreateTime())
					.toString(DatePattern.NORM_DATETIME_PATTERN));
				ossInfo.setLength(objectProfile.getContentLength());
			} catch (Exception e) {
				LogUtil.error("获取{}文件属性失败", key, e);
			}
		} else {
			ossInfo = new DirectoryOssInfo();
		}
		return ossInfo;
	}

	public UfileClient getUfileClient() {
		return ufileClient;
	}

	public void setUfileClient(UfileClient ufileClient) {
		this.ufileClient = ufileClient;
	}

	public ObjectApiBuilder getObjectApiBuilder() {
		return objectApiBuilder;
	}

	public void setObjectApiBuilder(ObjectApiBuilder objectApiBuilder) {
		this.objectApiBuilder = objectApiBuilder;
	}

	public UCloudOssConfig getuCloudOssConfig() {
		return uCloudOssConfig;
	}

	public void setuCloudOssConfig(
		UCloudOssConfig uCloudOssConfig) {
		this.uCloudOssConfig = uCloudOssConfig;
	}
}
