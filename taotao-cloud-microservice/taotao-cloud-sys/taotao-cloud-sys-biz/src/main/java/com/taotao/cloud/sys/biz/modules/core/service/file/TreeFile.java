package com.taotao.cloud.sys.biz.modules.core.service.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanri.tools.modules.core.utils.OnlyPath;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 树形文件结构
 */
@Data
public class TreeFile {
    private String filename;
    @JsonIgnore
    private OnlyPath wholePath;
    @JsonIgnore
    private OnlyPath parentWholePath;
    @JsonIgnore
    private OnlyPath root;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;
    private List<TreeFile> children = new ArrayList<>();

    public TreeFile() {
    }

    public TreeFile(OnlyPath wholePath,OnlyPath root) {
        this.wholePath = wholePath;
        this.root = root;
        this.filename = wholePath.getFileName();
        final File file = wholePath.toFile();
        if (file.exists()) {
            this.lastModified = new Date(file.lastModified());
        }
    }

    public TreeFile(File file,File root) {
        this.filename = file.getName();
        this.lastModified = new Date(file.lastModified());
        this.wholePath = new OnlyPath(file);
        this.root = new OnlyPath(root);
    }

    /**
     * 相对路径
     * @return
     */
    public String getRelativePath(){
        if (wholePath.equals(root)){
            return wholePath.getFileName();
        }
        return root.relativize(wholePath).toString();
    }

    /**
     * 父级相对路径
     * @return
     */
    public String getParentRelativePath(){
        if (parentWholePath == null){
            return null;
        }
        if (parentWholePath.equals(root)){
            return parentWholePath.getFileName();
        }
        return root.relativize(parentWholePath).toString();
    }

    /**
     * 是否为文件
     * @return
     */
    public boolean isFile(){
        return wholePath.isFile();
    }
}
