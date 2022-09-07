package com.taotao.cloud.promotion.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.promotion.api.web.vo.CouponActivityItemVO;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivityItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 优惠券活动
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:36:29
 */
public interface CouponActivityItemMapper extends BaseSuperMapper<CouponActivityItem> {

	/**
	 * 获取优惠券活动关联优惠券列表VO
	 *
	 * @param activityId 优惠券活动ID
	 * @return 优惠券活动关联优惠券列表VO
	 */
	@Select("""
		SELECT cai.*,
				c.coupon_name,
				c.price,
				c.coupon_type,
				c.coupon_discount 
		FROM tt_coupon_activity_item cai INNER JOIN tt_coupon c ON cai.coupon_id = c.id 
		WHERE cai.activity_id= #{activityId} 
		""")
	List<CouponActivityItemVO> getCouponActivityItemListVO(String activityId);
}
