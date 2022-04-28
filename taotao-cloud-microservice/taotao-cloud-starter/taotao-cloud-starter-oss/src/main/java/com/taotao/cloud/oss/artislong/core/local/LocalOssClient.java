package com.taotao.cloud.oss.artislong.core.local;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.local.model.LocalOssConfig;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 本地操作系统客户端
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:29
 */
public class LocalOssClient implements StandardOssClient {

    private LocalOssConfig localOssConfig;

	public LocalOssClient(LocalOssConfig localOssConfig) {
		this.localOssConfig = localOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        String key = getKey(targetName, true);
        if (isOverride && FileUtil.exist(key)) {
            FileUtil.del(key);
        }
        File file = FileUtil.writeFromStream(is, key);

        OssInfo ossInfo = getBaseInfo(file.getPath());

        ossInfo.setName(file.getName());
        ossInfo.setPath(OssPathUtil.replaceKey(targetName, file.getName(), true));
        return ossInfo;
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        uploadFile(file, targetName, localOssConfig.getSliceConfig(), OssConstant.OssType.LOCAL);
        return getInfo(targetName);
    }

    @Override
    public void prepareUpload(UpLoadCheckPoint uploadCheckPoint, File upLoadFile, String targetName, String checkpointFile, SliceConfig slice) {
        String key = getKey(targetName, true);

        uploadCheckPoint.setMagic(UpLoadCheckPoint.UPLOAD_MAGIC);
        uploadCheckPoint.setUploadFile(upLoadFile.getPath());
        uploadCheckPoint.setKey(key);
        uploadCheckPoint.setCheckpointFile(checkpointFile);
        uploadCheckPoint.setUploadFileStat(UpLoadFileStat.getFileStat(uploadCheckPoint.getUploadFile()));

        long partSize = localOssConfig.getSliceConfig().getPartSize();
        long fileLength = upLoadFile.length();
        int parts = (int) (fileLength / partSize);
        if (fileLength % partSize > 0) {
            parts++;
        }

        uploadCheckPoint.setUploadParts(splitUploadFile(uploadCheckPoint.getUploadFileStat().getSize(), partSize));
        uploadCheckPoint.setOriginPartSize(parts);
    }

    @Override
    public UpLoadPartResult uploadPart(UpLoadCheckPoint upLoadCheckPoint, int partNum, InputStream inputStream) {
        UpLoadPartResult partResult = null;
        UploadPart uploadPart = upLoadCheckPoint.getUploadParts().get(partNum);
        long offset = uploadPart.getOffset();
        long size = uploadPart.getSize();
        Integer partSize = Convert.toInt(size);

        partResult = new UpLoadPartResult(partNum + 1, offset, size);
        partResult.setNumber(partNum);
        try {

            RandomAccessFile targetFile = new RandomAccessFile(upLoadCheckPoint.getKey(), "rw");

            byte[] data = new byte[partSize];
            inputStream.skip(offset);
            targetFile.seek(offset);
            int len = targetFile.read(data);
            targetFile.write(data, 0, len);
            partResult.setEntityTag(new UpLoadPartEntityTag());
        } catch (Exception e) {
            partResult.setFailed(true);
            partResult.setException(e);
        }

        return partResult;
    }

    @Override
    public void downLoad(OutputStream os, String targetName) {
        FileUtil.writeToStream(getKey(targetName, true), os);
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
        downLoadFile(localFile, targetName, localOssConfig.getSliceConfig(), OssConstant.OssType.LOCAL);
    }

    @Override
    public DownloadObjectStat getDownloadObjectStat(String targetName) {
        File file = new File(getKey(targetName, true));
	    DownloadObjectStat downloadObjectStat = new DownloadObjectStat();
	    downloadObjectStat.setSize(file.length());
	    downloadObjectStat.setLastModified(new Date(file.lastModified()));
	    downloadObjectStat.setDigest(DigestUtil.sha256Hex(Convert.toStr(file.lastModified())));
	    return downloadObjectStat;
    }

