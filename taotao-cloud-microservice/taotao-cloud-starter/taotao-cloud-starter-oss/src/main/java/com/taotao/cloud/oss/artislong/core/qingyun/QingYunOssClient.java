package com.taotao.cloud.oss.artislong.core.qingyun;

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
import com.qingstor.sdk.exception.QSException;
import com.qingstor.sdk.service.Bucket;
import com.qingstor.sdk.service.QingStor;
import com.qingstor.sdk.service.Types;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.qingyun.model.QingYunOssConfig;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 清云操作系统客户端
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:17
 */
public class QingYunOssClient implements StandardOssClient {

    public static final String QINGSTORE_OBJECT_NAME = "qingStor";
    public static final String BUCKET_OBJECT_NAME = "bucketClient";

    private QingStor qingStor;
    private Bucket bucketClient;
    private QingYunOssConfig qingYunOssConfig;

	public QingYunOssClient(QingStor qingStor, Bucket bucketClient,
		QingYunOssConfig qingYunOssConfig) {
		this.qingStor = qingStor;
		this.bucketClient = bucketClient;
		this.qingYunOssConfig = qingYunOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        String key = getKey(targetName, false);
        if (isOverride || !isExist(targetName)) {
            Bucket.PutObjectInput input = new Bucket.PutObjectInput();
            input.setBodyInputStream(is);
            try {
                bucketClient.putObject(key, input);
            } catch (Exception e) {
                throw new OssException(e);
            }
        }
        return getInfo(targetName);
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        return uploadFile(file, targetName, qingYunOssConfig.getSliceConfig(), OssConstant.OssType.QINGYUN);
    }

    @Override
    public void completeUpload(UpLoadCheckPoint upLoadCheckPoint, List<UpLoadPartEntityTag> partEntityTags) {
        try {
            String uploadId = upLoadCheckPoint.getUploadId();
            String key = upLoadCheckPoint.getKey();

            Bucket.ListMultipartInput listMultipartInput = new Bucket.ListMultipartInput();
            listMultipartInput.setUploadID(uploadId);
            Bucket.ListMultipartOutput output = bucketClient.listMultipart(key, listMultipartInput);
            List<Types.ObjectPartModel> objectParts = output.getObjectParts();

            Bucket.CompleteMultipartUploadInput multipartUploadInput = new Bucket.CompleteMultipartUploadInput();
            multipartUploadInput.setUploadID(uploadId);
            multipartUploadInput.setObjectParts(objectParts);

            bucketClient.completeMultipartUpload(key, multipartUploadInput);
            FileUtil.del(upLoadCheckPoint.getCheckpointFile());
        } catch (QSException e) {
            throw new OssException(e);
        }
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

        try {
            Bucket.InitiateMultipartUploadInput input = new Bucket.InitiateMultipartUploadInput();
            Bucket.InitiateMultipartUploadOutput multipartUploadOutput = bucketClient.initiateMultipartUpload(key, input);
            uploadCheckPoint.setUploadId(multipartUploadOutput.getUploadID());
        } catch (QSException e) {
            throw new OssException(e);
        }

    }

