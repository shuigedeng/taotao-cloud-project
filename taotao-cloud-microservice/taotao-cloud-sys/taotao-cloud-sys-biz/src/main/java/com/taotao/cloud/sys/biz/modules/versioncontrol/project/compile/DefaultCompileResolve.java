package com.taotao.cloud.sys.biz.modules.versioncontrol.project.compile;

import com.taotao.cloud.sys.biz.modules.core.dtos.RelativeFile;
import com.taotao.cloud.sys.biz.modules.core.utils.OnlyPath;
import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.CompileFiles;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos.DiffChanges;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 最后找不到处理器的, 使用默认处理器 <br/>
 *
 * 文件处理方式和 webroot 处理方式一致
 */
@Component
@Order(2000)
@Slf4j
public class DefaultCompileResolve extends WebRootCompileResolve {

    @Override
    public CompileFiles.DiffCompileFile resolve(DiffChanges.DiffFile diffFile, RelativeFile modulePath, OnlyPath projectPath) {
        log.warn("文件 [{}] 未找到编译处理器, 将直接复制",diffFile.path());
        final CompileFiles.DiffCompileFile resolve = super.resolve(diffFile, modulePath,projectPath);
        return resolve;
    }

    @Override
    public boolean support(DiffChanges.DiffFile diffFile) {
        return true;
    }
}
