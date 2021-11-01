package com.taotao.cloud.order.biz.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.order.api.bo.order_info.OrderBO;
import com.taotao.cloud.order.api.dubbo.IDubboOrderService;
import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.order.biz.entity.OrderItem;
import com.taotao.cloud.order.biz.entity.QOrderInfo;
import com.taotao.cloud.order.biz.mapper.IOrderInfoMapper;
import com.taotao.cloud.order.biz.mapstruct.IOrderMapStruct;
import com.taotao.cloud.order.biz.repository.cls.OrderInfoRepository;
import com.taotao.cloud.order.biz.repository.inf.IOrderInfoRepository;
import com.taotao.cloud.order.biz.service.IOrderInfoService;
import com.taotao.cloud.order.biz.service.IOrderItemService;
import com.taotao.cloud.uc.api.dubbo.IDubboResourceService;
import com.taotao.cloud.uc.api.vo.resource.ResourceQueryBO;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br>
 *
 * @author shuigedeng
 * @version v1.0.0
 * @create 2020/6/10 16:55
 */
@Service
@DubboService(interfaceClass = IDubboOrderService.class)
public class OrderInfoServiceImpl
	extends BaseSuperServiceImpl<IOrderInfoMapper, OrderInfo, OrderInfoRepository, IOrderInfoRepository, Long>
	implements IDubboOrderService, IOrderInfoService<OrderInfo, Long> {

	private final static QOrderInfo ORDER_INFO = QOrderInfo.orderInfo;

	@Autowired
	private IOrderItemService<OrderItem, Long> orderItemService;

	@DubboReference
	private IDubboResourceService dubboResourceService;

	@Override
	public Boolean existByCode(String code) {
		BooleanExpression predicate = ORDER_INFO.code.eq(code);
		OrderInfo orderInfoById1 = cr().findOrderInfoById(2L);
		return cr().exists(predicate);
	}

	@Override
	public List<OrderBO> queryRegionByParentId(Long parentId) {

		OrderInfo orderInfoById1 = cr().findOrderInfoById(2L);

		orderItemService.getById(2L);

		ResourceQueryBO allById = dubboResourceService.queryAllId(1L);
		LogUtil.info(allById.toString());

		return new ArrayList<>();
	}

	@Override
	public OrderBO query(Long id) {
		OrderInfo orderInfo = getById(id);
		return IOrderMapStruct.INSTANCE.entityToBo(orderInfo);
	}
}
