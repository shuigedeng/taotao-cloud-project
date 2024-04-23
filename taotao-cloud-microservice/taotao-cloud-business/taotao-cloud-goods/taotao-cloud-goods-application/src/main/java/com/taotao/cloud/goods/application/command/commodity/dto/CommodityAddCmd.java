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

package com.taotao.cloud.goods.application.command.commodity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 直播商品DTO 用于获取直播商品状态时使用 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommodityAddCmd {

    @Schema(description = "商品ID")
    private Long goodsId;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "url")
    private String url;

    @Schema(description = "审核状态")
    private Integer auditStatus;
}
