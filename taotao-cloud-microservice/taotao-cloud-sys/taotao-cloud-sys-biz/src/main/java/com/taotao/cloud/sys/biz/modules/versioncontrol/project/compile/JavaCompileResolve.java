package com.taotao.cloud.sys.biz.modules.versioncontrol.project.compile;

import com.taotao.cloud.sys.biz.modules.core.dtos.RelativeFile;
import com.taotao.cloud.sys.biz.modules.core.utils.OnlyPath;
import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.CompileFiles;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos.DiffChanges;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专门解析 java 文件的编译
 */
@Component
@Order(500)
@Slf4j
public class JavaCompileResolve implements CompileResolve{

    public static final OnlyPath SOURCES_CODE = new OnlyPath("src/main/java");

    @Override
    public CompileFiles.DiffCompileFile resolve(DiffChanges.DiffFile diffFile, RelativeFile modulePath, OnlyPath projectPath) {
        // 编译文件路径
        final File compileDir = resolveCompilePath(modulePath);

        final CompileFiles.DiffCompileFile diffCompileFile = new CompileFiles.DiffCompileFile(diffFile,modulePath,projectPath);

        // 编译后文件路径信息 com/sanri/xxx/xxx.java
        final OnlyPath javaFilePath = SOURCES_CODE.relativize(modulePath.path().relativize(diffFile.path()));

        // 获取类名
        final String fileName = javaFilePath.getFileName();
        final String className = FilenameUtils.getBaseName(fileName);

        // 获取编译文件查找路径
        final File classFindPath = javaFilePath.getParent().resolveFile(compileDir);

        // 如果编译路径存在的话, 去查询编译后的文件
        if (classFindPath.exists()) {
            final AndFileFilter andFileFilter = new AndFileFilter(new WildcardFileFilter(className + "*"), new SuffixFileFilter("class"));
            final Collection<File> files = FileUtils.listFiles(compileDir, andFileFilter, TrueFileFilter.INSTANCE);

            // 类名前缀相同的去掉, 比如有这样两个类 AA.class AABB.class , 当查找 AA.class 时, 也会把 AABB.class 找出来
            final Iterator<File> iterator = files.iterator();
            while (iterator.hasNext()){
                final File file = iterator.next();
//                if (!(className + ".class").equalsIgnoreCase(file.getName()) && !file.getName().contains("$")){
//                    // 去掉找到的错误文件(名称同前缀的)
//                    iterator.remove();
//                }
                // 删除找到的错误文件
                final String baseName = FilenameUtils.getBaseName(file.getName());
                if (baseName.equals(className)){
                    continue;
                }

                if (baseName.charAt(className.length()) != '$'){
                    // 上面去除错误文件存在漏洞 当文件为 AABB$XX.class 的文件删除不掉
                    iterator.remove();
                }
            }

            final OnlyPath compilePath = new OnlyPath(compileDir);
            final List<RelativeFile> relativeFiles = files.stream().map(file -> {
                final OnlyPath binFilePath = compilePath.relativize(new OnlyPath(file));
                return new RelativeFile(compileDir, binFilePath);
            }).collect(Collectors.toList());

            diffCompileFile.setCompileFiles(relativeFiles);
            return diffCompileFile;
        }
        log.warn("编译路径[{}] 不存在, 可能还未编译",classFindPath);

        return diffCompileFile;
    }

    @Override
    public boolean support(DiffChanges.DiffFile diffFile) {
        final String fileName = new File(diffFile.path()).getName();
        final String extension = FilenameUtils.getExtension(fileName);
        return "java".equalsIgnoreCase(extension);
    }
}
