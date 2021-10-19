/**
 * Project Name: my-projects Package Name: com.taotao.cloud.order.biz.service.impl Date: 2020/6/10
 * 16:55 Author: shuigedeng
 */
package com.taotao.cloud.order.biz.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.order.api.OrderBO;
import com.taotao.cloud.order.api.OrderDO;
import com.taotao.cloud.order.api.OrderQueryBO;
import com.taotao.cloud.order.api.service.IOrderInfoService;
import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.order.biz.entity.QOrderInfo;
import com.taotao.cloud.order.biz.mapper.OrderInfoMapper;
import com.taotao.cloud.order.biz.mapstruct.OrderMapStruct;
import com.taotao.cloud.order.biz.repository.IOrderInfoRepository;
import com.taotao.cloud.order.biz.repository.OrderInfoRepository;
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

	@Override
	public List<OrderQueryBO> findOrderInfoByBo(
		OrderBO orderBO) {
		String code = orderBO.code();
		List<OrderDO> dos = im().findOrderInfoByBo(code);
		List<OrderDO> dos1 = ir().findOrderInfoByBo(code);
		//List<OrderDO> dos2 = cr().findOrderInfoByBo(code);
		//LogUtil.info(dos.toString());
		LogUtil.info(dos1.toString());
		//LogUtil.info(dos2.toString());
		List<OrderQueryBO> orderBOS = OrderMapStruct.INSTANCE.dosToBos(dos1);
		return orderBOS;
	}
}
