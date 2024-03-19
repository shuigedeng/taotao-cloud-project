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

package com.taotao.cloud.order.biz.model.dto.order_item;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单项查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:32:51
 */
@Schema(description = "订单项查询对象")
public record OrderItemQueryDTO(
        /** 订单子编码 */
        @Schema(description = "订单子编码") String itemCode,
        /** 商品SPU ID */
        @Schema(description = "商品SPU ID") Long productSpuId,
        /** 商品SPU_CODE */
        @Schema(description = "商品SPU_CODE") String productSpuCode,
        /** 商品SPU名称 */
        @Schema(description = "商品SPU名称") String productSpuName,
        /** 商品SKU ID */
        @Schema(description = "商品SKU ID") Long productSkuId,
        /** 商品SKU 规格名称 */
        @Schema(description = "商品SKU 规格名称") String productSkuName,
        /** 商品单价 */
        @Schema(description = "商品单价") BigDecimal productPrice,
        /** 购买数量 */
        @Schema(description = "购买数量") Integer num,
        /** 合计金额 */
        @Schema(description = "合计金额") BigDecimal sumAmount,
        /** 商品主图 */
        @Schema(description = "商品主图") String productPicUrl,
        /** 供应商id */
        @Schema(description = "供应商id") Long supplierId,
        /** 供应商名称 */
        @Schema(description = "供应商名称") String supplierName,
        /** 超时退货期限 */
        @Schema(description = "超时退货期限") Integer refundTime,
        /** 退货数量 */
        @Schema(description = "退货数量") Integer rejectCount,
        /** 商品类型 0 普通商品 1 秒杀商品 */
        @Schema(description = "商品类型 0 普通商品 1 秒杀商品") Integer type)
        implements Serializable {

    static final long serialVersionUID = 5126530068827085130L;

    public OrderItemQueryDTO {
        rejectCount = 0;
        productPrice = BigDecimal.ZERO;
        sumAmount = BigDecimal.ZERO;
        type = 0;
    }
}
