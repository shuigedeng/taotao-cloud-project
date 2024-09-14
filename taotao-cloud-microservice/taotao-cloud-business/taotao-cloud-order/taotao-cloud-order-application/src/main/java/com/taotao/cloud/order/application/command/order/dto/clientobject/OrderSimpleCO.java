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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.boot.common.enums.ClientTypeEnum;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

/** 订单简略信息 用于订单列表查看 */
@RecordBuilder
@Schema(description = "订单简略信息 用于订单列表查看")
public record OrderSimpleCO(
        @Schema(description = "sn") String sn,
        @Schema(description = "总价格") BigDecimal flowPrice,
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                @Schema(description = "创建时间")
                LocalDateTime createTime,

        /**
         * @see OrderStatusEnum
         */
        @Schema(description = "订单状态") String orderStatus,

        /**
         * @see PayStatusEnum
         */
        @Schema(description = "付款状态") String payStatus,
        @Schema(description = "支付方式") String paymentMethod,
        @Schema(description = "支付时间") @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime paymentTime,
        @Schema(description = "用户名") String memberName,
        @Schema(description = "店铺名称") String storeName,
        @Schema(description = "店铺ID") Long storeId,

        /**
         * @see ClientTypeEnum
         */
        @Schema(description = "订单来源") String clientType,

        /** 子订单信息 */
        List<OrderItemCO> orderItems,
        @Schema(hidden = true, description = "item goods_id") String groupGoodsId,
        @Schema(hidden = true, description = "item sku id") String groupSkuId,
        @Schema(hidden = true, description = "item 数量") String groupNum,
        @Schema(hidden = true, description = "item 图片") String groupImages,
        @Schema(hidden = true, description = "item 名字") String groupName,
        @Schema(hidden = true, description = "item 编号") String groupOrderItemsSn,
        @Schema(hidden = true, description = "item 商品价格") String groupGoodsPrice,

        /**
         * @see OrderItemAfterSaleStatusEnum
         */
        @Schema(
                        hidden = true,
                        description = "item 售后状态",
                        allowableValues = "NOT_APPLIED(未申请),ALREADY_APPLIED(已申请),EXPIRED(已失效不允许申请售后)")
                String groupAfterSaleStatus,

        /**
         * @see OrderComplaintStatusEnum
         */
        @Schema(hidden = true, description = "item 投诉状态") String groupComplainStatus,

        /**
         * @see CommentStatusEnum
         */
        @Schema(hidden = true, description = "item 评价状态") String groupCommentStatus,

        /**
         * @see OrderTypeEnum
         */
        @Schema(description = "订单类型") String orderType,

        /**
         * @see DeliverStatusEnum
         */
        @Schema(description = "货运状态") String deliverStatus)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -6293102172184734928L;

    // public List<OrderItemVO> getOrderItems() {
    // 	if (StringUtil.isEmpty(groupGoodsId)) {
    // 		return new ArrayList<>();
    // 	}
    // 	List<OrderItemVO> orderItemVOS = new ArrayList<>();
    // 	String[] orderItemsSn = groupOrderItemsSn.split(",");
    // 	String[] goodsId = groupGoodsId.split(",");
    // 	String[] skuId = groupSkuId.split(",");
    // 	String[] num = groupNum.split(",");
    // 	String[] image = groupImages.split(",");
    // 	String[] name = groupName.split(",");
    // 	String[] afterSaleStatus = groupAfterSaleStatus.split(",");
    // 	String[] complainStatus = groupComplainStatus.split(",");
    // 	String[] commentStatus = groupCommentStatus.split(",");
    // 	String[] goodsPrice = groupGoodsPrice.split(",");
    //
    // 	for (int i = 0; i < goodsId.length; i++) {
    // 		orderItemVOS.add(
    // 			new OrderItemVO(orderItemsSn[i], goodsId[i], skuId[i], num[i], image[i], name[i],
    // 				afterSaleStatus[i], complainStatus[i], commentStatus[i],
    // 				BigDecimal.parseBigDecimal()));
    // 	}
    // 	return orderItemVOS;
    //
    // }
    //
    // /**
    //  * 初始化自身状态
    //  */
    // public AllowOperation getAllowOperationVO() {
    // 	//设置订单的可操作状态
    // 	return new AllowOperation(this);
    // }

}
