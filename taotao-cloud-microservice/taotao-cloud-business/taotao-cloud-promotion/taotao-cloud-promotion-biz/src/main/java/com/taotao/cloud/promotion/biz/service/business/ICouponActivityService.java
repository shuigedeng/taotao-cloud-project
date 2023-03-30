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

package com.taotao.cloud.promotion.biz.service.business;

import com.taotao.cloud.member.api.model.vo.MemberVO;
import com.taotao.cloud.promotion.api.model.vo.CouponActivityVO;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivity;
import java.util.List;

/**
 * 优惠券活动业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:22
 */
public interface ICouponActivityService extends AbstractPromotionsService<CouponActivity> {

    /**
     * 获取优惠券活动VO 包含优惠券活动信息以及优惠券关联优惠券列表
     *
     * @param couponActivityId 优惠券活动ID
     * @return {@link CouponActivityVO }
     * @since 2022-04-27 16:43:22
     */
    CouponActivityVO getCouponActivityVO(String couponActivityId);

    /**
     * 精准发券
     *
     * @param couponActivityId 优惠券活动ID
     * @since 2022-04-27 16:43:22
     */
    void specify(Long couponActivityId);

    /**
     * 注册赠券
     *
     * @param couponActivityList 优惠券活动
     * @param member 会员
     * @since 2022-04-27 16:43:22
     */
    void registered(List<CouponActivity> couponActivityList, MemberVO member);
}
