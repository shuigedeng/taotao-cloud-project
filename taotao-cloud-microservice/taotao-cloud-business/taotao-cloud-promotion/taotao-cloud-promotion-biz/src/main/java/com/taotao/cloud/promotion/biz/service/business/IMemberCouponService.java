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
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.api.model.page.CouponPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.MemberCoupon;
import java.math.BigDecimal;
import java.util.List;

/**
 * 会员优惠券业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:53
 */
public interface IMemberCouponService extends IService<MemberCoupon> {

    /**
     * 检查该会员领取优惠券的可领取数量
     *
     * @param couponId 优惠券编号
     * @param memberId 会员
     * @since 2022-04-27 16:43:53
     */
    void checkCouponLimit(String couponId, String memberId);

    /**
     * 领取优惠券
     *
     * @param couponId 优惠券编号
     * @param memberId 会员
     * @param memberName 会员名称
     * @since 2022-04-27 16:43:53
     */
    void receiveBuyerCoupon(String couponId, String memberId, String memberName);

    /**
     * 领取优惠券
     *
     * @param couponId 优惠券编号
     * @param memberId 会员
     * @param memberName 会员名称
     * @since 2022-04-27 16:43:53
     */
    void receiveCoupon(String couponId, String memberId, String memberName);

    /**
     * 获取会员优惠券列表
     *
     * @param param 查询参数
     * @param pageVo 分页参数
     * @return {@link IPage }<{@link MemberCoupon }>
     * @since 2022-04-27 16:43:53
     */
    IPage<MemberCoupon> getMemberCoupons(CouponPageQuery param, PageQuery pageVo);

    /**
     * 获取会员所有优惠券
     *
     * @return {@link List }<{@link MemberCoupon }>
     * @since 2022-04-27 16:43:53
     */
    List<MemberCoupon> getMemberCoupons();

    /**
     * 获取会员优惠券列表
     *
     * @param param 查询参数
     * @param totalPrice 当前商品总价
     * @param pageVo 分页参数
     * @return {@link IPage }<{@link MemberCoupon }>
     * @since 2022-04-27 16:43:53
     */
    IPage<MemberCoupon> getMemberCouponsByCanUse(CouponPageQuery param, BigDecimal totalPrice, PageQuery pageVo);

    /**
     * 获取当前会员当前商品可用的会员优惠券
     *
     * @param memberId 会员Id
     * @param couponIds 优惠券id列表
     * @param totalPrice 当前商品总价
     * @return {@link List }<{@link MemberCoupon }>
     * @since 2022-04-27 16:43:53
     */
    List<MemberCoupon> getCurrentGoodsCanUse(String memberId, List<String> couponIds, BigDecimal totalPrice);

    /**
     * 获取当前会员全品类优惠券
     *
     * @param memberId 会员Id
     * @param storeId 店铺id
     * @return {@link List }<{@link MemberCoupon }>
     * @since 2022-04-27 16:43:53
     */
    List<MemberCoupon> getAllScopeMemberCoupon(String memberId, List<String> storeId);

    /**
     * 获取会员优惠券数量
     *
     * @return long
     * @since 2022-04-27 16:43:53
     */
    long getMemberCouponsNum();

    /**
     * 更新会员优惠券状态
     *
     * @param status 要变更的状态
     * @param id 会员优惠券id
     * @since 2022-04-27 16:43:53
     */
    void updateMemberCouponStatus(MemberCouponStatusEnum status, String id);

    /**
     * 使用优惠券
     *
     * @param ids 会员优惠券id
     * @since 2022-04-27 16:43:53
     */
    void used(List<String> ids);

    /**
     * 作废当前会员优惠券
     *
     * @param id id
     * @since 2022-04-27 16:43:53
     */
    void cancellation(String id);

    /**
     * 关闭会员优惠券
     *
     * @param couponIds 优惠券id集合
     * @since 2022-04-27 16:43:53
     */
    void closeMemberCoupon(List<String> couponIds);
    /**
     * 恢复会员优惠券
     *
     * @param memberCouponIds 会员优惠券id列表
     * @return 是否恢复成功
     */
    boolean recoveryMemberCoupon(List<String> memberCouponIds);

    /**
     * 作废优惠券
     *
     * @param couponId 优惠券ID
     */
    void voidCoupon(String couponId);
}
