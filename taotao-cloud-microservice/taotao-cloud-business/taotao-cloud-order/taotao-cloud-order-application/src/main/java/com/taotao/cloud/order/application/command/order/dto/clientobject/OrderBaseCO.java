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

import com.taotao.boot.common.enums.ClientTypeEnum;
import com.taotao.cloud.order.api.enums.cart.DeliveryMethodEnum;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:19
 */
@RecordBuilder
public record OrderBaseCO(
        /** 订单编号 */
        String sn,

        /** 交易编号 关联Trade */
        String tradeSn,

        /** 店铺ID */
        Long storeId,

        /** 店铺名称 */
        String storeName,

        /** 会员ID */
        Long memberId,

        /** 用户名 */
        String memberName,

        /**
         * 订单状态
         *
         * @see OrderStatusEnum
         */
        String orderStatus,

        /**
         * 付款状态
         *
         * @see PayStatusEnum
         */
        String payStatus,

        /**
         * 货运状态
         *
         * @see DeliverStatusEnum
         */
        String deliverStatus,

        /** 第三方付款流水号 */
        String receivableNo,

        /** 支付方式 */
        String paymentMethod,

        /** 支付时间 */
        LocalDateTime paymentTime,

        /** 收件人姓名 */
        String consigneeName,

        /** 收件人手机 */
        String consigneeMobile,

        /**
         * 配送方式
         *
         * @see DeliveryMethodEnum
         */
        String deliveryMethod,

        /** 地址名称， ','分割 */
        String consigneeAddressPath,

        /** 地址id，','分割 */
        String consigneeAddressIdPath,

        /** 详细地址 */
        String consigneeDetail,

        /** 总价格 */
        BigDecimal flowPrice,

        /** 商品价格 */
        BigDecimal goodsPrice,

        /** 运费 */
        BigDecimal freightPrice,

        /** 优惠的金额 */
        BigDecimal discountPrice,

        /** 修改价格 */
        BigDecimal updatePrice,

        /** 发货单号 */
        String logisticsNo,

        /** 物流公司CODE */
        String logisticsCode,

        /** 物流公司名称 */
        String logisticsName,

        /** 订单商品总重量 */
        BigDecimal weight,

        /** 商品数量 */
        Integer goodsNum,

        /** 买家订单备注 */
        String remark,

        /** 订单取消原因 */
        String cancelReason,

        /** 完成时间 */
        LocalDateTime completeTime,

        /** 送货时间 */
        LocalDateTime logisticsTime,

        /** 支付方式返回的交易号 */
        String payOrderNo,

        /**
         * 订单来源
         *
         * @see ClientTypeEnum
         */
        String clientType,

        /** 是否需要发票 */
        Boolean needReceipt,

        /** 是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空 */
        String parentOrderSn,

        /** 是否为某订单类型的订单，如果是则为订单类型的id，否则为空 */
        String promotionId,

        /**
         * 订单类型
         *
         * @see OrderTypeEnum
         */
        String orderType,

        /**
         * 订单促销类型
         *
         * @see OrderPromotionTypeEnum
         */
        String orderPromotionType,

        /** 价格详情 */
        String priceDetail,

        /** 订单是否支持原路退回 */
        Boolean canReturn,

        /** 提货码 */
        String verificationCode,

        /** 分销员ID */
        Long distributionId,

        /** 使用的店铺会员优惠券id(,区分) */
        String useStoreMemberCouponIds,

        /** 使用的平台会员优惠券id */
        String usePlatformMemberCouponId)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;
}
