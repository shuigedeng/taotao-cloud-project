package com.taotao.cloud.oss.artislong.core.baidu;

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
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.CompleteMultipartUploadRequest;
import com.baidubce.services.bos.model.GetObjectRequest;
import com.baidubce.services.bos.model.InitiateMultipartUploadRequest;
import com.baidubce.services.bos.model.InitiateMultipartUploadResponse;
import com.baidubce.services.bos.model.ListObjectsResponse;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PartETag;
import com.baidubce.services.bos.model.UploadPartRequest;
import com.baidubce.services.bos.model.UploadPartResponse;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.baidu.model.BaiduOssConfig;
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
 * <a href="https://cloud.baidu.com/doc/BOS/index.html">https://cloud.baidu.com/doc/BOS/index.html</a>
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:46
 */
public class BaiduOssClient implements StandardOssClient {

    public static final String BOS_OBJECT_NAME = "bosClient";

    private BosClient bosClient;
    private BaiduOssConfig baiduOssConfig;

	public BaiduOssClient(BosClient bosClient, BaiduOssConfig baiduOssConfig) {
		this.bosClient = bosClient;
		this.baiduOssConfig = baiduOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        String bucket = getBucket();
        String key = getKey(targetName, false);
        if (isOverride || !bosClient.doesObjectExist(bucket, key)) {
            bosClient.putObject(bucket, key, is);
        }
        return getInfo(targetName);
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        return uploadFile(file, targetName, baiduOssConfig.getSliceConfig(), OssConstant.OssType.BAIDU);
    }

    @Override
    public void completeUpload(UpLoadCheckPoint upLoadCheckPoint, List<UpLoadPartEntityTag> partEntityTags) {
        List<PartETag> eTags = partEntityTags.stream().sorted(Comparator.comparingInt(UpLoadPartEntityTag::getPartNumber))
                .map(partEntityTag -> {
                    PartETag p = new PartETag();
                    p.setETag(partEntityTag.getETag());
                    p.setPartNumber(partEntityTag.getPartNumber());
                    return p;
                }).collect(Collectors.toList());

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(upLoadCheckPoint.getBucket(), upLoadCheckPoint.getKey(), upLoadCheckPoint.getUploadId(), eTags);
        bosClient.completeMultipartUpload(completeMultipartUploadRequest);
        FileUtil.del(upLoadCheckPoint.getCheckpointFile());
    }

