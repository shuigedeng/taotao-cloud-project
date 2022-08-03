package com.taotao.cloud.sys.biz.api.controller.tools.classloader.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.taotao.cloud.sys.biz.api.controller.tools.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.api.controller.tools.classloader.FileLoadClassHandler;
import com.taotao.cloud.sys.biz.api.controller.tools.classloader.dtos.LoadClassResponse;
import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.OnlyPath;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;


/**
 * jar 文件处理
 */
@Component("jar_FileLoadClassHandler")
@Slf4j
public class JarFileLoadClassHandler implements FileLoadClassHandler {

    protected OnlyPath jarsPath = new OnlyPath("jars");

    @Override
    public void handle(Collection<File> files, File targetClassloaderDir, LoadClassResponse loadClassResponse) {
        // jar 文件, 直接复制到目标目录即可, 相对目标路径为 $targetDir/jars
        final File jarsDir = jarsPath.resolveFile(targetClassloaderDir);
        jarsDir.mkdirs();
        for (File file : files) {
            try {
                FileUtils.copyFileToDirectory(file,jarsDir);
                ReflectionUtils.invokeMethod(ClassloaderService.addURLMethod,loadClassResponse.getExtendClassloader(),file.toURI().toURL());
                loadClassResponse.addJar(file.getName());
            } catch (IOException e) {
                log.error("jar 复制出错: {}",e.getMessage(),e);
            }
        }
    }

}