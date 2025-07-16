/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.hadoop.hdfs.service.impl;

import com.taotao.cloud.hadoop.hdfs.service.HdfsService;
import com.taotao.cloud.hadoop.hdfs.utils.HDFSUtil;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.fs.BlockLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * HdfsServiceImpl
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 15:18
 */
@Service
public class HdfsServiceImpl implements HdfsService {

    @Autowired private HDFSUtil hdfsUtil;

    @Override
    public boolean mkdir(String path) throws Exception {
        return hdfsUtil.mkdir(path);
    }

    @Override
    public boolean existFile(String path) throws Exception {
        return hdfsUtil.existFile(path);
    }

    @Override
    public List<Map<String, Object>> readPathInfo(String path) throws Exception {
        return hdfsUtil.readPathInfo(path);
    }

    @Override
    public void createFile(String path, MultipartFile file) throws Exception {
        hdfsUtil.createFile(path, file);
    }

    @Override
    public String readFile(String path) throws Exception {
        return hdfsUtil.readFile(path);
    }

    @Override
    public List<Map<String, String>> listFile(String path) throws Exception {
        return hdfsUtil.listFile(path);
    }

    @Override
    public boolean renameFile(String oldName, String newName) throws Exception {
        return hdfsUtil.renameFile(oldName, newName);
    }

    @Override
    public boolean deleteFile(String path) throws Exception {
        return hdfsUtil.deleteFile(path);
    }

    @Override
    public void uploadFile(String path, String uploadPath) throws Exception {
        hdfsUtil.uploadFile(path, uploadPath);
    }

    @Override
    public void downloadFile(String path, String downloadPath) throws Exception {
        hdfsUtil.downloadFile(path, downloadPath);
    }

    @Override
    public void copyFile(String sourcePath, String targetPath) throws Exception {
        hdfsUtil.copyFile(sourcePath, targetPath);
    }

    @Override
    public byte[] openFileToBytes(String path) throws Exception {
        return hdfsUtil.openFileToBytes(path);
    }

    @Override
    public <T> T openFileToObject(String path, Class<T> clazz) throws Exception {
        return hdfsUtil.openFileToObject(path, clazz);
    }

    @Override
    public BlockLocation[] getFileBlockLocations(String path) throws Exception {
        return hdfsUtil.getFileBlockLocations(path);
    }
}
