package com.taotao.cloud.sys.biz.modules.classloader.impl;

import com.taotao.cloud.sys.biz.modules.classloader.FileLoadClassHandler;
import com.taotao.cloud.sys.biz.modules.classloader.dtos.LoadClassResponse;
import com.taotao.cloud.sys.biz.modules.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.modules.core.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * zip 文件处理
 */
@Component("zip_FileLoadClassHandler")
@Slf4j
public class ZipFileLoadClassHandler implements FileLoadClassHandler {

    @Autowired
    private ClassFileLoadClassHandler classFileLoadClassHandler;
    @Autowired
    private FileManager fileManager;

    @Override
    public void handle(Collection<File> files, File targetClassloaderDir, LoadClassResponse loadClassResponse) {
        final File unZipDir = fileManager.mkTmpDir("classloaderTemp/" + targetClassloaderDir.getName() + "/" + System.currentTimeMillis());
        // zip 中放的都是 class 文件, 使用 ClassFileLoadClassHandler 来处理
        for (File zipFile : files) {
            try {
                ZipUtil.unzip(zipFile, unZipDir);
            } catch (IOException e) {
                log.error("解压缩文件失败: {} {}",zipFile.getName(),e.getMessage(),e);
            }
        }

        // 查询所有的 class 文件, 用 ClassFileLoadClassHandler 进行加载
        final SuffixFileFilter classFileFilter = new SuffixFileFilter("class", IOCase.INSENSITIVE);
        final Collection<File> classFiles = FileUtils.listFiles(unZipDir, classFileFilter, TrueFileFilter.INSTANCE);
        classFileLoadClassHandler.handle(classFiles,targetClassloaderDir,loadClassResponse);
    }
}
