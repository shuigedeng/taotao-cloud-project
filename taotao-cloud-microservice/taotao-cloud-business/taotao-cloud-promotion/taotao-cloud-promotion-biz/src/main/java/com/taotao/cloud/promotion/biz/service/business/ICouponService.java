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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.promotion.api.model.page.CouponPageQuery;
import com.taotao.cloud.promotion.api.model.vo.CouponVO;
import com.taotao.cloud.promotion.biz.model.entity.Coupon;

/**
 * 优惠券业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:27
 */
public interface ICouponService extends AbstractPromotionsService<Coupon> {

    /**
     * 领取优惠券
     *
     * @param couponId 优惠券id
     * @param receiveNum 领取数量
     * @since 2022-04-27 16:43:27
     */
    void receiveCoupon(Long couponId, Integer receiveNum);

    /**
     * 使用优惠券
     *
     * @param couponId 优惠券id
     * @param usedNum 使用数量
     * @since 2022-04-27 16:43:27
     */
    void usedCoupon(String couponId, Integer usedNum);

    /**
     * 获取优惠券展示实体
     *
     * @param searchParams 查询参数
     * @param page 分页参数
     * @return {@link IPage }<{@link CouponVO }>
     * @since 2022-04-27 16:43:27
     */
    IPage<CouponVO> pageVOFindAll(CouponPageQuery searchParams, PageQuery page);

    /**
     * 获取优惠券展示详情
     *
     * @param couponId 优惠券id
     * @return {@link CouponVO }
     * @since 2022-04-27 16:43:27
     */
    CouponVO getDetail(String couponId);
}
