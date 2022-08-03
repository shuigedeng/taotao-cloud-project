package com.taotao.cloud.sys.biz.api.controller.tools.classloader;

import com.taotao.cloud.sys.biz.api.controller.tools.classloader.dtos.LoadClassResponse;

import java.io.File;
import java.util.Collection;

public interface FileLoadClassHandler {

    /**
     * 处理文件列表
     * @param files 文件列表
     * @param targetClassloaderDir 目录类加载器目录
     * @param loadClassResponse
     */
    void handle(Collection<File> files, File targetClassloaderDir, LoadClassResponse loadClassResponse);
}