    @Override
    public UpLoadPartResult uploadPart(UpLoadCheckPoint upLoadCheckPoint, int partNum, InputStream inputStream) {
        UploadPart uploadPart = upLoadCheckPoint.getUploadParts().get(partNum);
        long partSize = uploadPart.getSize();
        UpLoadPartResult partResult = new UpLoadPartResult(partNum + 1, uploadPart.getOffset(), partSize);

        try {
            inputStream.skip(uploadPart.getOffset());

            Bucket.UploadMultipartInput input = new Bucket.UploadMultipartInput();
            input.setPartNumber(partNum + 1);
            input.setFileOffset(uploadPart.getOffset());
            input.setUploadID(upLoadCheckPoint.getUploadId());
            input.setBodyInputStream(inputStream);
            Bucket.UploadMultipartOutput multipartOutput = bucketClient.uploadMultipart(upLoadCheckPoint.getKey(), input);

            partResult.setNumber(partNum + 1);
	        UpLoadPartEntityTag upLoadPartEntityTag = new UpLoadPartEntityTag();
	        upLoadPartEntityTag.setETag(multipartOutput.getETag());
	        upLoadPartEntityTag.setPartNumber(partNum);
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
        try {
            Bucket.GetObjectInput input = new Bucket.GetObjectInput();
            Bucket.GetObjectOutput object = bucketClient.getObject(getKey(targetName, false), input);
            IoUtil.copy(object.getBodyInputStream(), os);
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
        downLoadFile(localFile, targetName, qingYunOssConfig.getSliceConfig(), OssConstant.OssType.QINGYUN);
    }

    @Override
    public DownloadObjectStat getDownloadObjectStat(String targetName) {
        try {
            Bucket.GetObjectInput input = new Bucket.GetObjectInput();
            Bucket.GetObjectOutput object = bucketClient.getObject(getKey(targetName, false), input);
            DateTime date = DateUtil.date(Date.parse(object.getLastModified()));
            long contentLength = object.getContentLength();
            String eTag = object.getETag();
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
    public void prepareDownload(DownloadCheckPoint downloadCheckPoint, File localFile, String targetName, String checkpointFile) {
        downloadCheckPoint.setMagic(DownloadCheckPoint.DOWNLOAD_MAGIC);
        downloadCheckPoint.setDownloadFile(localFile.getPath());
        downloadCheckPoint.setBucketName(getBucket());
        downloadCheckPoint.setKey(getKey(targetName, false));
        downloadCheckPoint.setCheckPointFile(checkpointFile);

        downloadCheckPoint.setObjectStat(getDownloadObjectStat(targetName));

        long downloadSize;
        if (downloadCheckPoint.getObjectStat().getSize() > 0) {
            Long partSize = qingYunOssConfig.getSliceConfig().getPartSize();
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
        try {
            Bucket.GetObjectInput putObjectInput = new Bucket.GetObjectInput();
            putObjectInput.setRange("bytes=" + start + "-" + end);
            Bucket.GetObjectOutput object = bucketClient.getObject(key, putObjectInput);
            return object.getBodyInputStream();
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public void delete(String targetName) {
        try {
            bucketClient.deleteObject(getKey(targetName, false));
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
        String bucket = getBucket();
        String newSourceName = getKey(sourceName, false);
        String newTargetName = getKey(targetName, false);
        if (isOverride || !isExist(targetName)) {
            try {
                Bucket.PutObjectInput input = new Bucket.PutObjectInput();
                input.setXQSCopySource(StrUtil.SLASH + bucket + StrUtil.SLASH + newSourceName);
                bucketClient.putObject(newTargetName, input);
            } catch (Exception e) {
                throw new OssException(e);
            }
        }
    }

    @Override
    public void move(String sourceName, String targetName, Boolean isOverride) {
        String bucket = getBucket();
        String newSourceName = getKey(sourceName, false);
        String newTargetName = getKey(targetName, false);
        if (isOverride || !isExist(targetName)) {
            try {
                Bucket.PutObjectInput input = new Bucket.PutObjectInput();
                input.setXQSMoveSource(StrUtil.SLASH + bucket + StrUtil.SLASH + newSourceName);
                bucketClient.putObject(newTargetName, input);
            } catch (Exception e) {
                throw new OssException(e);
            }
        }
    }

    @Override
    public OssInfo getInfo(String targetName, Boolean isRecursion) {
        String key = getKey(targetName, false);

        OssInfo ossInfo = getBaseInfo(key);
        ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName : FileNameUtil.getName(targetName));
        ossInfo.setPath(OssPathUtil.replaceKey(targetName, ossInfo.getName(), true));

        if (isRecursion && isDirectory(key)) {
            List<OssInfo> fileOssInfos = new ArrayList<>();
            List<OssInfo> directoryInfos = new ArrayList<>();

            try {
                String prefix = OssPathUtil.convertPath(key, false);
                Bucket.ListObjectsInput input = new Bucket.ListObjectsInput();
                input.setPrefix(prefix.endsWith(StrUtil.SLASH) ? prefix : prefix + CharPool.SLASH);
                input.setDelimiter(StrUtil.SLASH);
                Bucket.ListObjectsOutput listObjects = bucketClient.listObjects(input);

                if (ObjectUtil.isNotEmpty(listObjects.getKeys())) {
                    for (Types.KeyModel keyModel : listObjects.getKeys()) {
                        if (FileNameUtil.getName(keyModel.getKey()).equals(FileNameUtil.getName(key))) {
                            ossInfo.setLastUpdateTime(DateUtil.parse(keyModel.getCreated()).toString(DatePattern.NORM_DATETIME_PATTERN));
                            ossInfo.setCreateTime(DateUtil.parse(keyModel.getCreated()).toString(DatePattern.NORM_DATETIME_PATTERN));
                            ossInfo.setLength(keyModel.getSize());
                        } else {
                            fileOssInfos.add(getInfo(OssPathUtil.replaceKey(keyModel.getKey(), getBasePath(), false), false));
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
            } catch (Exception e) {
                throw new OssException(e);
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
        OssInfo info = getInfo(targetName);
        return ObjectUtil.isNotEmpty(info.getLength()) && Convert.toLong(info.getLength()) > 0;
    }

    @Override
    public String getBasePath() {
        return qingYunOssConfig.getBasePath();
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<String, Object>() {
            {
                put(QINGSTORE_OBJECT_NAME, getQingStor());
                put(BUCKET_OBJECT_NAME, getBucketClient());
            }
        };
    }

    private String getBucket() {
        return qingYunOssConfig.getBucketName();
    }

    public OssInfo getBaseInfo(String key) {
        OssInfo ossInfo;

        if (isFile(key)) {
            ossInfo = new FileOssInfo();
            try {
                Bucket.GetObjectOutput object = bucketClient.getObject(key, new Bucket.GetObjectInput());
                ossInfo.setLastUpdateTime(DateUtil.date(Date.parse(object.getLastModified())).toString(DatePattern.NORM_DATETIME_PATTERN));
                ossInfo.setCreateTime(DateUtil.date(Date.parse(object.getLastModified())).toString(DatePattern.NORM_DATETIME_PATTERN));
                ossInfo.setLength(object.getContentLength());
            } catch (Exception e) {
	            LogUtil.error("获取{}文件属性失败", key, e);
            }
        } else {
            ossInfo = new DirectoryOssInfo();
        }
        return ossInfo;
    }

	public QingStor getQingStor() {
		return qingStor;
	}

	public void setQingStor(QingStor qingStor) {
		this.qingStor = qingStor;
	}

	public Bucket getBucketClient() {
		return bucketClient;
	}

	public void setBucketClient(Bucket bucketClient) {
		this.bucketClient = bucketClient;
	}

	public QingYunOssConfig getQingYunOssConfig() {
		return qingYunOssConfig;
	}

	public void setQingYunOssConfig(
		QingYunOssConfig qingYunOssConfig) {
		this.qingYunOssConfig = qingYunOssConfig;
	}
}
