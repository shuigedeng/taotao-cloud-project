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

package com.taotao.cloud.sys.biz.supports.largefile.util;

import com.taotao.cloud.file.biz.largefile.constant.FileConstant;
import com.taotao.cloud.file.biz.largefile.po.FileUploadRequest;
import com.taotao.cloud.sys.biz.file.src.main.java.com.taotao.cloud.file.biz.largefile.util.SystemUtil;

import java.io.File;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * FilePathUtil
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Component
@Slf4j
public class FilePathUtil implements ApplicationRunner {

    @Value("${upload.root.dir:/}")
    private String uploadRootDir;

    @Value("${upload.window.root:/}")
    private String uploadWindowRoot;

    @Override
    public void run( ApplicationArguments args ) throws Exception {
        createUploadRootDir();
    }

    private void createUploadRootDir() {
        String path = getBasePath();
        File file = new File(path);
        if (!file.mkdirs()) {
            file.mkdirs();
        }
    }

    public String getPath() {
        return uploadRootDir;
    }

    public String getBasePath() {
        String path = uploadRootDir;
        if (SystemUtil.isWinOs()) {
            path = uploadWindowRoot + uploadRootDir;
        }

        return path;
    }

    public String getPath( FileUploadRequest param ) {
        String path = this.getBasePath()
                + FileConstant.FILE_SEPARATORCHAR
                + param.getPath()
                + FileConstant.FILE_SEPARATORCHAR
                + param.getMd5();
        return path;
    }
}
