package com.taotao.cloud.sys.biz.api.controller.tools.core.controller.dtos;

import java.io.File;

import org.apache.commons.io.FileUtils;

import lombok.Data;

@Data
public class ListFileInfo {
    private String name;
    private String path;
    private long size;
    private boolean directory;
    private long lastUpdateTime;

    public ListFileInfo() {
    }

    public ListFileInfo(String name,long size, boolean directory, long lastUpdateTime) {
        this.name = name;
        this.size = size;
        this.directory = directory;
        this.lastUpdateTime = lastUpdateTime;
    }

    public ListFileInfo(File file) {
        this.name = file.getName();
        this.size = FileUtils.sizeOf(file);
        this.directory = file.isDirectory();
        this.lastUpdateTime = file.lastModified();
    }
}
