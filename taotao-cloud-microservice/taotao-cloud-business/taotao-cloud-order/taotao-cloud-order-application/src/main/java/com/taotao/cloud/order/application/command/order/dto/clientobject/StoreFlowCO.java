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

import com.taotao.cloud.order.api.enums.order.FlowTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import java.io.Serial;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 商家订单流水表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:02:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreFlowCO {

    @Serial
    private static final long serialVersionUID = -5998757398902747939L;
    /** 流水编号 */
    private String sn;
    /** 订单sn */
    private String orderSn;
    /** 子订单sn */
    private String orderItemSn;
    /** 售后SN */
    private String refundSn;
    /** 店铺id */
    private Long storeId;
    /** 店铺名称 */
    private String storeName;
    /** 会员id */
    private Long memberId;
    /** 会员名称 */
    private String memberName;
    /** 商品ID */
    private Long goodsId;
    /** 商品名称 */
    private String goodsName;
    /** 货品ID */
    private Long skuId;
    /** 图片 */
    private String image;
    /** 分类ID */
    private Long categoryId;
    /** 规格json */
    private String specs;
    /**
     * 流水类型：PAY/REFUND 支付/退款
     *
     * @see FlowTypeEnum
     */
    private String flowType;

    /**
     * 订单促销类型
     *
     * @see OrderPromotionTypeEnum
     */
    private String orderPromotionType;
    /** 积分活动商品结算价格 */
    private BigDecimal pointSettlementPrice;
    /** 砍价活动商品结算价格 */
    private BigDecimal kanjiaSettlementPrice;
    /** 平台优惠券 使用金额 */
    private BigDecimal siteCouponPrice;
    /** 站点优惠券佣金比例 */
    private BigDecimal siteCouponPoint;
    /** 站点优惠券佣金 */
    private BigDecimal siteCouponCommission;
    /** 单品分销返现支出 */
    private BigDecimal distributionRebate;
    /** 平台收取交易佣金 */
    private BigDecimal commissionPrice;
    /** 流水金额 */
    private BigDecimal finalPrice;
    /** 最终结算金额 */
    private BigDecimal billPrice;
    /** 第三方交易流水号 */
    private String transactionId;
    /** 支付方式名称 */
    private String paymentName;
    /** 销售量 */
    private Integer num;
}
