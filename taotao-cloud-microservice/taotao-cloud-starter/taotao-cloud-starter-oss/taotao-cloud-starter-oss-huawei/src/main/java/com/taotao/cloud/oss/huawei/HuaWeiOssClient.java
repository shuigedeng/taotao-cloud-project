package com.taotao.cloud.oss.huawei;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.obs.services.ObsClient;
import com.obs.services.model.DownloadFileRequest;
import com.obs.services.model.ListObjectsRequest;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsObject;
import com.obs.services.model.UploadFileRequest;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.constant.OssConstant;
import com.taotao.cloud.oss.common.model.DirectoryOssInfo;
import com.taotao.cloud.oss.common.model.FileOssInfo;
import com.taotao.cloud.oss.common.model.OssInfo;
import com.taotao.cloud.oss.common.model.SliceConfig;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import com.taotao.cloud.oss.common.util.OssPathUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://support.huaweicloud.com/obs/index.html">https://support.huaweicloud.com/obs/index.html</a>
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:17
 */
public class HuaWeiOssClient implements StandardOssClient {

    public static final String OBS_OBJECT_NAME = "obsClient";

    private ObsClient obsClient;
    private HuaweiOssConfig huaweiOssConfig;

	public HuaWeiOssClient(ObsClient obsClient, HuaweiOssConfig huaweiOssConfig) {
		this.obsClient = obsClient;
		this.huaweiOssConfig = huaweiOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        String bucket = getBucket();
        String key = getKey(targetName, false);
        if (isOverride || !obsClient.doesObjectExist(bucket, key)) {
            obsClient.putObject(bucket, key, is);
        }
        return getInfo(targetName);
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        String bucket = getBucket();
        String key = getKey(targetName, false);
        UploadFileRequest request = new UploadFileRequest(bucket, key);
        String upLoadFile = file.getPath();
        request.setUploadFile(upLoadFile);

        SliceConfig slice = huaweiOssConfig.getSliceConfig();

        request.setTaskNum(slice.getTaskNum());
        request.setPartSize(slice.getPartSize());
        request.setEnableCheckpoint(true);

        String checkpointFile = upLoadFile + StrUtil.DOT + OssConstant.OssType.HUAWEI;

        request.setCheckpointFile(checkpointFile);
        obsClient.uploadFile(request);
        return getInfo(targetName);
    }

    @Override
    public void downLoad(OutputStream os, String targetName) {
        ObsObject obsObject = obsClient.getObject(getBucket(), getKey(targetName, false));
        IoUtil.copy(obsObject.getObjectContent(), os);
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
        String downloadFile = localFile.getPath();
        DownloadFileRequest request = new DownloadFileRequest(getBucket(), getKey(targetName, false));
        request.setEnableCheckpoint(true);
        SliceConfig sliceConfig = huaweiOssConfig.getSliceConfig();
        request.setTaskNum(sliceConfig.getTaskNum());
        request.setPartSize(sliceConfig.getPartSize());

        String checkpointFile = downloadFile + StrUtil.DOT + OssConstant.OssType.HUAWEI;
        request.setCheckpointFile(checkpointFile);
        request.setDownloadFile(downloadFile);
        obsClient.downloadFile(request);
    }

    @Override
    public void delete(String targetName) {
        obsClient.deleteObject(getBucket(), getKey(targetName, false));
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
        String bucket = getBucket();
        String newTargetName = getKey(targetName, false);
        if (isOverride || !obsClient.doesObjectExist(bucket, newTargetName)) {
            obsClient.copyObject(bucket, getKey(sourceName, false), bucket, newTargetName);
        }
    }

    @Override
    public OssInfo getInfo(String targetName, Boolean isRecursion) {
        String key = getKey(targetName, false);

        OssInfo ossInfo = getBaseInfo(key);
        ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName : FileNameUtil.getName(targetName));
        ossInfo.setPath(OssPathUtil.replaceKey(targetName, ossInfo.getName(), true));

        if (isRecursion && isDirectory(key)) {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(getBucket());
            listObjectsRequest.setDelimiter("/");
            String prefix = OssPathUtil.convertPath(key, false);
            listObjectsRequest.setPrefix(prefix.endsWith("/") ? prefix : prefix + CharPool.SLASH);

            ObjectListing listObjects = obsClient.listObjects(listObjectsRequest);

            List<OssInfo> fileOssInfos = new ArrayList<>();
            List<OssInfo> directoryInfos = new ArrayList<>();
            for (ObsObject obsObject : listObjects.getObjects()) {
                if (FileNameUtil.getName(obsObject.getObjectKey()).equals(FileNameUtil.getName(key))) {
                    ossInfo.setLastUpdateTime(DateUtil.date(obsObject.getMetadata().getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
                    ossInfo.setCreateTime(DateUtil.date(obsObject.getMetadata().getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
	                ossInfo.setLength(obsObject.getMetadata().getContentLength());
	                // todo 需要设置访问路径
	                //ossInfo.setUrl(ossConfig.get() + "/" + bucketName + "/" + key);
                } else {
                    fileOssInfos.add(getInfo(OssPathUtil.replaceKey(obsObject.getObjectKey(), getBasePath(), false), false));
                }
            }

            for (String commonPrefix : listObjects.getCommonPrefixes()) {
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
            if (ObjectUtil.isNotEmpty(directoryInfos) && directoryInfos.get(0) instanceof DirectoryOssInfo) {
                ReflectUtil.setFieldValue(ossInfo, "directoryInfos", directoryInfos);
            }
        }

        return ossInfo;
    }

    @Override
    public Boolean isExist(String targetName) {
        return obsClient.doesObjectExist(getBucket(), getKey(targetName, false));
    }

    @Override
    public String getBasePath() {
        return huaweiOssConfig.getBasePath();
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<String, Object>() {
            {
                put(OBS_OBJECT_NAME, getObsClient());
            }
        };
    }

    private String getBucket() {
        return huaweiOssConfig.getBucketName();
    }

    public OssInfo getBaseInfo(String key) {
        OssInfo ossInfo;

        if (isFile(key)) {
            ossInfo = new FileOssInfo();
            try {
                ObjectMetadata objectMetadata = obsClient.getObjectMetadata(getBucket(), key);
                ossInfo.setLastUpdateTime(DateUtil.date(objectMetadata.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
                ossInfo.setCreateTime(DateUtil.date(objectMetadata.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
	            ossInfo.setLength(objectMetadata.getContentLength());
	            // todo 需要设置访问路径
	            //ossInfo.setUrl(ossConfig.get() + "/" + bucketName + "/" + key);
            } catch (Exception e) {
                LogUtils.error("获取{}文件属性失败", key, e);
            }
        } else {
            ossInfo = new DirectoryOssInfo();
        }
        return ossInfo;
    }

	public ObsClient getObsClient() {
		return obsClient;
	}

	public void setObsClient(ObsClient obsClient) {
		this.obsClient = obsClient;
	}

	public HuaweiOssConfig getHuaweiOssConfig() {
		return huaweiOssConfig;
	}

	public void setHuaweiOssConfig(
		HuaweiOssConfig huaweiOssConfig) {
		this.huaweiOssConfig = huaweiOssConfig;
	}
}
