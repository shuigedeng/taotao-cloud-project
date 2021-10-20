/**
 * Project Name: my-projects Package Name: com.taotao.cloud.order.biz.service.impl Date: 2020/6/10
 * 16:55 Author: shuigedeng
 */
package com.taotao.cloud.order.biz.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.order.api.service.IOrderInfoService;
import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.order.biz.entity.QOrderInfo;
import com.taotao.cloud.order.biz.mapper.OrderInfoMapper;
import com.taotao.cloud.order.biz.repository.IOrderInfoRepository;
import com.taotao.cloud.order.biz.repository.impl.OrderInfoRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * <br>
 *
 * @author shuigedeng
 * @version v1.0.0
 * @create 2020/6/10 16:55
 */
@Service
@DubboService
public class OrderInfoServiceImpl
	extends
	BaseSuperServiceImpl<OrderInfoMapper, OrderInfo, OrderInfoRepository, IOrderInfoRepository, Long>
	implements IOrderInfoService<OrderInfo, Long> {

	private final static QOrderInfo ORDER_INFO = QOrderInfo.orderInfo;

	@Override
	public Boolean existByCode(String code) {
		BooleanExpression predicate = ORDER_INFO.code.eq(code);
		List<OrderInfo> orderInfoById = ir().findOrderInfoById(1L);
		OrderInfo byCode = ir().findByCode("1");
		List<OrderInfo> orderInfoById1 = cr().findOrderInfoById(1L);
		OrderInfo userById = im().getUserById(1L);
		return cr().exists(predicate);
	}
}
