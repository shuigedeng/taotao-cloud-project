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

package com.taotao.cloud.sys.biz.supports.largefile.controller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.sys.biz.supports.largefile.service.LargeFileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * LargeFileController
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Controller
@RequestMapping(value = "/largefile")
@Slf4j
public class LargeFileController {

    @Autowired
    private LargeFileService largeFileService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @GetMapping(value = "/")
    public String gotoPage() {
        return "index";
    }

    @GetMapping(value = "/uploadFile")
    public String gotoFilePage() {
        return "upload";
    }

    @GetMapping(value = "/oss/upload")
    public String gotoOssPage() {
        return "ossUpload";
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public Result<FileUpload> upload( FileUploadRequest fileUploadRequestDTO ) throws IOException {

        // boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        boolean isMultipart = true;
        FileUpload fileUploadDTO = null;
        if (isMultipart) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("upload");
            if (fileUploadRequestDTO.getChunk() != null && fileUploadRequestDTO.getChunks() > 0) {
                fileUploadDTO = largeFileService.sliceUpload(fileUploadRequestDTO);
            } else {
                fileUploadDTO = largeFileService.upload(fileUploadRequestDTO);
            }
            stopWatch.stop();
            log.info("{}", stopWatch.prettyPrint());

            return Result.success(fileUploadDTO);
        }

        throw new RuntimeException("上传失败");
    }

    @RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
    @ResponseBody
    public Result<FileUpload> checkFileMd5( String md5, String path ) throws IOException {
        FileUploadRequest param = new FileUploadRequest().setPath(path).setMd5(md5);
        FileUpload fileUploadDTO = largeFileService.checkFileMd5(param);

        return Result.success(fileUploadDTO);
    }

    @PostMapping("/download")
    public void download( FileDownloadRequest requestDTO ) {
        try {
            FileUtil.downloadFile(requestDTO.getName(), requestDTO.getPath(), request, response);
        } catch (FileNotFoundException e) {
            log.error("download error:" + e.getMessage(), e);
            throw new RuntimeException("文件下载失败");
        }
    }
}
