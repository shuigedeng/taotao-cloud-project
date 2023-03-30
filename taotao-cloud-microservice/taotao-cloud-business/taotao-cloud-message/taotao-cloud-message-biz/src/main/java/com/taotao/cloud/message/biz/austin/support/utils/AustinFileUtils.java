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

package com.taotao.cloud.message.biz.austin.support.utils;

import cn.hutool.core.io.IoUtil;
import com.google.common.base.Throwables;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 3y
 * @date 2023/2/14
 */
@Slf4j
public class AustinFileUtils {

    /**
     * 读取 远程链接 返回File对象
     *
     * @param path 文件路径
     * @param remoteUrl cdn/oss文件访问链接
     * @return
     */
    public static File getRemoteUrl2File(String path, String remoteUrl) {
        try {
            URL url = new URL(remoteUrl);
            File file = new File(path, url.getPath());
            if (!file.exists()) {
                IoUtil.copy(url.openStream(), new FileOutputStream(file));
            }
            return file;
        } catch (Exception e) {
            log.error(
                    "AustinFileUtils#getRemoteUrl2File fail:{},remoteUrl:{}",
                    Throwables.getStackTraceAsString(e),
                    remoteUrl);
        }
        return null;
    }
}
