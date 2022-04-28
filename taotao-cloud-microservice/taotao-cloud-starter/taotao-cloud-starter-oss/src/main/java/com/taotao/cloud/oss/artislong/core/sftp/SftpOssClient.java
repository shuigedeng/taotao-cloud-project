package com.taotao.cloud.oss.artislong.core.sftp;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.sftp.model.SftpOssConfig;
import com.taotao.cloud.oss.artislong.exception.OssException;
import com.taotao.cloud.oss.artislong.model.DirectoryOssInfo;
import com.taotao.cloud.oss.artislong.model.FileOssInfo;
import com.taotao.cloud.oss.artislong.model.OssInfo;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sftp oss客户
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:57
 */
public class SftpOssClient implements StandardOssClient {

    public static final String SFTP_OBJECT_NAME = "sftp";

    private Sftp sftp;
    private SftpOssConfig sftpOssConfig;

	public SftpOssClient(Sftp sftp, SftpOssConfig sftpOssConfig) {
		this.sftp = sftp;
		this.sftpOssConfig = sftpOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        String key = getKey(targetName, true);
        String parentPath = OssPathUtil.convertPath(Paths.get(key).getParent().toString(), true);
        if (!sftp.exist(parentPath)) {
            sftp.mkDirs(parentPath);
        }
        if (isOverride || !sftp.exist(key)) {
            sftp.upload(parentPath, FileNameUtil.getName(targetName), is);
        }
        return getInfo(targetName);
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        String key = getKey(targetName, true);
        String parentPath = OssPathUtil.convertPath(Paths.get(key).getParent().toString(), true);
        if (!sftp.exist(parentPath)) {
            sftp.mkDirs(parentPath);
        }
        sftp.put(file.getPath(), parentPath, new DefaultSftpProgressMonitor(file.length()), Sftp.Mode.RESUME);
        return getInfo(targetName);
    }

    @Override
    public void downLoad(OutputStream os, String targetName) {
        sftp.download(getKey(targetName, true), os);
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
        try {
            OssInfo ossInfo = getInfo(targetName, false);
            long skip = localFile.exists() ? localFile.length() : 0;
            OutputStream os = new FileOutputStream(localFile);
            ChannelSftp sftpClient = sftp.getClient();
            sftpClient.get(getKey(targetName, true), os, new DefaultSftpProgressMonitor(Convert.toLong(ossInfo.getLength())), Sftp.Mode.RESUME.ordinal(), skip);
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public void delete(String targetName) {
        String key = getKey(targetName, true);
        if (isDirectory(targetName)) {
            sftp.delDir(key);
        } else {
            sftp.delFile(key);
        }
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
	    LogUtil.warn("sftp协议不支持copy命令");
    }

    @Override
    public void move(String sourceName, String targetName, Boolean isOverride) {
	    LogUtil.warn("sftp协议不支持move命令");
    }

    @Override
    public void rename(String sourceName, String targetName, Boolean isOverride) {
        String newSourceName = getKey(sourceName, true);
        String newTargetName = getKey(targetName, true);
        try {
            if (isOverride || !isExist(newTargetName)) {
                sftp.getClient().rename(newSourceName, newTargetName);
            }
        } catch (SftpException e) {
	        LogUtil.error("{}重命名为{}失败,错误信息为：", newSourceName, newTargetName, e);
        }
    }

    @Override
    public OssInfo getInfo(String targetName, Boolean isRecursion) {
        String key = getKey(targetName, true);
        OssInfo ossInfo = getBaseInfo(key);
        if (isRecursion && sftp.isDir(key)) {
            List<ChannelSftp.LsEntry> lsEntries = sftp.lsEntries(key);
            List<OssInfo> fileOssInfos = new ArrayList<>();
            List<OssInfo> directoryInfos = new ArrayList<>();
            for (ChannelSftp.LsEntry lsEntry : lsEntries) {
                SftpATTRS attrs = lsEntry.getAttrs();
                String target = OssPathUtil.convertPath(targetName + CharPool.SLASH + lsEntry.getFilename(), true);
                if (attrs.isDir()) {
                    directoryInfos.add(getInfo(target, true));
                } else {
                    fileOssInfos.add(getInfo(target, false));
                }
            }
            ReflectUtil.setFieldValue(ossInfo, "fileInfos", fileOssInfos);
            ReflectUtil.setFieldValue(ossInfo, "directoryInfos", directoryInfos);
        }
        return ossInfo;
    }

    @Override
    public Boolean isExist(String targetName) {
        return sftp.exist(getKey(targetName, true));
    }

    @Override
    public Boolean isFile(String targetName) {
        return !isDirectory(targetName);
    }

    @Override
    public Boolean isDirectory(String targetName) {
        return sftp.isDir(getKey(targetName, true));
    }

    @Override
    public String getBasePath() {
        return sftpOssConfig.getBasePath();
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<String, Object>() {
            {
                put(SFTP_OBJECT_NAME, getSftp());
            }
        };
    }

    private OssInfo getBaseInfo(String targetName) {
        String name = FileNameUtil.getName(targetName);
        String path = OssPathUtil.replaceKey(name, getBasePath(), true);
        ChannelSftp.LsEntry targetLsEntry = null;
        OssInfo ossInfo;
        if (sftp.isDir(targetName)) {
            ossInfo = new DirectoryOssInfo();
            List<ChannelSftp.LsEntry> lsEntries = sftp.lsEntries(OssPathUtil.convertPath(Paths.get(targetName).getParent().toString(), true));
            for (ChannelSftp.LsEntry lsEntry : lsEntries) {
                if (lsEntry.getFilename().equals(name)) {
                    targetLsEntry = lsEntry;
                    break;
                }
            }
        } else {
            ossInfo = new FileOssInfo();
            List<ChannelSftp.LsEntry> lsEntries = sftp.lsEntries(targetName);
            if (!lsEntries.isEmpty()) {
                targetLsEntry = lsEntries.get(0);
            }
        }
        if (ObjectUtil.isNotEmpty(targetLsEntry)) {
            SftpATTRS sftpattrs = targetLsEntry.getAttrs();
            if (!sftpattrs.isDir()) {
                ossInfo = new FileOssInfo();
            }
            ossInfo.setName(name);
            ossInfo.setPath(path);
            ossInfo.setLength(sftpattrs.getSize());
            ossInfo.setCreateTime(DateUtil.date(sftpattrs.getMTime() * 1000L).toString(DatePattern.NORM_DATETIME_PATTERN));
            ossInfo.setLastUpdateTime(DateUtil.date(sftpattrs.getATime() * 1000L).toString(DatePattern.NORM_DATETIME_PATTERN));
        }
        return ossInfo;
    }

	public Sftp getSftp() {
		return sftp;
	}

	public void setSftp(Sftp sftp) {
		this.sftp = sftp;
	}

	public SftpOssConfig getSftpOssConfig() {
		return sftpOssConfig;
	}

	public void setSftpOssConfig(SftpOssConfig sftpOssConfig) {
		this.sftpOssConfig = sftpOssConfig;
	}
}
