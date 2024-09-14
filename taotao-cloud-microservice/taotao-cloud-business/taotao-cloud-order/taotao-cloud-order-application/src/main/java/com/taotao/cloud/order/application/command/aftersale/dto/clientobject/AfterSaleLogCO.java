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

package com.taotao.cloud.order.application.command.aftersale.dto.clientobject;

import com.taotao.boot.common.enums.UserEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/** 售后日志 */
@RecordBuilder
@Schema(description = "售后日志VO")
public record AfterSaleLogCO(
        @Schema(description = "id") Long id,

        /** 售后服务单号 */
        @Schema(description = "售后服务单号") String sn,

        /** 操作者id(可以是卖家) */
        @Schema(description = "操作者id(可以是卖家)") String operatorId,

        /**
         * 操作者类型
         *
         * @see UserEnum
         */
        @Schema(description = "操作者类型") String operatorType,

        /** 操作者名称 */
        @Schema(description = "操作者名称") String operatorName,

        /** 日志信息 */
        @Schema(description = "日志信息") String message)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;
}
