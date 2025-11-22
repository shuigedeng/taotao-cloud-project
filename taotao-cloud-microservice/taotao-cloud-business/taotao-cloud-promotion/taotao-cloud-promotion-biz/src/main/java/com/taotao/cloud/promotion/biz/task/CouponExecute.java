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

package com.taotao.cloud.promotion.biz.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.biz.model.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.business.IMemberCouponService;
import com.taotao.boot.webmvc.timetask.EveryDayExecute;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 优惠券状态监测 */
@Component
public class CouponExecute implements EveryDayExecute {

    /** 过期常量，过期后或者使用后一定时间内，删除无效的优惠券，物理删除 */
    static final int EXPIRATION_DAY = 3;

    @Autowired
    private IMemberCouponService memberCouponService;

    /** 检测优惠券的使用时间，超期未使用则失效 此方法用于领取*天后失效优惠券使用 */
    @Override
    public void execute() {
        // 将过期优惠券变更为过期状体
        LambdaUpdateWrapper<MemberCoupon> updateWrapper = new LambdaUpdateWrapper<MemberCoupon>()
                .eq(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.NEW.name())
                .le(MemberCoupon::getEndTime, new Date())
                .set(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.EXPIRE.name());
        this.memberCouponService.update(updateWrapper);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - EXPIRATION_DAY);
        Date removeTime = calendar.getTime();
        // 删除过期/已使用的优惠券
        LambdaUpdateWrapper<MemberCoupon> deleteWrapper = new LambdaUpdateWrapper<MemberCoupon>()
                // 如果结束时间小于 当前时间增加指定删除日期，则删除
                .le(MemberCoupon::getEndTime, removeTime);
        this.memberCouponService.remove(deleteWrapper);
    }
}
