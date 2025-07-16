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

package com.taotao.cloud.hadoop.hdfs.service;

import java.util.List;
import java.util.Map;
import org.apache.hadoop.fs.BlockLocation;
import org.springframework.web.multipart.MultipartFile;

/**
 * HdfsService
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 15:17
 */
public interface HdfsService {

    boolean mkdir(String path) throws Exception;

    boolean existFile(String path) throws Exception;

    List<Map<String, Object>> readPathInfo(String path) throws Exception;

    void createFile(String path, MultipartFile file) throws Exception;

    String readFile(String path) throws Exception;

    List<Map<String, String>> listFile(String path) throws Exception;

    boolean renameFile(String oldName, String newName) throws Exception;

    boolean deleteFile(String path) throws Exception;

    void uploadFile(String path, String uploadPath) throws Exception;

    void downloadFile(String path, String downloadPath) throws Exception;

    void copyFile(String sourcePath, String targetPath) throws Exception;

    byte[] openFileToBytes(String path) throws Exception;

    <T> T openFileToObject(String path, Class<T> clazz) throws Exception;

    BlockLocation[] getFileBlockLocations(String path) throws Exception;
}
