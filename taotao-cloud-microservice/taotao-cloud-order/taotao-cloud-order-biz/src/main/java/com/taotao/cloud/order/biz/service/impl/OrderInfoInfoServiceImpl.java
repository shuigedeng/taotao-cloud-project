/**
 * Project Name: my-projects Package Name: com.taotao.cloud.order.biz.service.impl Date: 2020/6/10
 * 16:55 Author: shuigedeng
 */
package com.taotao.cloud.order.biz.service.impl;

import com.taotao.cloud.order.api.service.IOrderInfoService;
import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.order.biz.entity.QOrderInfo;
import com.taotao.cloud.order.biz.mapper.OrderInfoMapper;
import com.taotao.cloud.order.biz.repository.OrderInfoRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
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
public class OrderInfoInfoServiceImpl
	extends BaseSuperServiceImpl<OrderInfoMapper, OrderInfo, OrderInfoRepository, Long>
	implements IOrderInfoService<OrderInfo, Long> {

	private final static QOrderInfo ORDER_INFO = QOrderInfo.orderInfo;

}
