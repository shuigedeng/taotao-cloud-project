package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.OnlyPath;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos.CompileFiles;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TarBinFileResult {
    /**
     * 相对于临时路径的文件路径, 这个路径中只包含了打包成功的文件
     */
    private OnlyPath relativePath;

    public TarBinFileResult() {
    }

    public TarBinFileResult(OnlyPath relativePath) {
        this.relativePath = relativePath;
    }

    public String getRelativePath() {
        return relativePath.toString();
    }

    public OnlyPath path(){
        return relativePath;
    }

    @Data
    public static final class BinFileMeta{
        /**
         * 源文件数量
         */
        private int sourceFileCount;
        /**
         * 编译后文件数量
         */
        private int binFileCount;
        /**
         * 删除文件数
         */
        private int deleteFileCount;

        /**
         * 错误文件数
         */
        private int errorFileCount;

        /**
         * 文件信息
         */
        private List<CompileFiles.DiffCompileFile> compileFileInfos = new ArrayList<>();

        public BinFileMeta(List<CompileFiles.DiffCompileFile> compileFileInfos) {
            this.compileFileInfos.addAll(compileFileInfos);
            this.sourceFileCount = compileFileInfos.size();
        }
    }

}
