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

package com.taotao.cloud.order.biz.model.pojo;

import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.sys.model.vo.order.OrderSimpleVO;
import com.taotao.cloud.order.biz.model.entity.order.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 订单可进行的操作
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Schema(description = "订单可进行的操作")
public record AllowOperation(
        @Schema(description = "可以取消") Boolean cancel,
        @Schema(description = "可以支付") Boolean pay,
        @Schema(description = "可以发货") Boolean ship,
        @Schema(description = "可以收货") Boolean rog,
        @Schema(description = "是否允许查看物流信息") Boolean showLogistics,
        @Schema(description = "是否允许更改收货人信息") Boolean editConsignee,
        @Schema(description = "是否允许更改价格") Boolean editPrice,
        @Schema(description = "是否可以进行核销") Boolean take)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -5109440403955543227L;

    /**
     * 根据各种状态构建对象
     *
     * @param order
     */
    public AllowOperation(Order order) {

        // 获取订单类型
        String status = order.getOrderStatus();
        String payStatus = order.getPayStatus();
        // 编辑订单价格 未付款并且是新订单
        if (payStatus.equals(PayStatusEnum.UNPAID.name()) && status.equals(OrderStatusEnum.UNPAID.name())) {
            this.editPrice = true;
        }

        // 新订单
        if (CharSequenceUtil.equalsAny(
                status,
                OrderStatusEnum.UNPAID.name(),
                OrderStatusEnum.PAID.name(),
                OrderStatusEnum.UNDELIVERED.name())) {
            this.cancel = true;
        }
        // 新订单，允许支付
        this.pay = status.equals(OrderStatusEnum.UNPAID.name()) && payStatus.equals(PayStatusEnum.UNPAID.name());

        // 可编辑订单收件人信息=实物订单 && 订单未发货 && 订单未取消
        this.editConsignee = order.getOrderType().equals(OrderTypeEnum.NORMAL.name())
                && order.getDeliverStatus().equals(DeliverStatusEnum.UNDELIVERED.name())
                && !status.equals(OrderStatusEnum.CANCELLED.name());

        // 是否允许被发货
        this.ship = editConsignee && status.equals(OrderStatusEnum.UNDELIVERED.name());

        // 是否允许被收货
        this.rog = status.equals(OrderStatusEnum.DELIVERED.name());

        // 是否允许查看物流信息
        this.showLogistics = order.getDeliverStatus().equals(DeliverStatusEnum.DELIVERED.name())
                && status.equals(OrderStatusEnum.DELIVERED.name());

        this.take = order.getOrderType().equals(OrderTypeEnum.VIRTUAL.name())
                && order.getOrderStatus().equals(OrderStatusEnum.TAKE.name());
    }

    /**
     * 根据各种状态构建对象
     *
     * @param order
     */
    public AllowOperation(OrderSimpleVO order) {
        // 获取订单类型
        String status = order.getOrderStatus();
        String payStatus = order.getPayStatus();
        // 编辑订单价格 未付款并且是新订单
        if (payStatus.equals(PayStatusEnum.UNPAID.name()) && status.equals(OrderStatusEnum.UNPAID.name())) {
            this.editPrice = true;
        }

        // 新订单
        if (CharSequenceUtil.equalsAny(
                status,
                OrderStatusEnum.UNPAID.name(),
                OrderStatusEnum.PAID.name(),
                OrderStatusEnum.UNDELIVERED.name())) {
            this.cancel = true;
        }
        // 新订单，允许支付
        this.pay = status.equals(OrderStatusEnum.UNPAID.name());

        // 订单未发货，就可以编辑收货人信息
        this.editConsignee = order.getDeliverStatus().equals(DeliverStatusEnum.UNDELIVERED.name());

        // 是否允许被发货
        this.ship = editConsignee && status.equals(OrderStatusEnum.UNDELIVERED.name());

        // 是否允许被收货
        this.rog = status.equals(OrderStatusEnum.DELIVERED.name());

        // 是否允许查看物流信息
        this.showLogistics = order.getDeliverStatus().equals(DeliverStatusEnum.DELIVERED.name())
                && status.equals(OrderStatusEnum.DELIVERED.name());

        this.take = order.getOrderType().equals(OrderTypeEnum.VIRTUAL.name())
                && order.getOrderStatus().equals(OrderStatusEnum.TAKE.name());
    }
}