    @Override
    public void prepareUpload(UpLoadCheckPoint uploadCheckPoint, File upLoadFile, String targetName, String checkpointFile, SliceConfig slice) {
        String bucket = getBucket();
        String key = getKey(targetName, false);

        uploadCheckPoint.setMagic(UpLoadCheckPoint.UPLOAD_MAGIC);
        uploadCheckPoint.setUploadFile(upLoadFile.getPath());
        uploadCheckPoint.setKey(key);
        uploadCheckPoint.setBucket(bucket);
        uploadCheckPoint.setCheckpointFile(checkpointFile);
        uploadCheckPoint.setUploadFileStat(UpLoadFileStat.getFileStat(uploadCheckPoint.getUploadFile()));

        long partSize = slice.getPartSize();
        long fileLength = upLoadFile.length();
        int parts = (int) (fileLength / partSize);
        if (fileLength % partSize > 0) {
            parts++;
        }

        uploadCheckPoint.setUploadParts(splitUploadFile(uploadCheckPoint.getUploadFileStat().getSize(), partSize));
        uploadCheckPoint.setPartEntityTags(new ArrayList<>());
        uploadCheckPoint.setOriginPartSize(parts);

        InitiateMultipartUploadResponse initiateMultipartUploadResponse =
                bosClient.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucket, key));

        uploadCheckPoint.setUploadId(initiateMultipartUploadResponse.getUploadId());
    }

    @Override
    public UpLoadPartResult uploadPart(UpLoadCheckPoint upLoadCheckPoint, int partNum, InputStream inputStream) {
        UploadPart uploadPart = upLoadCheckPoint.getUploadParts().get(partNum);
        long partSize = uploadPart.getSize();
        UpLoadPartResult partResult = new UpLoadPartResult(partNum + 1, uploadPart.getOffset(), partSize);

        try {
            inputStream.skip(uploadPart.getOffset());

            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(upLoadCheckPoint.getBucket());
            uploadPartRequest.setKey(upLoadCheckPoint.getKey());
            uploadPartRequest.setUploadId(upLoadCheckPoint.getUploadId());
            uploadPartRequest.setInputStream(inputStream);
            uploadPartRequest.setPartSize(partSize);
            uploadPartRequest.setPartNumber(partNum + 1);
            UploadPartResponse uploadPartResponse = bosClient.uploadPart(uploadPartRequest);

            partResult.setNumber(uploadPartResponse.getPartNumber());
            PartETag eTag = uploadPartResponse.getPartETag();
	        UpLoadPartEntityTag upLoadPartEntityTag = new UpLoadPartEntityTag();
	        upLoadPartEntityTag.setETag(eTag.getETag());
	        upLoadPartEntityTag.setPartNumber(eTag.getPartNumber());
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
        BosObject bosObject = bosClient.getObject(getBucket(), getKey(targetName, false));
        IoUtil.copy(bosObject.getObjectContent(), os);
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
        downLoadFile(localFile, targetName, baiduOssConfig.getSliceConfig(), OssConstant.OssType.BAIDU);
    }

    @Override
    public DownloadObjectStat getDownloadObjectStat(String targetName) {
        ObjectMetadata objectMetadata = bosClient.getObjectMetadata(getBucket(), getKey(targetName, false));
        DateTime date = DateUtil.date(objectMetadata.getLastModified());
        long contentLength = objectMetadata.getContentLength();
        String eTag = objectMetadata.getETag();
	    DownloadObjectStat downloadObjectStat = new DownloadObjectStat();
	    downloadObjectStat.setSize(contentLength);
	    downloadObjectStat.setLastModified(date);
	    downloadObjectStat.setDigest(eTag);
	    return downloadObjectStat;
    }

    @Override
    public void prepareDownload(DownloadCheckPoint downloadCheckPoint, File localFile, String targetName, String checkpointFile) {
        downloadCheckPoint.setMagic(DownloadCheckPoint.DOWNLOAD_MAGIC);
        downloadCheckPoint.setDownloadFile(localFile.getPath());
        downloadCheckPoint.setBucketName(getBucket());
        downloadCheckPoint.setKey(getKey(targetName, false));
        downloadCheckPoint.setCheckPointFile(checkpointFile);

        downloadCheckPoint.setObjectStat(getDownloadObjectStat(targetName));

        long downloadSize;
        if (downloadCheckPoint.getObjectStat().getSize() > 0) {
            Long partSize = baiduOssConfig.getSliceConfig().getPartSize();
            long[] slice = getDownloadSlice(new long[0], downloadCheckPoint.getObjectStat().getSize());
            downloadCheckPoint.setDownloadParts(splitDownloadFile(slice[0], slice[1], partSize));
            downloadSize = slice[1];
        } else {
            downloadSize = 0;
            downloadCheckPoint.setDownloadParts(splitDownloadOneFile());
        }
        downloadCheckPoint.setOriginPartSize(downloadCheckPoint.getDownloadParts().size());
        createDownloadTemp(downloadCheckPoint.getTempDownloadFile(), downloadSize);
    }

    @Override
    public InputStream downloadPart(String key, long start, long end) {
        GetObjectRequest request = new GetObjectRequest();
        request.setKey(key);
        request.setBucketName(getBucket());
        request.setRange(start, end);
        BosObject object = bosClient.getObject(request);
        return object.getObjectContent();
    }

    @Override
    public void delete(String targetName) {
        bosClient.deleteObject(getBucket(), getKey(targetName, false));
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
        String bucket = getBucket();
        String newTargetName = getKey(targetName, false);
        if (isOverride || !bosClient.doesObjectExist(bucket, newTargetName)) {
            bosClient.copyObject(bucket, getKey(sourceName, false), bucket, newTargetName);
        }
    }

    @Override
    public OssInfo getInfo(String targetName, Boolean isRecursion) {
        String key = getKey(targetName, false);

        OssInfo ossInfo = getBaseInfo(key);
        ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName : FileNameUtil.getName(targetName));
        ossInfo.setPath(OssPathUtil.replaceKey(targetName, ossInfo.getName(), true));

        if (isRecursion && isDirectory(key)) {
            String prefix = OssPathUtil.convertPath(key, false);
            ListObjectsResponse listObjects = bosClient.listObjects(getBucket(), prefix.endsWith("/") ? prefix : prefix + CharPool.SLASH);

            List<OssInfo> fileOssInfos = new ArrayList<>();
            List<OssInfo> directoryInfos = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(listObjects.getContents())) {
                for (BosObjectSummary bosObjectSummary : listObjects.getContents()) {
                    if (FileNameUtil.getName(bosObjectSummary.getKey()).equals(FileNameUtil.getName(key))) {
                        ossInfo.setLastUpdateTime(DateUtil.date(bosObjectSummary.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
                        ossInfo.setCreateTime(DateUtil.date(bosObjectSummary.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
	                    ossInfo.setLength(bosObjectSummary.getSize());
	                    // todo 需要设置访问路径
	                    //ossInfo.setUrl(ossConfig.get() + "/" + bucketName + "/" + key);
                    } else {
                        fileOssInfos.add(getInfo(OssPathUtil.replaceKey(bosObjectSummary.getKey(), getBasePath(), false), false));
                    }
                }
            }

            if (ObjectUtil.isNotEmpty(listObjects.getCommonPrefixes())) {
                for (String commonPrefix : listObjects.getCommonPrefixes()) {
                    String target = OssPathUtil.replaceKey(commonPrefix, getBasePath(), false);
                    if (isDirectory(commonPrefix)) {
                        directoryInfos.add(getInfo(target, true));
                    } else {
                        fileOssInfos.add(getInfo(target, false));
                    }
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
        return bosClient.doesObjectExist(getBucket(), getKey(targetName, false));
    }

    @Override
    public String getBasePath() {
        return baiduOssConfig.getBasePath();
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<String, Object>() {
            {
                put(BOS_OBJECT_NAME, getBosClient());
            }
        };
    }

    private String getBucket() {
        return baiduOssConfig.getBucketName();
    }

    public OssInfo getBaseInfo(String key) {
        OssInfo ossInfo;

        if (isFile(key)) {
            ossInfo = new FileOssInfo();
            try {
                ObjectMetadata objectMetadata = bosClient.getObjectMetadata(getBucket(), key);
                ossInfo.setLastUpdateTime(DateUtil.date(objectMetadata.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
                ossInfo.setCreateTime(DateUtil.date(objectMetadata.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
	            ossInfo.setLength(objectMetadata.getContentLength());
	            // todo 需要设置访问路径
	            //ossInfo.setUrl(ossConfig.get() + "/" + bucketName + "/" + key);
            } catch (Exception e) {
                LogUtil.error("获取{}文件属性失败", key, e);
            }
        } else {
            ossInfo = new DirectoryOssInfo();
        }
        return ossInfo;
    }

	public BosClient getBosClient() {
		return bosClient;
	}

	public void setBosClient(BosClient bosClient) {
		this.bosClient = bosClient;
	}

	public BaiduOssConfig getBaiduOssConfig() {
		return baiduOssConfig;
	}

	public void setBaiduOssConfig(
		BaiduOssConfig baiduOssConfig) {
		this.baiduOssConfig = baiduOssConfig;
	}
}
