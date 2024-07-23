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

import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import com.taotao.cloud.order.application.command.aftersale.dto.AfterSaleAllowOperation;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 售后 */
@RecordBuilder
@Schema(description = "售后VO")
public record AfterSaleCO(
        @Schema(description = "id") Long id,
        @Schema(description = "售后服务单号") String sn,
        @Schema(description = "订单编号") String orderSn,
        @Schema(description = "订单货物编号") String orderItemSn,
        @Schema(description = "交易编号") String tradeSn,
        @Schema(description = "会员ID") String memberId,
        @Schema(description = "会员名称") String memberName,
        @Schema(description = "商家ID") String storeId,
        @Schema(description = "商家名称") String storeName,

        // **********商品信息*************
        @Schema(description = "商品ID") String goodsId,
        @Schema(description = "货品ID") String skuId,
        @Schema(description = "申请数量") Integer num,
        @Schema(description = "商品图片") String goodsImage,
        @Schema(description = "商品名称") String goodsName,
        @Schema(description = "规格json") String specs,
        @Schema(description = "实际金额") BigDecimal flowPrice,

        // 交涉信息
        @Schema(description = "申请原因") String reason,
        @Schema(description = "问题描述") String problemDesc,
        @Schema(description = "评价图片") String afterSaleImage,

        /**
         * @see AfterSaleTypeEnum
         */
        @Schema(description = "售后类型", allowableValues = "RETURN_GOODS,RETURN_MONEY") String serviceType,

        /**
         * @see AfterSaleStatusEnum
         */
        @Schema(
                        description = "售后单状态",
                        allowableValues =
                                "APPLY,PASS,REFUSE,BUYER_RETURN,SELLER_RE_DELIVERY,BUYER_CONFIRM,SELLER_CONFIRM,COMPLETE")
                String serviceStatus,

        // **********退款信息*************
        /**
         * @see AfterSaleRefundWayEnum
         */
        @Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE") String refundWay,
        @Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER") String accountType,
        @Schema(description = "银行账户") String bankAccountNumber,
        @Schema(description = "银行开户名") String bankAccountName,
        @Schema(description = "银行开户行") String bankDepositName,
        @Schema(description = "商家备注") String auditRemark,
        @Schema(description = "订单支付方式返回的交易号") String payOrderNo,
        @Schema(description = "申请退款金额") BigDecimal applyRefundPrice,
        @Schema(description = "实际退款金额") BigDecimal actualRefundPrice,
        @Schema(description = "退还积分") Integer refundPoint,
        @Schema(description = "退款时间") LocalDateTime refundTime,

        // **********买家物流信息*************
        @Schema(description = "发货单号") String mLogisticsNo,
        @Schema(description = "物流公司CODE") String mLogisticsCode,
        @Schema(description = "物流公司名称") String mLogisticsName,
        @Schema(description = "买家发货时间") LocalDateTime mDeliverTime)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;

    /** 初始化自身状态 */
    public AfterSaleAllowOperation getAfterSaleAllowOperationVO() {
        // 设置订单的可操作状态
        return new AfterSaleAllowOperation(this);
    }
}
