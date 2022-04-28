package com.taotao.cloud.order.biz.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.order.biz.entity.order.OrderItem;
import com.taotao.cloud.order.biz.mapper.order.IOrderItemMapper;
import com.taotao.cloud.order.biz.service.order.IOrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class OrderItemServiceImpl extends ServiceImpl<IOrderItemMapper, OrderItem> implements
	IOrderItemService {

    @Override
    public void updateCommentStatus(String orderItemSn, CommentStatusEnum commentStatusEnum) {
        LambdaUpdateWrapper<OrderItem> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(OrderItem::getCommentStatus, commentStatusEnum.name());
        lambdaUpdateWrapper.eq(OrderItem::getSn, orderItemSn);
        this.update(lambdaUpdateWrapper);
    }

    @Override
    public void updateAfterSaleStatus(String orderItemSn, OrderItemAfterSaleStatusEnum orderItemAfterSaleStatusEnum) {
        LambdaUpdateWrapper<OrderItem> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(OrderItem::getAfterSaleStatus, orderItemAfterSaleStatusEnum.name());
        lambdaUpdateWrapper.eq(OrderItem::getSn, orderItemSn);
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 更新订单可投诉状态
     *
     * @param orderSn            订单sn
     * @param skuId              商品skuId
     * @param complainId         订单交易投诉ID
     * @param complainStatusEnum 修改状态
     */
    @Override
    public void updateOrderItemsComplainStatus(String orderSn, String skuId, String complainId, OrderComplaintStatusEnum complainStatusEnum) {
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItem::getOrderSn, orderSn).eq(OrderItem::getSkuId, skuId);
        OrderItem orderItem = getOne(queryWrapper);
        if (orderItem == null) {
            throw new BusinessException(ResultEnum.ORDER_ITEM_NOT_EXIST);
        }
        orderItem.setComplainId(complainId);
        orderItem.setComplainStatus(complainStatusEnum.name());
        updateById(orderItem);
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
    public OrderItem getByOrderSnAndSkuId(String orderSn, String skuId) {
        return this.getOne(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderSn, orderSn)
                .eq(OrderItem::getSkuId, skuId));
    }
}
