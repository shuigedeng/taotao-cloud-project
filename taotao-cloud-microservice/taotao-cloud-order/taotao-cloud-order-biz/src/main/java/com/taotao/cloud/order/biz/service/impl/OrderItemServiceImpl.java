/**
 * Project Name: my-projects Package Name: com.taotao.cloud.order.biz.service.impl Date: 2020/6/10
 * 16:55 Author: shuigedeng
 */
package com.taotao.cloud.order.biz.service.impl;

import com.taotao.cloud.order.api.service.IOrderItemService;
import com.taotao.cloud.order.biz.entity.OrderItem;
import com.taotao.cloud.order.biz.entity.QOrderItem;
import com.taotao.cloud.order.biz.mapper.OrderItemMapper;
import com.taotao.cloud.order.biz.repository.IOrderItemRepository;
import com.taotao.cloud.order.biz.repository.OrderItemRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * OrderItemServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-13 21:42:00
 */
@Service
public class OrderItemServiceImpl extends
	BaseSuperServiceImpl<OrderItemMapper, OrderItem, OrderItemRepository, IOrderItemRepository, Long>
	implements IOrderItemService<OrderItem, Long> {

	private final static QOrderItem ORDER_ITEM = QOrderItem.orderItem;
}
