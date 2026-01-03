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

package com.taotao.cloud.sys.biz;

import com.taotao.boot.oss.common.storage.FileInfo;
import com.taotao.boot.oss.common.storage.FileStorageService;
import com.taotao.boot.oss.common.storage.UploadPretreatment;

import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * FileStorageServiceTest
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
@SpringBootTest
class FileStorageServiceTest {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 单独对文件上传进行测试
     */
    @Test
    public void upload() {
        String filename = "image.jpg";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);

        UploadPretreatment uploadPretreatment = fileStorageService.of(in);

        uploadPretreatment.setName("file");
        uploadPretreatment.setOriginalFilename(filename);
        uploadPretreatment.setPath("test/");
        uploadPretreatment.thumbnail();
        uploadPretreatment.putAttr("role", "admin");
        FileInfo fileInfo = uploadPretreatment.upload();
        Assertions.notNull(fileInfo, "文件上传失败！");
        log.info("文件上传成功：{}", fileInfo.toString());
    }

    /**
     * 测试根据 url 上传文件
     */
    @Test
    public void uploadByURL() {
        String url = "https://www.xuyanwu.cn/file/upload/1566046282790-1.png";
        UploadPretreatment uploadPretreatment = fileStorageService.of(url);
        uploadPretreatment.thumbnail();
        uploadPretreatment.setPath("test/");
        uploadPretreatment.setObjectId("0");
        uploadPretreatment.setObjectType("0");
        FileInfo fileInfo = uploadPretreatment.upload();
        Assertions.notNull(fileInfo, "文件上传失败！");
        log.info("文件上传成功：{}", fileInfo.toString());
    }

    /**
     * 测试上传并删除文件
     */
    @Test
    public void delete() {
        String filename = "image.jpg";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        UploadPretreatment uploadPretreatment = fileStorageService.of(in);
        uploadPretreatment.setOriginalFilename(filename);
        uploadPretreatment.setPath("test/");
        uploadPretreatment.setObjectId("0");
        uploadPretreatment.setObjectType("0");
        uploadPretreatment.putAttr("role", "admin");
        uploadPretreatment.thumbnail(200, 200);
        FileInfo fileInfo = uploadPretreatment.upload();
        Assertions.notNull(fileInfo, "文件上传失败！");

        log.info("尝试删除已存在的文件：{}", fileInfo);
        boolean delete = fileStorageService.delete(fileInfo.getUrl());
        Assertions.isTrue(delete, "文件删除失败！" + fileInfo.getUrl());
        log.info("文件删除成功：{}", fileInfo);

        fileInfo = BeanUtil.copyProperties(fileInfo, FileInfo.class);
        fileInfo.setFilename(fileInfo.getFilename() + "111.tmp");
        fileInfo.setUrl(fileInfo.getUrl() + "111.tmp");
        log.info("尝试删除不存在的文件：{}", fileInfo);
        delete = fileStorageService.delete(fileInfo);
        Assertions.isTrue(delete, "文件删除失败！" + fileInfo.getUrl());
        log.info("文件删除成功：{}", fileInfo);
    }

    /**
     * 测试上传并验证文件是否存在
     */
    @Test
    public void exists() {
        String filename = "image.jpg";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        UploadPretreatment uploadPretreatment = fileStorageService.of(in);
        uploadPretreatment.setOriginalFilename(filename);
        uploadPretreatment.setPath("test/");
        uploadPretreatment.setObjectId("0");
        uploadPretreatment.setObjectType("0");
        FileInfo fileInfo = uploadPretreatment.upload();
        Assertions.notNull(fileInfo, "文件上传失败！");
        boolean exists = fileStorageService.exists(fileInfo);
        log.info("文件是否存在，应该存在，实际为：{}，文件：{}", exists, fileInfo);
        Assertions.isTrue(exists, "文件是否存在，应该存在，实际为：{}，文件：{}", exists, fileInfo);

        fileInfo = BeanUtil.copyProperties(fileInfo, FileInfo.class);
        fileInfo.setFilename(fileInfo.getFilename() + "111.cc");
        fileInfo.setUrl(fileInfo.getUrl() + "111.cc");
        exists = fileStorageService.exists(fileInfo);
        log.info("文件是否存在，不该存在，实际为：{}，文件：{}", exists, fileInfo);
        Assertions.isFalse(exists, "文件是否存在，不该存在，实际为：{}，文件：{}", exists, fileInfo);
    }

    /**
     * 测试上传并下载文件
     */
    @Test
    public void download() {
        String filename = "image.jpg";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        UploadPretreatment uploadPretreatment = fileStorageService.of(in);
        uploadPretreatment.setOriginalFilename(filename);
        uploadPretreatment.setPath("test/");
        uploadPretreatment.setObjectId("0");
        uploadPretreatment.setObjectType("0");
        uploadPretreatment.setSaveFilename("aaa.jpg");
        uploadPretreatment.setSaveThFilename("bbb");
        uploadPretreatment.thumbnail(200, 200);
        FileInfo fileInfo = uploadPretreatment.upload();
        Assertions.notNull(fileInfo, "文件上传失败！");

        byte[] bytes = fileStorageService
                .download(fileInfo)
                .setProgressMonitor(( progressSize, allSize ) ->
                        log.info("文件下载进度：{} {}%", progressSize, progressSize * 100 / allSize))
                .bytes();
        Assertions.notNull(bytes, "文件下载失败！");
        log.info("文件下载成功，文件大小：{}", bytes.length);

        byte[] thBytes = fileStorageService
                .downloadTh(fileInfo)
                .setProgressMonitor(( progressSize, allSize ) ->
                        log.info("缩略图文件下载进度：{} {}%", progressSize, progressSize * 100 / allSize))
                .bytes();
        Assertions.notNull(thBytes, "缩略图文件下载失败！");
        log.info("缩略图文件下载成功，文件大小：{}", thBytes.length);
    }
}
