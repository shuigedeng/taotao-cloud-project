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

package com.taotao.cloud.workflow.biz.common.model.form.purchaselist;

import java.math.BigDecimal;
import lombok.Data;

/** 日常物品采购清单 */
@Data
public class PurchaseListEntryEntityInfoModel {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "采购主键")
    private String purchaseId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "规格型号")
    private String specifications;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "数量")
    private String qty;

    @Schema(description = "单价")
    private BigDecimal price;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "排序码")
    private Long sortCode;
}
