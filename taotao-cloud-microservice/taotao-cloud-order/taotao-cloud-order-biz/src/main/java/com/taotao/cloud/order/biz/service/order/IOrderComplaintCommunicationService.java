package com.taotao.cloud.order.biz.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationSearchParams;
import com.taotao.cloud.order.biz.entity.order.OrderComplaintCommunication;

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
	 * @param searchParams 参数
	 * @param pageVO       分页
	 * @return {@link IPage }<{@link OrderComplaintCommunication }>
	 * @since 2022-04-28 08:54:33
	 */
	IPage<OrderComplaintCommunication> getCommunication(
		OrderComplaintCommunicationSearchParams searchParams, PageVO pageVO);


}
