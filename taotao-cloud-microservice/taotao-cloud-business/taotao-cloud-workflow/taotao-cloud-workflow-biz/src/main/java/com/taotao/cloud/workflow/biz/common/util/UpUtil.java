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

package com.taotao.cloud.workflow.biz.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

public class UpUtil {

    /** 获取上传文件 */
    public static List<MultipartFile> getFileAll() {
        MultipartResolver resolver = new StandardServletMultipartResolver();
        MultipartHttpServletRequest mRequest = resolver.resolveMultipart(ServletUtil.getRequest());
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        List<MultipartFile> list = new ArrayList<>();
        for (Map.Entry<String, MultipartFile> map : fileMap.entrySet()) {
            list.add(map.getValue());
        }
        return list;
    }

    /** 获取文件类型 */
    public static String getFileType(MultipartFile multipartFile) {
        if (multipartFile.getContentType() != null) {
            String[] split = multipartFile.getOriginalFilename().split("\\.");
            if (split.length > 1) {
                return split[split.length - 1];
            }
        }
        return "";
    }
}
