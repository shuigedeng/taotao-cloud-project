package com.taotao.cloud.oss.artislong.core.ftp;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.ftp.model.FtpOssConfig;
import com.taotao.cloud.oss.artislong.exception.OssException;
import com.taotao.cloud.oss.artislong.model.DirectoryOssInfo;
import com.taotao.cloud.oss.artislong.model.FileOssInfo;
import com.taotao.cloud.oss.artislong.model.OssInfo;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtpOssClient implements StandardOssClient {

    public static final String FTP_OBJECT_NAME = "ftp";

    private Ftp ftp;
    private FtpOssConfig ftpOssConfig;

	public FtpOssClient(Ftp ftp, FtpOssConfig ftpOssConfig) {
		this.ftp = ftp;
		this.ftpOssConfig = ftpOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        String key = getKey(targetName, true);
        String parentPath = OssPathUtil.convertPath(Paths.get(key).getParent().toString(), true);
        if (!ftp.exist(parentPath)) {
            ftp.mkDirs(parentPath);
        }
        if (isOverride || !ftp.exist(key)) {
            ftp.upload(parentPath, FileNameUtil.getName(targetName), is);
        }
        return getInfo(targetName);
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        String key = getKey(targetName, true);
        String fileName = FileNameUtil.getName(targetName);

        Ftp ftp = new Ftp(ftpOssConfig, FtpMode.Passive);
        ftp.setBackToPwd(ftpOssConfig.isBackToPwd());
        FTPClient ftpClient = ftp.getClient();
        InputStream inputStream = null;
        try {
            inputStream = FileUtil.getInputStream(file);
            ftpClient.changeWorkingDirectory(OssPathUtil.convertPath(Paths.get(key).getParent().toString(), true));
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding(StandardCharsets.UTF_8.name());
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            FTPFile[] files = ftpClient.listFiles(key);
            if (files.length == 1) {
                long remoteSize = files[0].getSize();
                long localSize = file.length();
                if (remoteSize == localSize) {
	                LogUtil.info("要上传的文件已存在");
                    ftpClient.disconnect();
                } else if (remoteSize > localSize) {
                    LogUtil.info("软件中心的软件比即将上传的要大，无须上传或重新命名要上传的文件名");
                    ftpClient.disconnect();
                }
                if (inputStream.skip(remoteSize) == remoteSize) {
                    ftpClient.setRestartOffset(remoteSize);
                    boolean success = ftpClient.storeFile(new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), inputStream);
                    if (success) {
                        LogUtil.info("文件断点续传成功");
                        ftpClient.disconnect();
                    }
                }
            } else {
                boolean success = ftpClient.storeFile(new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), inputStream);
                LogUtil.info("文件上传" + success);
            }
        } catch (Exception e) {
            throw new OssException(e);
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(ftp);
        }
        return getInfo(targetName);
    }

    @Override
    public void downLoad(OutputStream os, String targetName) {
        String key = getKey(targetName, true);
        ftp.download(OssPathUtil.convertPath(Paths.get(key).getParent().toString(), true), key, os);
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
        Ftp ftp = null;
        OutputStream outputStream = null;

        try {
            ftp = new Ftp(ftpOssConfig, FtpMode.Passive);
            ftp.setBackToPwd(ftpOssConfig.isBackToPwd());
            FTPClient ftpClient = ftp.getClient();
            ftpClient.enterLocalPassiveMode();

            String key = getKey(targetName, true);
            FTPFile[] ftpFiles = ftpClient.listFiles(key);

            if (ObjectUtil.isEmpty(ftpFiles)) {
                LogUtil.warn("目标文件不存在，请检查！");
                return;
            }
            if (ftpFiles.length != 1) {
                LogUtil.info("目标文件有多个，请检查！");
                return;
            }
            long remoteFileSize = ftpFiles[0].getSize();
            if (localFile.exists()) {
                outputStream = new FileOutputStream(localFile, true);
                long localFileSize = localFile.length();
                if (localFileSize >= remoteFileSize) {
                    LogUtil.info("本地文件大小大于远程文件大小，下载中止");
                    return;
                }
                ftpClient.setRestartOffset(localFileSize);
                ftpClient.retrieveFile(key, outputStream);
                outputStream.close();
            } else {
                outputStream = new FileOutputStream(localFile);
                ftpClient.retrieveFile(key, outputStream);
                outputStream.close();
            }
        } catch (Exception e) {
            throw new OssException(e);
        } finally {
            IoUtil.close(outputStream);
            IoUtil.close(ftp);
        }
    }

    @Override
    public void delete(String targetName) {
        String key = getKey(targetName, true);
        if (isDirectory(targetName)) {
            ftp.delDir(key);
        } else {
            ftp.delFile(key);
        }
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
        LogUtil.warn("ftp协议不支持copy命令，暂不实现");
    }

    @Override
    public void move(String sourceName, String targetName, Boolean isOverride) {
        LogUtil.warn("ftp协议不支持move命令，暂不实现");
    }

    @Override
    public void rename(String sourceName, String targetName, Boolean isOverride) {
        String newSourceName = getKey(sourceName, true);
        String newTargetName = getKey(targetName, true);
        try {
            if (isOverride || !isExist(newTargetName)) {
                ftp.getClient().rename(newSourceName, newTargetName);
            }
        } catch (IOException e) {
            LogUtil.error("{}重命名为{}失败,错误信息为：", newSourceName, newTargetName, e);
            throw new OssException(e);
        }
    }

    @Override
    public OssInfo getInfo(String targetName, Boolean isRecursion) {
        String key = getKey(targetName, true);
        OssInfo ossInfo = getBaseInfo(key);
        if (isRecursion && ftp.isDir(key)) {
            FTPFile[] ftpFiles = ftp.lsFiles(key);
            List<OssInfo> fileOssInfos = new ArrayList<>();
            List<OssInfo> directoryInfos = new ArrayList<>();
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.isDirectory()) {
                    directoryInfos.add(getInfo(targetName + CharPool.SLASH + ftpFile.getName(), true));
                } else {
                    fileOssInfos.add(getInfo(targetName + CharPool.SLASH + ftpFile.getName(), false));
                }
            }
            ReflectUtil.setFieldValue(ossInfo, "fileInfos", fileOssInfos);
            ReflectUtil.setFieldValue(ossInfo, "directoryInfos", directoryInfos);
        }
        return ossInfo;
    }

    @Override
    public Boolean isExist(String targetName) {
        return ftp.exist(getKey(targetName, true));
    }

    @Override
    public Boolean isFile(String targetName) {
        return !isDirectory(targetName);
    }

    @Override
    public Boolean isDirectory(String targetName) {
        return ftp.isDir(getKey(targetName, true));
    }

    @Override
    public String getBasePath() {
        return ftpOssConfig.getBasePath();
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<String, Object>() {
            {
                put(FTP_OBJECT_NAME, getFtp());
            }
        };
    }

    private OssInfo getBaseInfo(String targetName) {
        String name = FileNameUtil.getName(targetName);
        String path = OssPathUtil.replaceKey(targetName, name, true);
        FTPFile targetFtpFile = null;
        OssInfo ossInfo;
        if (ftp.isDir(targetName)) {
            ossInfo = new DirectoryOssInfo();
            FTPFile[] ftpFiles = ftp.lsFiles(OssPathUtil.convertPath(Paths.get(targetName).getParent().toString(), true));
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.getName().equals(name)) {
                    targetFtpFile = ftpFile;
                    break;
                }
            }
        } else {
            ossInfo = new FileOssInfo();
            FTPFile[] ftpFiles = ftp.lsFiles(targetName);
            if (ArrayUtil.isNotEmpty(ftpFiles)) {
                targetFtpFile = ftpFiles[0];
            }
        }
        if (ObjectUtil.isNotEmpty(targetFtpFile)) {
            if (targetFtpFile.isFile()) {
                ossInfo = new FileOssInfo();
            }
            ossInfo.setName(name);
            ossInfo.setPath(path);
            ossInfo.setLength(Convert.toStr(targetFtpFile.getSize()));
            ossInfo.setCreateTime(DateUtil.date(targetFtpFile.getTimestamp()).toString(DatePattern.NORM_DATETIME_PATTERN));
            ossInfo.setLastUpdateTime(DateUtil.date(targetFtpFile.getTimestamp()).toString(DatePattern.NORM_DATETIME_PATTERN));
        }
        return ossInfo;
    }

	public Ftp getFtp() {
		return ftp;
	}

	public void setFtp(Ftp ftp) {
		this.ftp = ftp;
	}

	public FtpOssConfig getFtpOssConfig() {
		return ftpOssConfig;
	}

	public void setFtpOssConfig(FtpOssConfig ftpOssConfig) {
		this.ftpOssConfig = ftpOssConfig;
	}
}
