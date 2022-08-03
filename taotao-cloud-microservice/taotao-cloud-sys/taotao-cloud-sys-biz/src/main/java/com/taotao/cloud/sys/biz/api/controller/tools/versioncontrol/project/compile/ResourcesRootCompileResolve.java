package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.compile;

import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.RelativeFile;
import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.OnlyPath;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos.CompileFiles;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos.DiffChanges;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

/**
 * 资源文件的处理, 原样复制就行
 */
@Component
@Order(800)
public class ResourcesRootCompileResolve implements CompileResolve {

    public static final OnlyPath RESOURCES_ROOT = new OnlyPath("src/main/resources");

    @Override
    public CompileFiles.DiffCompileFile resolve(DiffChanges.DiffFile diffFile, RelativeFile modulePath, OnlyPath projectPath) {
        final File compilePath = resolveCompilePath(modulePath);

        final OnlyPath onlyPath = new OnlyPath(diffFile.path());
        final OnlyPath relativize = RESOURCES_ROOT.relativize(modulePath.path().relativize(onlyPath));
        final CompileFiles.DiffCompileFile diffCompileFile = new CompileFiles.DiffCompileFile(diffFile,modulePath,projectPath);
        diffCompileFile.setCompileFiles(Arrays.asList(new RelativeFile(modulePath.relativeFile(),relativize)));
        return diffCompileFile;
    }

    @Override
    public boolean support(DiffChanges.DiffFile diffFile) {
        final String relativePath = diffFile.path();
        return new OnlyPath(relativePath).startsWith(RESOURCES_ROOT);
    }
}