package com.taotao.cloud.sys.biz.modules.classloader.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;

import com.sanri.tools.modules.core.utils.OnlyPath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.stereotype.Component;

import com.sanri.tools.modules.classloader.FileLoadClassHandler;
import com.sanri.tools.modules.classloader.dtos.LoadClassResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import static com.sanri.tools.modules.classloader.ClassloaderService.addURLMethod;

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
                ReflectionUtils.invokeMethod(addURLMethod,loadClassResponse.getExtendClassloader(),file.toURI().toURL());
                loadClassResponse.addJar(file.getName());
            } catch (IOException e) {
                log.error("jar 复制出错: {}",e.getMessage(),e);
            }
        }
    }

}
