package com.taotao.cloud.order.biz.service.impl;

import com.taotao.cloud.order.api.bo.order_info.OrderBO;
import com.taotao.cloud.order.api.dubbo.IDubboOrderItemService;
import com.taotao.cloud.order.biz.entity.order.OrderItemBack;
import com.taotao.cloud.order.biz.entity.QOrderItem;
import com.taotao.cloud.order.biz.mapper.IOrderItemMapper;
import com.taotao.cloud.order.biz.repository.cls.OrderItemRepository;
import com.taotao.cloud.order.biz.repository.inf.IOrderItemRepository;
import com.taotao.cloud.order.biz.service.IOrderItemService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * OrderItemServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-13 21:42:00
 */
@Service
@DubboService(interfaceClass = IDubboOrderItemService.class)
public class OrderItemServiceImpl
	extends BaseSuperServiceImpl<IOrderItemMapper, OrderItemBack, OrderItemRepository, IOrderItemRepository, Long>
	implements IDubboOrderItemService, IOrderItemService<OrderItemBack, Long> {

	private final static QOrderItem ORDER_ITEM = QOrderItem.orderItem;

	@Override
	public OrderBO query(Long id) {
		return null;
	}
}
