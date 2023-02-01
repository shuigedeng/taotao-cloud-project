package com.taotao.cloud.order.biz.service.business.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.model.page.order.OrderComplaintCommunicationPageQuery;
import com.taotao.cloud.order.biz.model.entity.order.OrderComplaintCommunication;

/**
 * 订单投诉通信业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:33
 */
public interface IOrderComplaintCommunicationService extends IService<OrderComplaintCommunication> {

	/**
	 * 添加订单投诉通信
	 *
	 * @param orderComplaintCommunication 订单投诉通信
	 * @return {@link Boolean }
	 * @since 2022-04-28 08:54:33
	 */
	Boolean addCommunication(OrderComplaintCommunication orderComplaintCommunication);

	/**
	 * 获取通信记录
	 *
	 * @param orderComplaintCommunicationPageQuery 参数
	 * @return {@link IPage }<{@link OrderComplaintCommunication }>
	 * @since 2022-04-28 08:54:33
	 */
	IPage<OrderComplaintCommunication> getCommunication(
		OrderComplaintCommunicationPageQuery orderComplaintCommunicationPageQuery);


}
