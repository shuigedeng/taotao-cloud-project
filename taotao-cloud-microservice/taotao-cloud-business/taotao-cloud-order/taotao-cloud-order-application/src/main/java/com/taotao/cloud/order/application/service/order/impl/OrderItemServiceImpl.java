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

package com.taotao.cloud.order.application.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.order.application.service.order.IOrderItemService;
import com.taotao.cloud.order.infrastructure.persistent.mapper.order.IOrderItemMapper;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderItemPO;
import java.util.List;
import lombok.AllArgsConstructor;
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
public class OrderItemServiceImpl extends ServiceImpl<IOrderItemMapper, OrderItemPO> implements
	IOrderItemService {

    @Override
    public Boolean updateCommentStatus(String orderItemSn, CommentStatusEnum commentStatusEnum) {
        LambdaUpdateWrapper<OrderItemPO> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(OrderItemPO::getCommentStatus, commentStatusEnum.name());
        lambdaUpdateWrapper.eq(OrderItemPO::getSn, orderItemSn);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public Boolean updateAfterSaleStatus(
            String orderItemSn, OrderItemAfterSaleStatusEnum orderItemAfterSaleStatusEnum) {
        LambdaUpdateWrapper<OrderItemPO> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(OrderItemPO::getAfterSaleStatus, orderItemAfterSaleStatusEnum.name());
        lambdaUpdateWrapper.eq(OrderItemPO::getSn, orderItemSn);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public Boolean updateOrderItemsComplainStatus(
            String orderSn, Long skuId, Long complainId, OrderComplaintStatusEnum complainStatusEnum) {
        LambdaQueryWrapper<OrderItemPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItemPO::getOrderSn, orderSn).eq(OrderItemPO::getSkuId, skuId);
        OrderItemPO orderItemPO = getOne(queryWrapper);
        if (orderItemPO == null) {
            throw new BusinessException(ResultEnum.ORDER_ITEM_NOT_EXIST);
        }
        orderItemPO.setComplainId(complainId);
        orderItemPO.setComplainStatus(complainStatusEnum.name());
        return updateById(orderItemPO);
    }

    @Override
    public OrderItemPO getBySn(String sn) {
        LambdaQueryWrapper<OrderItemPO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(OrderItemPO::getSn, sn);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<OrderItemPO> getByOrderSn(String orderSn) {
        LambdaQueryWrapper<OrderItemPO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(OrderItemPO::getOrderSn, orderSn);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public OrderItemPO getByOrderSnAndSkuId(String orderSn, Long skuId) {
        return this.getOne(new LambdaQueryWrapper<OrderItemPO>()
                .eq(OrderItemPO::getOrderSn, orderSn)
                .eq(OrderItemPO::getSkuId, skuId));
    }
}
