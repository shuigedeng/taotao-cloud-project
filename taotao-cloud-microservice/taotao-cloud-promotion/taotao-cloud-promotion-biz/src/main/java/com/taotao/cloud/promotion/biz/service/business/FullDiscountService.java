package com.taotao.cloud.promotion.biz.service.business;


import com.taotao.cloud.order.api.model.vo.cart.FullDiscountVO;
import com.taotao.cloud.promotion.biz.model.entity.FullDiscount;

import java.util.List;

/**
 * 满优惠业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:32
 */
public interface FullDiscountService extends AbstractPromotionsService<FullDiscount> {

	/**
	 * 当前满优惠活动
	 *
	 * @param storeId 商家编号
	 * @return {@link List }<{@link FullDiscountVO }>
	 * @since 2022-04-27 16:43:32
	 */
	List<FullDiscountVO> currentPromotion(List<String> storeId);

	/**
	 * 获取满优惠活动详情
	 *
	 * @param id 满优惠KID
	 * @return {@link FullDiscountVO }
	 * @since 2022-04-27 16:43:32
	 */
	FullDiscountVO getFullDiscount(String id);


}
