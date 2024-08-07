/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.promotion.biz.mapper;

import com.taotao.cloud.promotion.api.model.vo.CouponActivityItemVO;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivityItem;
import java.util.List;
import org.apache.ibatis.annotations.Select;

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
    @Select(
            """
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
