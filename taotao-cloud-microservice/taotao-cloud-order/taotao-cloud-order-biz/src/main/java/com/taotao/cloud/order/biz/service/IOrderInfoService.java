package com.taotao.cloud.order.biz.service;


import com.taotao.cloud.order.api.bo.order_info.OrderBO;
import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;

/**
 * 订单管理service
 *
 * @author shuigedeng
 * @since 2020/4/30 11:03
 */
public interface IOrderInfoService extends
	BaseSuperService<OrderInfo, Long> {

	/**
	 * existByCode
	 *
	 * @param code code
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-12-15 20:22:45
	 */
	Boolean existByCode(String code);

	/**
	 * queryRegionByParentId
	 *
	 * @param parentId parentId
	 * @return {@link List&lt;com.taotao.cloud.order.api.bo.order_info.OrderBO&gt; }
	 * @author shuigedeng
	 * @since 2021-12-15 20:23:45
	 */
	List<OrderBO> queryRegionByParentId(Long parentId);
}

