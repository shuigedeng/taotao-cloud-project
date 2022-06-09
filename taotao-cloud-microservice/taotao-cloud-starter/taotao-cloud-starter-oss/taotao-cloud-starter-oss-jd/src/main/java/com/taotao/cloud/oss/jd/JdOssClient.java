package com.taotao.cloud.oss.jd;

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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.common.constant.OssConstant;
import com.taotao.cloud.oss.common.model.DirectoryOssInfo;
import com.taotao.cloud.oss.common.model.FileOssInfo;
import com.taotao.cloud.oss.common.model.OssInfo;
import com.taotao.cloud.oss.common.model.download.DownloadCheckPoint;
import com.taotao.cloud.oss.common.model.download.DownloadObjectStat;
import com.taotao.cloud.oss.common.model.upload.UpLoadCheckPoint;
import com.taotao.cloud.oss.common.model.upload.UpLoadPartEntityTag;
import com.taotao.cloud.oss.common.model.upload.UpLoadPartResult;
import com.taotao.cloud.oss.common.model.upload.UploadPart;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import com.taotao.cloud.oss.common.util.OssPathUtil;
import com.taotao.cloud.oss.jd.JdOssConfig;

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
 * <a href="https://docs.jdcloud.com/cn/object-storage-service/product-overview">https://docs.jdcloud.com/cn/object-storage-service/product-overview</a>
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:34
 */
public class JdOssClient implements StandardOssClient {

    public static final String AMAZONS3_OBJECT_NAME = "amazonS3";
    public static final String TRANSFER_OBJECT_NAME = "transferManager";

    private AmazonS3 amazonS3;
    private TransferManager transferManager;
    private JdOssConfig jdOssConfig;

	public JdOssClient(AmazonS3 amazonS3, TransferManager transferManager,
		JdOssConfig jdOssConfig) {
		this.amazonS3 = amazonS3;
		this.transferManager = transferManager;
		this.jdOssConfig = jdOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        String bucketName = getBucket();
        String key = getKey(targetName, false);

        if (isOverride || !amazonS3.doesObjectExist(bucketName, key)) {
            amazonS3.putObject(bucketName, key, is, new ObjectMetadata());
        }
        return getInfo(targetName);
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        return uploadFile(file, targetName, jdOssConfig.getSliceConfig(), OssConstant.OssType.JD);
    }

    @Override
    public void completeUpload(UpLoadCheckPoint upLoadCheckPoint, List<UpLoadPartEntityTag> partEntityTags) {
        List<PartETag> eTags = partEntityTags.stream().sorted(Comparator.comparingInt(UpLoadPartEntityTag::getPartNumber))
                .map(partEntityTag -> new PartETag(partEntityTag.getPartNumber(), partEntityTag.getETag())).collect(Collectors.toList());

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(upLoadCheckPoint.getBucket(), upLoadCheckPoint.getKey(), upLoadCheckPoint.getUploadId(), eTags);
        amazonS3.completeMultipartUpload(completeMultipartUploadRequest);

        FileUtil.del(upLoadCheckPoint.getCheckpointFile());
    }

