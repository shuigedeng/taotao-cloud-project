/**
 * Project Name: my-projects Package Name: com.taotao.cloud.order.biz.service.impl Date: 2020/6/10
 * 16:55 Author: shuigedeng
 */
package com.taotao.cloud.order.biz.service.impl;

import com.taotao.cloud.common.utils.BeanUtil;
import com.taotao.cloud.order.api.dto.OrderItemDTO;
import com.taotao.cloud.order.biz.entity.OrderItem;
import com.taotao.cloud.order.biz.repository.OrderItemRepository;
import com.taotao.cloud.order.biz.service.IOrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 *
 * @author shuigedeng
 * @version v1.0.0
 * @create 2020/6/10 16:55
 */
@Service
public class OrderItemServiceImpl implements IOrderItemService {

	private final OrderItemRepository orderItemRepository;

	public OrderItemServiceImpl(
		OrderItemRepository orderItemRepository) {
		this.orderItemRepository = orderItemRepository;
	}


	@Override
	public OrderItem findOrderItemByCode(String code) {
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OrderItem saveOrderItem(OrderItemDTO orderItemDTO) {
		OrderItem orderItem = new OrderItem();
		BeanUtil.copyIgnoredNull(orderItemDTO, orderItem);
		orderItemRepository.save(orderItem);

		return orderItem;
	}
}
