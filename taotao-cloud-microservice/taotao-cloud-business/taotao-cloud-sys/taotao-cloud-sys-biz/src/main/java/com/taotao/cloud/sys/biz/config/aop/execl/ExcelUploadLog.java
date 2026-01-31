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

package com.taotao.cloud.sys.biz.config.aop.execl;

import java.util.Date;

import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/**
 * ExcelUploadLog
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Data
public class ExcelUploadLog {

    private Integer id;
    // 唯一编码
    private String batchNo;
    // 上传到文件服务器的文件key
    private String key;
    // 错误日志文件名
    private String fileName;
    // 上传状态
    private Integer status;
    // 上传人
    private String createName;
    // 上传类型
    private String uploadType;
    // 结束时间
    private Date endTime;
    // 开始时间
    private Date startTime;
}
