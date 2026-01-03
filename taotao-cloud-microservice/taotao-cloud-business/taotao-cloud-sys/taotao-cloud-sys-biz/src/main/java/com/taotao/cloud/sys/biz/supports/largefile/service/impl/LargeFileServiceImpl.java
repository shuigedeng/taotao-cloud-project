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

package com.taotao.cloud.sys.biz.supports.largefile.service.impl;

import com.taotao.cloud.sys.biz.supports.largefile.service.LargeFileService;
import com.taotao.cloud.sys.biz.supports.largefile.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LargeFileServiceImpl
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Service
@Slf4j
public class LargeFileServiceImpl implements LargeFileService, InitializingBean {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private FilePathUtil filePathUtil;

    @Autowired
    private LargefileProperties largefileProperties;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private ExecutorService executorService;
    private CompletionService<FileUpload> completionService;

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = Executors.newFixedThreadPool(
                largefileProperties.getThreadMaxSize(), ( r ) -> {
                    String threadName = "uploadPool-" + atomicInteger.getAndIncrement();
                    Thread thread = new Thread(r);
                    thread.setName(threadName);
                    return thread;
                });
        completionService = new ExecutorCompletionService<>(
                executorService,
                new LinkedBlockingDeque<>(
                        largefileProperties.getQueueMaxSize()));
    }

    @Override
    public FileUpload upload( FileUploadRequest param ) throws IOException {
        if (Objects.isNull(param.getFile())) {
            throw new RuntimeException("file can not be empty");
        }

        param.setPath(FileUtil.withoutHeadAndTailDiagonal(param.getPath()));
        String md5 = FileMD5Util.getFileMD5(param.getFile());
        param.setMd5(md5);

        String filePath = filePathUtil.getPath(param);
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        String path =
                filePath + FileConstant.FILE_SEPARATORCHAR + param.getFile().getOriginalFilename();
        FileOutputStream out = new FileOutputStream(path);
        out.write(param.getFile().getBytes());
        out.flush();
        FileUtil.close(out);

        redisUtil.hset(FileConstant.FILE_UPLOAD_STATUS, md5, "true");

        return FileUpload.builder()
                .path(path)
                .mtime(DateUtil.getCurrentTimeStamp())
                .uploadComplete(true)
                .build();
    }

    @Override
    public FileUpload sliceUpload( FileUploadRequest fileUploadRequestDTO ) {
        try {
            completionService.submit(new FileCallable(UploadModeEnum.RANDOM_ACCESS, fileUploadRequestDTO));

            return completionService.take().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FileUpload checkFileMd5( FileUploadRequest param ) throws IOException {
        Object uploadProgressObj = redisUtil.hget(FileConstant.FILE_UPLOAD_STATUS, param.getMd5());
        if (uploadProgressObj == null) {
            FileUpload fileMd5DTO = FileUpload.builder()
                    .code(FileCheckMd5Status.FILE_NO_UPLOAD.getValue())
                    .build();
            return fileMd5DTO;
        }
        String processingStr = uploadProgressObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = String.valueOf(redisUtil.get(FileConstant.FILE_MD5_KEY + param.getMd5()));
        return fillFileUploadDTO(param, processing, value);
    }

    /**
     * 填充返回文件内容信息
     */
    private FileUpload fillFileUploadDTO( FileUploadRequest param, boolean processing, String value )
            throws IOException {

        if (processing) {
            param.setPath(FileUtil.withoutHeadAndTailDiagonal(param.getPath()));
            String path = filePathUtil.getPath(param);
            return FileUpload.builder()
                    .code(FileCheckMd5Status.FILE_UPLOADED.getValue())
                    .path(path)
                    .build();
        } else {
            File confFile = new File(value);
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            List<Integer> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i);
                }
            }
            return FileUpload.builder()
                    .code(FileCheckMd5Status.FILE_UPLOAD_SOME.getValue())
                    .missChunks(missChunkList)
                    .build();
        }
    }
}