    @Override
    public UpLoadPartResult uploadPart(UpLoadCheckPoint upLoadCheckPoint, int partNum, InputStream inputStream) {
        UpLoadPartResult partResult = null;
        UploadPart uploadPart = upLoadCheckPoint.getUploadParts().get(partNum);
        long partSize = uploadPart.getSize();
        partResult = new UpLoadPartResult(partNum + 1, uploadPart.getOffset(), partSize);
        try {
            inputStream.skip(uploadPart.getOffset());

            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(upLoadCheckPoint.getBucket());
            uploadPartRequest.setKey(upLoadCheckPoint.getKey());
            uploadPartRequest.setUploadId(upLoadCheckPoint.getUploadId());
            uploadPartRequest.setInputStream(inputStream);
            uploadPartRequest.setPartSize(partSize);
            uploadPartRequest.setPartNumber(uploadPart.getNumber());

            UploadPartResult uploadPartResponse = amazonS3.uploadPart(uploadPartRequest);

            partResult.setNumber(uploadPartResponse.getPartNumber());
	        UpLoadPartEntityTag upLoadPartEntityTag = new UpLoadPartEntityTag();
	        upLoadPartEntityTag.setETag(uploadPartResponse.getETag());
	        upLoadPartEntityTag.setPartNumber(uploadPartResponse.getPartNumber());
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
        S3Object s3Object = amazonS3.getObject(getBucket(), getKey(targetName, false));
        IoUtil.copy(s3Object.getObjectContent(), os);
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
        downLoadFile(localFile, targetName, jdOssConfig.getSliceConfig(), OssConstant.OssType.BAIDU);
    }

    @Override
    public DownloadObjectStat getDownloadObjectStat(String targetName) {
        ObjectMetadata objectMetadata = amazonS3.getObjectMetadata(getBucket(), getKey(targetName, false));
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
            Long partSize = jdOssConfig.getSliceConfig().getPartSize();
            long[] slice = getDownloadSlice(new long[0], downloadCheckPoint.getObjectStat().getSize());
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
        GetObjectRequest request = new GetObjectRequest(getBucket(), key);
        request.setRange(start, end);
        S3Object object = amazonS3.getObject(request);
        return object.getObjectContent();
    }

    @Override
    public void delete(String targetName) {
        amazonS3.deleteObject(getBucket(), getKey(targetName, false));
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
        String bucketName = getBucket();
        String targetKey = getKey(targetName, false);
        if (isOverride || !amazonS3.doesObjectExist(bucketName, targetKey)) {
            amazonS3.copyObject(getBucket(), getKey(sourceName, false), getBucket(), targetKey);
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
            ObjectListing listObjects = amazonS3.listObjects(getBucket(), prefix.endsWith("/") ? prefix : prefix + CharPool.SLASH);

            List<OssInfo> fileOssInfos = new ArrayList<>();
            List<OssInfo> directoryInfos = new ArrayList<>();
            for (S3ObjectSummary s3ObjectSummary : listObjects.getObjectSummaries()) {
                if (FileNameUtil.getName(s3ObjectSummary.getKey()).equals(FileNameUtil.getName(key))) {
                    ossInfo.setLastUpdateTime(DateUtil.date(s3ObjectSummary.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
                    ossInfo.setCreateTime(DateUtil.date(s3ObjectSummary.getLastModified()).toString(DatePattern.NORM_DATETIME_PATTERN));
	                ossInfo.setLength(s3ObjectSummary.getSize());
	                // todo 需要设置访问路径
	                //ossInfo.setUrl(ossConfig.get() + "/" + bucketName + "/" + key);
                } else {
                    fileOssInfos.add(getInfo(OssPathUtil.replaceKey(s3ObjectSummary.getKey(), getBasePath(), false), false));
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
        return amazonS3.doesObjectExist(getBucket(), getKey(targetName, false));
    }

    @Override
    public String getBasePath() {
        return jdOssConfig.getBasePath();
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<String, Object>() {
            {
                put(AMAZONS3_OBJECT_NAME, getAmazonS3());
                put(TRANSFER_OBJECT_NAME, getTransferManager());
            }
        };
    }

    private String getBucket() {
        return jdOssConfig.getBucketName();
    }

    public OssInfo getBaseInfo(String key) {
        OssInfo ossInfo;

        if (isFile(key)) {
            ossInfo = new FileOssInfo();
            try {
                ObjectMetadata objectMetadata = amazonS3.getObjectMetadata(getBucket(), OssPathUtil.replaceKey(key, "", false));
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

	public AmazonS3 getAmazonS3() {
		return amazonS3;
	}

	public void setAmazonS3(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	public TransferManager getTransferManager() {
		return transferManager;
	}

	public void setTransferManager(TransferManager transferManager) {
		this.transferManager = transferManager;
	}

	public JdOssConfig getJdOssConfig() {
		return jdOssConfig;
	}

	public void setJdOssConfig(JdOssConfig jdOssConfig) {
		this.jdOssConfig = jdOssConfig;
	}
}
