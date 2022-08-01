package com.taotao.cloud.sys.biz.modules.classloader.impl;

import com.sanri.tools.modules.classloader.FileLoadClassHandler;
import com.sanri.tools.modules.classloader.dtos.LoadClassResponse;
import com.sanri.tools.modules.compiler.JavaCompilerService;
import com.sanri.tools.modules.compiler.dtos.CompileResult;
import com.sanri.tools.modules.compiler.dtos.SourceCompileConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * java 加载类
 */
@Component("java_FileLoadClassHandler")
@Slf4j
public class JavaFileLoadClassHandler implements FileLoadClassHandler {
    @Autowired
    private ClassFileLoadClassHandler classFileLoadClassHandler;

    /**
     * 循环依赖, 只能使用字段注入
     */
    @Autowired
    private JavaCompilerService javaCompilerService;

    @Override
    public void handle(Collection<File> files, File targetClassloaderDir, LoadClassResponse loadClassResponse) {
        List<File> allClassFiles = new ArrayList<>();
        for (File javaFile : files) {
            try {
                final String className = FilenameUtils.getBaseName(javaFile.getName());
                final String content = FileUtils.readFileToString(javaFile, StandardCharsets.UTF_8);
                final SourceCompileConfig sourceCompileConfig = new SourceCompileConfig(className,content);
                sourceCompileConfig.setClassloaderName(loadClassResponse.getExtendClassloader().getName());
                try {
                    final CompileResult compileResult = javaCompilerService.compileSourceFile(sourceCompileConfig);

                    final Collection<File> classFiles = compileResult.getClassFiles();
                    allClassFiles.addAll(classFiles);
                }catch (Exception e){
                    log.error("源文件[{}]编译失败, 将不会有 class 加载",javaFile);
                }
            } catch (IOException e) {
                log.error("编译文件[{}]失败 {}",javaFile.getName(),e.getMessage(),e);
            }
        }

        classFileLoadClassHandler.handle(allClassFiles,targetClassloaderDir,loadClassResponse);
    }
}
