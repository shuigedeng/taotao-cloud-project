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
 * 解析在 sources root 中的文件编译, 这个会在 java 文件处理之后 <br/>
 *
 * 添加这个在后面, 主要是为了 mybatis xml 文件的处理, 有的情况下 xml 文件会放在 source root 中 <br/>
 */
@Component
@Order(600)
public class SourcesRootCompileResolve implements CompileResolve {
    public static final OnlyPath SOURCES_ROOT = new OnlyPath("src/main/java");

    /**
     * 因为 java 文件已经在前面处理过了, 这里其它任何文件都原样搬到编译路径即可
     * @param diffFile 文件信息
     * @param compilePath
     * @param projectDir
     * @return
     */
    @Override
    public CompileFiles.DiffCompileFile resolve(DiffChanges.DiffFile diffFile, RelativeFile modulePath, OnlyPath projectPath) {
        final File compilePath = resolveCompilePath(modulePath);

        final OnlyPath onlyPath = new OnlyPath(diffFile.path());
        final OnlyPath relativize = SOURCES_ROOT.relativize(modulePath.path().relativize(onlyPath));
        final CompileFiles.DiffCompileFile diffCompileFile = new CompileFiles.DiffCompileFile(diffFile,modulePath,projectPath);
        diffCompileFile.setCompileFiles(Arrays.asList(new RelativeFile(modulePath.relativeFile(),relativize)));
        return diffCompileFile;
    }

    @Override
    public boolean support(DiffChanges.DiffFile diffFile) {
        final String relativePath = diffFile.path();
        return new OnlyPath(relativePath).startsWith(SOURCES_ROOT);
    }
}
