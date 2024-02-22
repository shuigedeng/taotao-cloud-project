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

package com.taotao.cloud.sys.application.event.roketmq.handler.impl;

import com.taotao.cloud.sys.application.event.roketmq.handler.MemberRegisterEventHandler;
import org.springframework.stereotype.Component;

/** 注册赠券活动 */
@Component
public class RegisteredCouponActivityExecute implements MemberRegisterEventHandler {

//    @Autowired
//    private IFeignCouponActivityApi couponActivityService;
//
//    /**
//     * 获取进行中的注册赠券的优惠券活动 发送注册赠券
//     *
//     * @param member 会员
//     */
//    @Override
//    public void memberRegister(Member member) {
//        // List<CouponActivity> couponActivities = couponActivityService.list(
//        // 	new QueryWrapper<CouponActivity>()
//        // 		.eq("coupon_activity_type", CouponActivityTypeEnum.REGISTERED.name())
//        // 		.and(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START)));
//        List<CouponActivityVO> couponActivityVOS = new ArrayList<>();
//        MemberDTO memberDTO = new MemberDTO();
//        couponActivityService.registered(couponActivityVOS, memberDTO);
//    }
}
