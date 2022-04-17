package com.taotao.cloud.promotion.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.promotion.api.vo.CouponActivityItemVO;
import com.taotao.cloud.promotion.biz.entity.CouponActivityItem;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 * 优惠券活动
 */
public interface CouponActivityItemMapper extends BaseMapper<CouponActivityItem> {

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
		FROM li_coupon_activity_item cai INNER JOIN li_coupon c ON cai.coupon_id = c.id 
		WHERE cai.activity_id= #{activityId} 
		""")
	List<CouponActivityItemVO> getCouponActivityItemListVO(String activityId);
}