    @Override
    public void prepareDownload(DownloadCheckPoint downloadCheckPoint, File localFile, String targetName, String checkpointFile) {
        downloadCheckPoint.setMagic(DownloadCheckPoint.DOWNLOAD_MAGIC);
        downloadCheckPoint.setDownloadFile(localFile.getPath());
        downloadCheckPoint.setKey(getKey(targetName, false));
        downloadCheckPoint.setCheckPointFile(checkpointFile);

        downloadCheckPoint.setObjectStat(getDownloadObjectStat(targetName));

        long downloadSize;
        if (downloadCheckPoint.getObjectStat().getSize() > 0) {
            Long partSize = localOssConfig.getSliceConfig().getPartSize();
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
        try {
            RandomAccessFile uploadFile = new RandomAccessFile(key, "r");
            byte[] data = new byte[Convert.toInt(end - start)];
            uploadFile.seek(start);
            uploadFile.read(data);
            return new ByteArrayInputStream(data);
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public void delete(String targetName) {
        FileUtil.del(getKey(targetName, true));
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
        FileUtil.copy(getKey(sourceName, true), getKey(targetName, true), isOverride);
    }

    @Override
    public void move(String sourceName, String targetName, Boolean isOverride) {
        FileUtil.move(Paths.get(getKey(sourceName, true)), Paths.get(getKey(targetName, true)), isOverride);
    }

    @Override
    public void rename(String sourceName, String targetName, Boolean isOverride) {
        FileUtil.rename(Paths.get(getKey(sourceName, true)), getKey(targetName, true), isOverride);
    }

    @Override
    public OssInfo getInfo(String targetName, Boolean isRecursion) {

        String key = getKey(targetName, true);
        File file = FileUtil.file(key);
        OssInfo ossInfo = getBaseInfo(file.getPath());
        ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName : FileNameUtil.getName(targetName));
        ossInfo.setPath(OssPathUtil.replaceKey(targetName, file.getName(), true));

        if (isRecursion && FileUtil.isDirectory(key)) {
            List<File> files = PathUtil.loopFiles(Paths.get(key), 1, pathname -> true);
            List<OssInfo> fileOssInfos = new ArrayList<>();
            List<OssInfo> directoryInfos = new ArrayList<>();
            for (File childFile : files) {
                String target = OssPathUtil.replaceKey(childFile.getPath(), getBasePath(), true);
                if (childFile.isFile()) {
                    fileOssInfos.add(getInfo(target, false));
                } else if (childFile.isDirectory()) {
                    directoryInfos.add(getInfo(target, true));
                }
            }
            ReflectUtil.setFieldValue(ossInfo, "fileInfos", fileOssInfos);
            ReflectUtil.setFieldValue(ossInfo, "directoryInfos", directoryInfos);
        }

        return ossInfo;
    }

    @Override
    public Boolean isExist(String targetName) {
        return FileUtil.exist(getKey(targetName, true));
    }

    @Override
    public Boolean isFile(String targetName) {
        return FileUtil.isFile(getKey(targetName, true));
    }

    @Override
    public Boolean isDirectory(String targetName) {
        return FileUtil.isDirectory(getKey(targetName, true));
    }

    public OssInfo getBaseInfo(String targetName) {
        OssInfo ossInfo = null;
        try {
            Path path = Paths.get(targetName);
            BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

            FileTime lastModifiedTime = basicFileAttributes.lastModifiedTime();
            FileTime creationTime = basicFileAttributes.creationTime();
            long size = basicFileAttributes.size();
            UserPrincipal owner = Files.getOwner(path);

            if (FileUtil.isFile(targetName)) {
                ossInfo = new FileOssInfo();
            } else {
                ossInfo = new DirectoryOssInfo();
            }
            ossInfo.setLastUpdateTime(DateUtil.date(lastModifiedTime.toMillis()).toString(DatePattern.NORM_DATETIME_PATTERN));
            ossInfo.setCreateTime(DateUtil.date(creationTime.toMillis()).toString(DatePattern.NORM_DATETIME_PATTERN));
            ossInfo.setLength(size);
        } catch (Exception e) {
            LogUtil.error("获取{}文件属性失败", targetName, e);
        }

        return Optional.ofNullable(ossInfo).orElse(new FileOssInfo());
    }

    @Override
    public String getBasePath() {
        String basePath = localOssConfig.getBasePath();
        if (!FileUtil.exist(basePath)) {
            FileUtil.mkdir(basePath);
        }
        return basePath;
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<>();
    }

	public LocalOssConfig getLocalOssConfig() {
		return localOssConfig;
	}

	public void setLocalOssConfig(
		LocalOssConfig localOssConfig) {
		this.localOssConfig = localOssConfig;
	}
}
