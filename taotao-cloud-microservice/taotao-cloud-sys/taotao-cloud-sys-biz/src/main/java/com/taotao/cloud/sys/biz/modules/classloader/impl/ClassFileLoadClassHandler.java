package com.taotao.cloud.sys.biz.modules.classloader.impl;

import com.sanri.tools.modules.classloader.FileLoadClassHandler;
import com.sanri.tools.modules.classloader.dtos.LoadClassResponse;
import com.sanri.tools.modules.core.utils.OnlyPath;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.List;

/**
 * 专用于处理 class 文件
 */
@Component("class_FileLoadClassHandler")
@Slf4j
public class ClassFileLoadClassHandler implements FileLoadClassHandler {
    private OnlyPath classPath = new OnlyPath("classes");

    @Override
    public void handle(Collection<File> files, File targetClassloaderDir, LoadClassResponse loadClassResponse) {
        final File classesDir = classPath.resolveFile(targetClassloaderDir);

        // 对于每一个 class 文件, 将分析包路径, 并移动到目标类路径下
        for (File classFile : files) {
            try(final FileInputStream fileInputStream = new FileInputStream(classFile)){
                ClassReader reader = new ClassReader(fileInputStream);
                //创建ClassNode,读取的信息会封装到这个类里面
                ClassNode classNode = new ClassNode();
                reader.accept(classNode, 0);
                final String className = classNode.name.replaceAll("/",".");
                final String packageName = FilenameUtils.getBaseName(className);
                final String classSimpleName = FilenameUtils.getExtension(className);
                if (StringUtils.isBlank(packageName)){
                    log.warn("类[{}]没有包名",className);
                    FileUtils.copyFileToDirectory(classFile,classesDir);
                    continue;

                }
                FileUtils.copyFileToDirectory(classFile,new File(classesDir, RegExUtils.replaceAll(packageName, "\\.", "/")));
            }catch (Exception e){
                log.error("类[{}]加载失败: {}",classFile.getName(),e.getMessage(),e);
            }
        }
    }

    public OnlyPath getClassPath() {
        return classPath;
    }
}
