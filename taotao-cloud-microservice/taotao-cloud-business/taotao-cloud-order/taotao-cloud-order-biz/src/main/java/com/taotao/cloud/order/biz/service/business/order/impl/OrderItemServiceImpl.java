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

package com.taotao.cloud.order.biz.service.business.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.order.biz.mapper.order.OrderItemMapper;
import com.taotao.cloud.order.biz.model.entity.order.OrderItem;
import com.taotao.cloud.order.biz.service.business.order.OrderItemService;
import java.util.List;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 子订单业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:07
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Override
    public Boolean updateCommentStatus(String orderItemSn, CommentStatusEnum commentStatusEnum) {
        LambdaUpdateWrapper<OrderItem> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(OrderItem::getCommentStatus, commentStatusEnum.name());
        lambdaUpdateWrapper.eq(OrderItem::getSn, orderItemSn);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public Boolean updateAfterSaleStatus(
            String orderItemSn, OrderItemAfterSaleStatusEnum orderItemAfterSaleStatusEnum) {
        LambdaUpdateWrapper<OrderItem> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(OrderItem::getAfterSaleStatus, orderItemAfterSaleStatusEnum.name());
        lambdaUpdateWrapper.eq(OrderItem::getSn, orderItemSn);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public Boolean updateOrderItemsComplainStatus(
            String orderSn, Long skuId, Long complainId, OrderComplaintStatusEnum complainStatusEnum) {
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItem::getOrderSn, orderSn).eq(OrderItem::getSkuId, skuId);
        OrderItem orderItem = getOne(queryWrapper);
        if (orderItem == null) {
            throw new BusinessException(ResultEnum.ORDER_ITEM_NOT_EXIST);
        }
        orderItem.setComplainId(complainId);
        orderItem.setComplainStatus(complainStatusEnum.name());
        return updateById(orderItem);
    }

    @Override
    public OrderItem getBySn(String sn) {
        LambdaQueryWrapper<OrderItem> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(OrderItem::getSn, sn);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<OrderItem> getByOrderSn(String orderSn) {
        LambdaQueryWrapper<OrderItem> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(OrderItem::getOrderSn, orderSn);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public OrderItem getByOrderSnAndSkuId(String orderSn, Long skuId) {
        return this.getOne(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderSn, orderSn)
                .eq(OrderItem::getSkuId, skuId));
    }
}
