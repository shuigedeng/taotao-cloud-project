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

package com.taotao.cloud.file.biz.filestorage.aspect;

import com.taotao.cloud.oss.common.storage.FileInfo;
import com.taotao.cloud.oss.common.storage.UploadPretreatment;
import com.taotao.cloud.oss.common.storage.aspect.DeleteAspectChain;
import com.taotao.cloud.oss.common.storage.aspect.DownloadAspectChain;
import com.taotao.cloud.oss.common.storage.aspect.DownloadThAspectChain;
import com.taotao.cloud.oss.common.storage.aspect.ExistsAspectChain;
import com.taotao.cloud.oss.common.storage.aspect.FileStorageAspect;
import com.taotao.cloud.oss.common.storage.aspect.UploadAspectChain;
import com.taotao.cloud.oss.common.storage.platform.FileStorage;
import com.taotao.cloud.oss.common.storage.recorder.FileRecorder;
import java.io.InputStream;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** 使用切面打印文件上传和删除的日志 */
@Slf4j
@Component
public class LogFileStorageAspect implements FileStorageAspect {

    /** 上传，成功返回文件信息，失败返回 null */
    @Override
    public FileInfo uploadAround(
            UploadAspectChain chain,
            FileInfo fileInfo,
            UploadPretreatment pre,
            FileStorage fileStorage,
            FileRecorder fileRecorder) {
        log.info("上传文件 before -> {}", fileInfo);
        fileInfo = chain.next(fileInfo, pre, fileStorage, fileRecorder);
        log.info("上传文件 after -> {}", fileInfo);
        return fileInfo;
    }

    /** 删除文件，成功返回 true */
    @Override
    public boolean deleteAround(
            DeleteAspectChain chain, FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder) {
        log.info("删除文件 before -> {}", fileInfo);
        boolean res = chain.next(fileInfo, fileStorage, fileRecorder);
        log.info("删除文件 after -> {}", res);
        return res;
    }

    /** 文件是否存在 */
    @Override
    public boolean existsAround(ExistsAspectChain chain, FileInfo fileInfo, FileStorage fileStorage) {
        log.info("文件是否存在 before -> {}", fileInfo);
        boolean res = chain.next(fileInfo, fileStorage);
        log.info("文件是否存在 after -> {}", res);
        return res;
    }

    /** 下载文件 */
    @Override
    public void downloadAround(
            DownloadAspectChain chain, FileInfo fileInfo, FileStorage fileStorage, Consumer<InputStream> consumer) {
        log.info("下载文件 before -> {}", fileInfo);
        chain.next(fileInfo, fileStorage, consumer);
        log.info("下载文件 after -> {}", fileInfo);
    }

    /** 下载缩略图文件 */
    @Override
    public void downloadThAround(
            DownloadThAspectChain chain, FileInfo fileInfo, FileStorage fileStorage, Consumer<InputStream> consumer) {
        log.info("下载缩略图文件 before -> {}", fileInfo);
        chain.next(fileInfo, fileStorage, consumer);
        log.info("下载缩略图文件 after -> {}", fileInfo);
    }
}
