package com.taotao.cloud.sys.biz.api.controller.tools.core.service.storage;

import java.io.*;
import java.util.List;

/**
 * 生成的文件, 数据存储
 */
public interface DataFileStorage {

    public enum DataType{
        data,tmp
    }

    OutputStream getOutputStream(String path,DataType dataType) throws IOException;

    InputStream getInputStream(String path,DataType dataType) throws IOException;

    List<String> list(DataType dataType,String parentPath);
}
