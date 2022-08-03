package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.OnlyPath;
import lombok.Data;

import java.io.File;

/**
 * 相对路径文件
 */
@Data
public class RelativeFile {
    /**
     * 相对的基础文件
     */
    @JsonIgnore
    private File baseDir;

    /**
     * 相对路径信息
     */
    private OnlyPath path;

    public RelativeFile(File baseDir, String path) {
        this.baseDir = baseDir;
        this.path = new OnlyPath(path);
    }
    public RelativeFile(File baseDir, OnlyPath path) {
        this.baseDir = baseDir;
        this.path = path;
    }

    public String getPath(){
        return path.toString();
    }

    public OnlyPath path(){
        return path;
    }

    /**
     * 获取相对的文件
     * @return
     */
    public File relativeFile(){
        return path.resolveFile(baseDir);
    }

    /**
     * 文件是否存在
     * @return
     */
    public boolean exist(){
        return relativeFile().exists();
    }

    /**
     * 是否是一个文件
     * @return
     */
    public boolean isFile(){
        final File file = relativeFile();
        return file.exists() && file.isFile();
    }

    /**
     * 文件上次修改时间
     * @return
     */
    public Long getLastedModifyTime(){
        final File file = relativeFile();
        if (!file.exists()){
            return null;
        }
        return file.lastModified();
    }
}
