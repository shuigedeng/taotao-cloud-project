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

package com.taotao.cloud.order.application.command.order.dto.clientobject;

import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/** 子订单VO */
@RecordBuilder
@Schema(description = "子订单VO")
public record OrderItemCO(

        /** 订单编号 */
        String orderSn,

        /** 子订单编号 */
        String sn,

        /** 单价 */
        BigDecimal unitPrice,

        /** 小记 */
        BigDecimal subTotal,

        /** 商品ID */
        Long goodsId,

        /** 货品ID */
        Long skuId,

        /** 销售量 */
        Integer num,

        /** 交易编号 */
        String tradeSn,

        /** 图片 */
        String image,

        /** 商品名称 */
        String goodsName,

        /** 分类ID */
        Long categoryId,

        /** 快照id */
        Long snapshotId,

        /** 规格json */
        String specs,

        /** 促销类型 */
        String promotionType,

        /** 促销id */
        Long promotionId,

        /** 销售金额 */
        BigDecimal goodsPrice,

        /** 实际金额 */
        BigDecimal flowPrice,

        /**
         * 评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，
         *
         * @see CommentStatusEnum
         */
        String commentStatus,

        /**
         * 售后状态
         *
         * @see OrderItemAfterSaleStatusEnum
         */
        String afterSaleStatus,

        /** 价格详情 */
        String priceDetail,

        /**
         * 投诉状态
         *
         * @see OrderComplaintStatusEnum
         */
        String complainStatus,

        /** 交易投诉id */
        Long complainId,

        /** 退货商品数量 */
        Integer returnGoodsNumber)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -6293102172184734928L;
}
