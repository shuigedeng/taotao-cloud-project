package com.taotao.cloud.oss.jinshan;

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
import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.Ks3ObjectSummary;
import com.ksyun.ks3.dto.ObjectListing;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.PartETag;
import com.ksyun.ks3.service.Ks3;
import com.ksyun.ks3.service.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.constant.OssConstant;
import com.taotao.cloud.oss.common.model.DirectoryOssInfo;
import com.taotao.cloud.oss.common.model.FileOssInfo;
import com.taotao.cloud.oss.common.model.OssInfo;
import com.taotao.cloud.oss.common.model.SliceConfig;
import com.taotao.cloud.oss.common.model.download.DownloadCheckPoint;
import com.taotao.cloud.oss.common.model.download.DownloadObjectStat;
import com.taotao.cloud.oss.common.model.upload.UpLoadCheckPoint;
import com.taotao.cloud.oss.common.model.upload.UpLoadFileStat;
import com.taotao.cloud.oss.common.model.upload.UpLoadPartEntityTag;
import com.taotao.cloud.oss.common.model.upload.UpLoadPartResult;
import com.taotao.cloud.oss.common.model.upload.UploadPart;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import com.taotao.cloud.oss.common.util.OssPathUtil;

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
 * <a href="https://docs.ksyun.com/documents/38731">https://docs.ksyun.com/documents/38731</a>
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:12
 */
public class JinShanOssClient implements StandardOssClient {

    public static final String KS3_OBJECT_NAME = "ks3";

    private Ks3 ks3;
    private JinShanOssConfig jinShanOssConfig;

	public JinShanOssClient(Ks3 ks3, JinShanOssConfig jinShanOssConfig) {
		this.ks3 = ks3;
		this.jinShanOssConfig = jinShanOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        String bucket = getBucket();
        String key = getKey(targetName, false);
        if (isOverride || !ks3.objectExists(bucket, key)) {
            ks3.putObject(bucket, key, is, null);
        }
        return getInfo(targetName);
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        return uploadFile(file, targetName, jinShanOssConfig.getSliceConfig(), OssConstant.OssType.JINSHAN);
    }

