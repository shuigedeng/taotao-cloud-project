package com.taotao.cloud.sys.biz.modules.versioncontrol.project.compile;


import com.taotao.cloud.sys.biz.modules.core.dtos.RelativeFile;
import com.taotao.cloud.sys.biz.modules.core.utils.OnlyPath;
import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.CompileFiles;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos.DiffChanges;

import java.io.File;

public interface CompileResolve {
    /**
     * 编译文件解析
     * @param diffFile 文件信息
     * @param modulePath 相对于项目路径的路径
     * @return
     */
    CompileFiles.DiffCompileFile resolve(DiffChanges.DiffFile diffFile, RelativeFile modulePath, OnlyPath projectPath);

    /**
     * 是否支持当前文件类型的解析
     * @param diffFile
     * @return
     */
    boolean support(DiffChanges.DiffFile diffFile);

    default File resolveCompilePath(RelativeFile modulePath){
        final File moduleDir = modulePath.relativeFile();
        return new File(moduleDir,"target/classes");
    }

}
