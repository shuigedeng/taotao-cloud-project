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

package com.taotao.cloud.order.application.command.order.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 交易投诉 参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "交易投诉 参数")
public record OrderComplaintOperationAddCmd(
        @Schema(description = "要更改的状态状态") String complainStatus,
        @Schema(description = "交易投诉主键") Long complainId,
        @Schema(description = "商家申诉内容") String appealContent,
        @Schema(description = "商家申诉上传的图片") List<String> images,
        @Schema(description = "仲裁结果") String arbitrationResult)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;
}