    @Override
    public void completeUpload(UpLoadCheckPoint upLoadCheckPoint, List<UpLoadPartEntityTag> partEntityTags) {
        List<PartETag> eTags = partEntityTags.stream().sorted(Comparator.comparingInt(UpLoadPartEntityTag::getPartNumber))
                .map(partEntityTag -> {
                    PartETag p = new PartETag();
                    p.seteTag(partEntityTag.getETag());
                    p.setPartNumber(partEntityTag.getPartNumber());
                    return p;
                }).collect(Collectors.toList());

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(upLoadCheckPoint.getBucket(), upLoadCheckPoint.getKey(), upLoadCheckPoint.getUploadId(), eTags);
        ks3.completeMultipartUpload(completeMultipartUploadRequest);
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

        InitiateMultipartUploadResult initiateMultipartUploadResult =
                ks3.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucket, key));

        uploadCheckPoint.setUploadId(initiateMultipartUploadResult.getUploadId());
    }

    @Override
    public UpLoadPartResult uploadPart(UpLoadCheckPoint upLoadCheckPoint, int partNum, InputStream inputStream) {
        UploadPart uploadPart = upLoadCheckPoint.getUploadParts().get(partNum);
        long partSize = uploadPart.getSize();
        UpLoadPartResult partResult = new UpLoadPartResult(partNum + 1, uploadPart.getOffset(), partSize);

        try {
            inputStream.skip(uploadPart.getOffset());

            UploadPartRequest uploadPartRequest = new UploadPartRequest(upLoadCheckPoint.getBucket(), upLoadCheckPoint.getKey());
            uploadPartRequest.setUploadId(upLoadCheckPoint.getUploadId());
            uploadPartRequest.setInputStream(inputStream);
            uploadPartRequest.setPartSize(partSize);
            uploadPartRequest.setPartNumber(partNum + 1);
            PartETag eTag = ks3.uploadPart(uploadPartRequest);

            partResult.setNumber(eTag.getPartNumber());
	        UpLoadPartEntityTag upLoadPartEntityTag = new UpLoadPartEntityTag();
	        upLoadPartEntityTag.setETag(eTag.geteTag());
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
        GetObjectResult objectResult = ks3.getObject(getBucket(), getKey(targetName, false));
        IoUtil.copy(objectResult.getObject().getObjectContent(), os);
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
        downLoadFile(localFile, targetName, jinShanOssConfig.getSliceConfig(), OssConstant.OssType.JINSHAN);
    }

    @Override
    public DownloadObjectStat getDownloadObjectStat(String targetName) {
        GetObjectResult objectResult = ks3.getObject(getBucket(), getKey(targetName, false));
        ObjectMetadata objectMetadata = objectResult.getObject().getObjectMetadata();
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
            Long partSize = jinShanOssConfig.getSliceConfig().getPartSize();
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
        GetObjectRequest request = new GetObjectRequest(getBucket(), key);
        request.setRange(start, end);
        GetObjectResult object = ks3.getObject(request);
        return object.getObject().getObjectContent();
    }

    @Override
    public void delete(String targetName) {
        ks3.deleteObject(getBucket(), getKey(targetName, false));
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
        String bucket = getBucket();
        String newTargetName = getKey(targetName, false);
        if (isOverride || !ks3.objectExists(bucket, newTargetName)) {
            ks3.copyObject(bucket, newTargetName, bucket, getKey(sourceName, false));
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
            ObjectListing listObjects = ks3.listObjects(getBucket(), prefix.endsWith("/") ? prefix : prefix + CharPool.SLASH);

            List<OssInfo> fileOssInfos = new ArrayList<>();
            List<OssInfo> directoryInfos = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(listObjects.getObjectSummaries())) {
                for (Ks3ObjectSummary ks3ObjectSummary : listObjects.getObjectSummaries()) {
                    if (FileNameUtil.getName(ks3ObjectSummary.getKey()).equals(FileNameUtil.getName(key))) {
                        ossInfo.setLastUpdateTime(DateUtil.date(ks3ObjectSummary.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
                        ossInfo.setCreateTime(DateUtil.date(ks3ObjectSummary.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
	                    ossInfo.setLength(ks3ObjectSummary.getSize());
	                    // todo 需要设置访问路径
	                    //ossInfo.setUrl(ossConfig.get() + "/" + bucketName + "/" + key);
                    } else {
                        fileOssInfos.add(getInfo(OssPathUtil.replaceKey(ks3ObjectSummary.getKey(), getBasePath(), false), false));
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
        return ks3.objectExists(getBucket(), getKey(targetName, false));
    }

    @Override
    public String getBasePath() {
        return jinShanOssConfig.getBasePath();
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<String, Object>() {
            {
                put(KS3_OBJECT_NAME, getKs3());
            }
        };
    }

    private String getBucket() {
        return jinShanOssConfig.getBucketName();
    }

    public OssInfo getBaseInfo(String key) {
        OssInfo ossInfo;

        if (isFile(key)) {
            ossInfo = new FileOssInfo();
            try {
                GetObjectResult objectResult = ks3.getObject(getBucket(), key);
                ObjectMetadata objectMetadata = objectResult.getObject().getObjectMetadata();
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

	public Ks3 getKs3() {
		return ks3;
	}

	public void setKs3(Ks3 ks3) {
		this.ks3 = ks3;
	}

	public JinShanOssConfig getJinShanOssConfig() {
		return jinShanOssConfig;
	}

	public void setJinShanOssConfig(
		JinShanOssConfig jinShanOssConfig) {
		this.jinShanOssConfig = jinShanOssConfig;
	}
}
