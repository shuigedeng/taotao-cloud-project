package com.taotao.cloud.sys.biz.modules.core.service.storage;

import com.taotao.cloud.sys.biz.modules.core.service.file.FileManagerProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Service
public class FileStorage implements DataFileStorage, InitializingBean {
    private FileManagerProperties fileManagerProperties;

    @Override
    public OutputStream getOutputStream(String path,DataType dataType) throws FileNotFoundException {
        final File data = dataType == DataType.data ? fileManagerProperties.getData() : fileManagerProperties.getTmp();
        final File file = new File(data, path);
        if (!file.exists()){
            file.mkdirs();
        }
        return new FileOutputStream(file);
    }

    @Override
    public InputStream getInputStream(String path,DataType dataType) throws FileNotFoundException {
        final File data = dataType == DataType.data  ? fileManagerProperties.getData() : fileManagerProperties.getTmp();
        final File file = new File(data, path);
        if (!file.exists()){
            file.mkdirs();
        }
        return new FileInputStream(file);
    }

    @Override
    public List<String> list(DataType dataType, String parentPath) {
        final File data = dataType == DataType.data  ? fileManagerProperties.getData() : fileManagerProperties.getTmp();
        final File file = new File(data, parentPath);
        if (file.exists()){
            return Arrays.asList(file.list());
        }
        return new ArrayList<>();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        File data = fileManagerProperties.getData();
        File tmp = fileManagerProperties.getTmp();
        File base = fileManagerProperties.getBase();

        if (data != null && !data.exists()){
            data.mkdirs();
        }
        if (tmp != null && !tmp.exists()){
            tmp.mkdirs();
        }

        if (base != null){
            if (!base.exists()){
                base.mkdirs();
            }
            if (data == null){
                data = new File(base,"data");
                data.mkdir();
                fileManagerProperties.setData(data);
            }
            if (tmp == null){
                tmp = new File(base,"tmp");
                tmp.mkdir();
                fileManagerProperties.setTmp(tmp);
            }
        }

        Assert.notNull(data, "data Dir cannot be null");
        Assert.notNull(tmp, "tmp Dir cannot be null");
    }
}
