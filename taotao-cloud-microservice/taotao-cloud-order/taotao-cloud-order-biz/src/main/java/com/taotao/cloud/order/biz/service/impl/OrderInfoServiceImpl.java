/**
 * Project Name: my-projects
 * Package Name: com.taotao.cloud.order.biz.service.impl
 * Date: 2020/6/10 16:55
 * Author: dengtao
 */
package com.taotao.cloud.order.biz.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.core.utils.BeanUtil;
import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.biz.entity.Order;
import com.taotao.cloud.order.biz.entity.QOrder;
import com.taotao.cloud.order.biz.repository.OrderInfoRepository;
import com.taotao.cloud.order.biz.service.IOrderInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 *
 * @author dengtao
 * @version v1.0.0
 * @create 2020/6/10 16:55
 */
@Service
@AllArgsConstructor
public class OrderInfoServiceImpl implements IOrderInfoService {

	private final OrderInfoRepository orderInfoRepository;
	private final static QOrder ORDER_INFO = QOrder.order;

	@Override
	public Order findOrderInfoByCode(String code) {
		BooleanExpression expression = ORDER_INFO.delFlag.eq(false).and(ORDER_INFO.code.eq(code));
		return orderInfoRepository.fetchOne(expression);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Order saveOrder(OrderDTO orderDTO) {
		Order order = Order.builder().build();
		BeanUtil.copyIgnoredNull(orderDTO, order);
		return orderInfoRepository.saveAndFlush(order);
	}
}
