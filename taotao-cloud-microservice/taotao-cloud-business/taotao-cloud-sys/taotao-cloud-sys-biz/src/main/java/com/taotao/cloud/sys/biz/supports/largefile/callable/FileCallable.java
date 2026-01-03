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

package com.taotao.cloud.sys.biz.supports.largefile.callable;

import com.taotao.cloud.sys.biz.supports.largefile.context.UploadContext;
import com.taotao.cloud.sys.biz.supports.largefile.enu.UploadModeEnum;
import com.taotao.cloud.sys.biz.supports.largefile.po.FileUploadRequest;

import java.util.concurrent.Callable;

/**
 * FileCallable
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class FileCallable implements Callable<FileUpload> {

    private UploadModeEnum mode;

    private FileUploadRequest param;

    public FileCallable( UploadModeEnum mode, FileUploadRequest param ) {

        this.mode = mode;
        this.param = param;
    }

    @Override
    public FileUpload call() throws Exception {
        return UploadContext.INSTANCE.getInstance(mode).sliceUpload(param);
    }
}
