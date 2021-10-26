/**
 * Project Name: my-projects Package Name: com.taotao.cloud.order.biz.service.impl Date: 2020/6/10
 * 16:55 Author: shuigedeng
 */
package com.taotao.cloud.order.biz.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.order.api.bo.order_info.OrderBO;
import com.taotao.cloud.order.api.dubbo.IDubboOrderService;
import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.order.biz.entity.QOrderInfo;
import com.taotao.cloud.order.biz.mapper.IOrderInfoMapper;
import com.taotao.cloud.order.biz.mapstruct.IOrderMapStruct;
import com.taotao.cloud.order.biz.repository.cls.OrderInfoRepository;
import com.taotao.cloud.order.biz.repository.inf.IOrderInfoRepository;
import com.taotao.cloud.order.biz.service.IOrderInfoService;
import com.taotao.cloud.uc.api.dubbo.IDubboResourceService;
import com.taotao.cloud.uc.api.vo.resource.ResourceQueryBO;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboReference;
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
	BaseSuperServiceImpl<IOrderInfoMapper, OrderInfo, OrderInfoRepository, IOrderInfoRepository, Long>
	implements IDubboOrderService, IOrderInfoService<OrderInfo, Long> {

	private final static QOrderInfo ORDER_INFO = QOrderInfo.orderInfo;

	@DubboReference
	private IDubboResourceService dubboResourceService;

	@Override
	public Boolean existByCode(String code) {
		BooleanExpression predicate = ORDER_INFO.code.eq(code);
		List<OrderInfo> orderInfoById = ir().findOrderInfoById(1L);
		OrderInfo byCode = ir().findByCode("1");
		List<OrderInfo> orderInfoById1 = cr().findOrderInfoById(1L);
		OrderInfo userById = im().getUserById(1L);
		return cr().exists(predicate);
	}

	@Override
	public List<OrderBO> queryRegionByParentId(Long parentId) {
		OrderInfo orderInfo = getById(parentId);
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
