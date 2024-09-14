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

package com.taotao.cloud.order.application.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.order.application.service.order.IOrderItemService;
import com.taotao.cloud.order.application.service.order.IOrderService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 每天订单任务执行
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:48:39
 */
@Component
public class OrderEveryDayTaskExecute implements EveryDayExecute {

    /** 订单 */
    @Autowired
    private IOrderService orderService;
    /** 订单货物 */
    @Autowired
    private IOrderItemService orderItemService;
    /** 设置 */
    @Autowired
    private IFeignSettingApi settingApi;
    /** 会员评价 */
    @Autowired
    private IFeignMemberEvaluationApi memberEvaluationApi;

    @Autowired
    private IAfterSaleService afterSaleService;

    /** 执行每日任务 */
    @Override
    public void execute() {
        OrderSettingVO orderSetting = settingApi.getOrderSetting(SettingCategoryEnum.ORDER_SETTING.name());
        // 订单设置
        if (orderSetting == null) {
            throw new BusinessException(ResultEnum.ORDER_SETTING_ERROR);
        }

        // 自动确认收货
        completedOrder(orderSetting);
        // 自动好评
        memberEvaluation(orderSetting);
        // 关闭允许售后申请
        closeAfterSale(orderSetting);
        // 关闭允许投诉
        closeComplaint(orderSetting);
    }

    /**
     * 自动确认收获，订单完成
     *
     * @param orderSetting 订单设置
     */
    private void completedOrder(OrderSettingVO orderSetting) {
        // 订单自动收货时间 = 当前时间 - 自动收货时间天数
        DateTime receiveTime = DateUtil.offsetDay(DateUtil.date(), -orderSetting.getAutoReceive());
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderStatus, OrderStatusEnum.DELIVERED.name());

        // 订单发货时间 >= 订单自动收货时间
        queryWrapper.le(Order::getLogisticsTime, receiveTime);
        List<Order> list = orderService.list(queryWrapper);

        // 判断是否有符合条件的订单，进行订单完成处理
        if (!list.isEmpty()) {
            List<String> receiveSnList = list.stream().map(Order::getSn).toList();
            for (String orderSn : receiveSnList) {
                orderService.systemComplete(orderSn);
            }
        }
    }

    /**
     * 自动好评
     *
     * @param orderSetting 订单设置
     */
    private void memberEvaluation(OrderSettingVO orderSetting) {
        // 订单自动收货时间 = 当前时间 - 自动收货时间天数
        DateTime receiveTime = DateUtil.offsetDay(DateUtil.date(), -orderSetting.getAutoEvaluation());

        // 订单完成时间 <= 订单自动好评时间
        QueryWrapper<OrderSimpleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("o.complete_time", receiveTime);
        queryWrapper.eq("oi.comment_status", CommentStatusEnum.UNFINISHED.name());
        List<OrderItem> orderItems = orderItemMapper.waitOperationOrderItem(queryWrapper);

        // 判断是否有符合条件的订单，进行自动评价处理
        if (!orderItems.isEmpty()) {
            for (OrderItem orderItem : orderItems) {
                MemberEvaluationDTO memberEvaluationDTO = new MemberEvaluationDTO();
                memberEvaluationDTO.setOrderItemSn(orderItem.getSn());
                memberEvaluationDTO.setContent("系统默认好评");
                memberEvaluationDTO.setGoodsId(orderItem.getGoodsId());
                memberEvaluationDTO.setSkuId(orderItem.getSkuId());
                memberEvaluationDTO.setGrade(EvaluationGradeEnum.GOOD.name());
                memberEvaluationDTO.setDeliveryScore(5);
                memberEvaluationDTO.setDescriptionScore(5);
                memberEvaluationDTO.setServiceScore(5);

                memberEvaluationApi.addMemberEvaluation(memberEvaluationDTO, false);
            }
        }
    }

    /**
     * 关闭允许售后申请
     *
     * @param orderSetting 订单设置
     */
    private void closeAfterSale(OrderSettingVO orderSetting) {
        // 订单关闭售后申请时间 = 当前时间 - 自动关闭售后申请天数
        DateTime receiveTime = DateUtil.offsetDay(DateUtil.date(), -orderSetting.getAutoEvaluation());

        // 关闭售后订单=未售后订单+小于订单关闭售后申请时间
        QueryWrapper<OrderSimpleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("o.complete_time", receiveTime);
        queryWrapper.eq("oi.after_sale_status", OrderItemAfterSaleStatusEnum.NOT_APPLIED.name());
        List<OrderItem> orderItems = orderItemMapper.waitOperationOrderItem(queryWrapper);

        // 判断是否有符合条件的订单，关闭允许售后申请处理
        if (!orderItems.isEmpty()) {
            // 获取订单货物ID
            List<Long> orderItemIdList =
                    orderItems.stream().map(OrderItem::getId).toList();

            // 修改订单售后状态
            LambdaUpdateWrapper<OrderItem> lambdaUpdateWrapper = new LambdaUpdateWrapper<OrderItem>()
                    .set(OrderItem::getAfterSaleStatus, OrderItemAfterSaleStatusEnum.EXPIRED.name())
                    .in(OrderItem::getId, orderItemIdList);
            orderItemService.update(lambdaUpdateWrapper);
        }
    }

    /**
     * 关闭允许交易投诉
     *
     * @param orderSetting 订单设置
     */
    private void closeComplaint(OrderSettingVO orderSetting) {
        // 订单关闭交易投诉申请时间 = 当前时间 - 自动关闭交易投诉申请天数
        DateTime receiveTime = DateUtil.offsetDay(DateUtil.date(), -orderSetting.getCloseComplaint());

        // 关闭售后订单=未售后订单+小于订单关闭售后申请时间
        QueryWrapper<OrderSimpleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("o.complete_time", receiveTime);
        queryWrapper.eq("oi.complain_status", OrderComplaintStatusEnum.NO_APPLY.name());
        List<OrderItem> orderItems = orderItemMapper.waitOperationOrderItem(queryWrapper);

        // 判断是否有符合条件的订单，关闭允许售后申请处理
        if (!orderItems.isEmpty()) {
            // 获取订单货物ID
            List<Long> orderItemIdList =
                    orderItems.stream().map(OrderItem::getId).toList();

            // 修改订单投诉状态
            LambdaUpdateWrapper<OrderItem> lambdaUpdateWrapper = new LambdaUpdateWrapper<OrderItem>()
                    .set(OrderItem::getComplainStatus, OrderItemAfterSaleStatusEnum.EXPIRED.name())
                    .in(OrderItem::getId, orderItemIdList);
            orderItemService.update(lambdaUpdateWrapper);
        }
    }
}
