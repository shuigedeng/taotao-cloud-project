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

package com.taotao.cloud.promotion.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.promotion.api.model.vo.CouponActivityItemVO;
import com.taotao.cloud.promotion.biz.mapper.CouponActivityItemMapper;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivityItem;
import com.taotao.cloud.promotion.biz.service.business.ICouponActivityItemService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 优惠券活动关联优惠券业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:01
 */
@Service
public class CouponActivityItemServiceImpl extends ServiceImpl<CouponActivityItemMapper, CouponActivityItem>
        implements ICouponActivityItemService {

    @Override
    public List<CouponActivityItem> getCouponActivityList(Long activityId) {
        LambdaQueryWrapper<CouponActivityItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CouponActivityItem::getActivityId, activityId);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<CouponActivityItemVO> getCouponActivityItemListVO(String activityId) {
        return this.baseMapper.getCouponActivityItemListVO(activityId);
    }

    /**
     * 根据优惠券id删除优惠活动关联信息项
     *
     * @param couponIds 优惠券id集合
     */
    @Override
    public void removeByCouponId(List<String> couponIds) {
        this.remove(new LambdaQueryWrapper<CouponActivityItem>().in(CouponActivityItem::getCouponId, couponIds));
    }
}
