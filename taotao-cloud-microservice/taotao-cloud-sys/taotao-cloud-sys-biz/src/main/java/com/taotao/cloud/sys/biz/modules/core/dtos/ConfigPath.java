package com.taotao.cloud.sys.biz.modules.core.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * 用于向前端展示文件信息
 * 文件名+是否是目录
 */
public class ConfigPath implements Comparable<ConfigPath>{
    private String pathName;
    private boolean isDirectory;
    @JsonIgnore
    private File file;

    public ConfigPath(String pathName, boolean isDirectory) {
        this.pathName = pathName;
        this.isDirectory = isDirectory;
    }

    public ConfigPath(String pathName, boolean isDirectory, File file) {
        this.pathName = pathName;
        this.isDirectory = isDirectory;
        this.file = file;
    }

    public ConfigPath() {
    }

    public String getPathName() {
        return pathName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public File getFile() {
        return file;
    }

    @Override
    public int compareTo(ConfigPath o) {
        try {
            BasicFileAttributes basicFileAAttributes = Files.readAttributes(this.file.toPath(), BasicFileAttributes.class);
            FileTime aFileaccessTime = basicFileAAttributes.lastAccessTime();

            BasicFileAttributes basicBFileAttributes = Files.readAttributes(o.file.toPath(), BasicFileAttributes.class);
            FileTime bFileaccessTime = basicBFileAttributes.lastAccessTime();

            return bFileaccessTime.compareTo(aFileaccessTime);
        } catch (IOException e) {
            return -1 ;
        }
    }
}
