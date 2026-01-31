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

package com.taotao.cloud.sys.biz.supports.largefile.po;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;
import lombok.NoArgsConstructor;
import lombok.experimental.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileUploadRequest
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Accessors(chain = true)
public class FileUploadRequest {

    // 上传文件到指定目录
    private String path;
    // 上传文件的文件名称
    private String name;
    // 任务ID
    private String id;
    // 总分片数量
    private Integer chunks;
    // 当前为第几块分片
    private Integer chunk;
    // 按多大的文件粒度进行分片
    private Long chunkSize;
    // 分片对象
    private MultipartFile file;
    // MD5
    private String md5;

    // 当前分片大小
    private Long size;
}
