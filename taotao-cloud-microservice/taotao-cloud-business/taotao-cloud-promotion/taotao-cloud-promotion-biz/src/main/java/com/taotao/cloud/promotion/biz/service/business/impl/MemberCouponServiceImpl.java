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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.CouponPageQuery;
import com.taotao.cloud.promotion.biz.mapper.MemberCouponMapper;
import com.taotao.cloud.promotion.biz.model.entity.Coupon;
import com.taotao.cloud.promotion.biz.model.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.business.ICouponService;
import com.taotao.cloud.promotion.biz.service.business.IMemberCouponService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员优惠券业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberCouponServiceImpl extends ServiceImpl<MemberCouponMapper, MemberCoupon>
        implements IMemberCouponService {

    /** 优惠券 */
    @Autowired
    private ICouponService couponService;

    @Override
    public void checkCouponLimit(String couponId, String memberId) {
        Coupon coupon = couponService.getById(couponId);
        LambdaQueryWrapper<MemberCoupon> queryWrapper = new LambdaQueryWrapper<MemberCoupon>()
                .eq(MemberCoupon::getCouponId, couponId)
                .eq(MemberCoupon::getMemberId, memberId);
        long haveCoupons = this.count(queryWrapper);
        if (!PromotionsStatusEnum.START.name().equals(coupon.getPromotionStatus())) {
            throw new BusinessException(ResultEnum.COUPON_RECEIVE_ERROR);
        }
        if (coupon.getPublishNum() != 0 && coupon.getReceivedNum() >= coupon.getPublishNum()) {
            throw new BusinessException(ResultEnum.COUPON_NUM_INSUFFICIENT_ERROR);
        }
        if (!coupon.getCouponLimitNum().equals(0) && haveCoupons >= coupon.getCouponLimitNum()) {
            throw new BusinessException(ResultEnum.COUPON_LIMIT_ERROR, "此优惠券最多领取" + coupon.getCouponLimitNum() + "张");
        }
    }

    /**
     * 领取优惠券
     *
     * @param couponId 优惠券编号
     * @param memberId 会员
     * @param memberName 会员名称
     */
    @Override
    public void receiveBuyerCoupon(String couponId, String memberId, String memberName) {
        Coupon coupon = couponService.getById(couponId);
        if (coupon != null && !CouponGetEnum.FREE.name().equals(coupon.getGetType())) {
            throw new BusinessException(ResultEnum.COUPON_DO_NOT_RECEIVER);
        } else if (coupon != null) {
            this.receiverCoupon(couponId, memberId, memberName, coupon);
        }
    }

    @Override
    public void receiveCoupon(String couponId, String memberId, String memberName) {
        Coupon coupon = couponService.getById(couponId);
        if (coupon != null) {
            this.receiverCoupon(couponId, memberId, memberName, coupon);
        } else {
            throw new BusinessException(ResultEnum.COUPON_NOT_EXIST);
        }
    }

    @Override
    public IPage<MemberCoupon> getMemberCoupons(CouponPageQuery param, PageVO pageVo) {
        QueryWrapper<MemberCoupon> queryWrapper = param.queryWrapper();
        return this.page(PageUtil.initPage(pageVo), queryWrapper);
    }

    @Override
    public List<MemberCoupon> getMemberCoupons() {
        AuthUser authUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
        LambdaQueryWrapper<MemberCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberCoupon::getMemberId, authUser.getId());
        queryWrapper.eq(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.NEW.name());
        queryWrapper.le(MemberCoupon::getStartTime, new Date());
        queryWrapper.ge(MemberCoupon::getEndTime, new Date());
        return this.list(queryWrapper);
    }

    /**
     * 获取会员优惠券列表
     *
     * @param param 查询参数
     * @param pageVo 分页参数
     * @return 会员优惠券列表
     */
    @Override
    public IPage<MemberCoupon> getMemberCouponsByCanUse(CouponPageQuery param, BigDecimal totalPrice, PageVO pageVo) {
        LambdaQueryWrapper<MemberCoupon> queryWrapper = new LambdaQueryWrapper<>();
        List<String> storeIds = new ArrayList<>(Arrays.asList(param.getStoreId().split(",")));
        storeIds.add("platform");
        queryWrapper.in(MemberCoupon::getStoreId, storeIds);
        queryWrapper.eq(MemberCoupon::getMemberId, param.getMemberId());
        queryWrapper.and(i -> i.like(MemberCoupon::getScopeId, param.getScopeId())
                .or(j -> j.eq(MemberCoupon::getScopeType, PromotionsScopeTypeEnum.ALL.name())));
        queryWrapper.eq(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.NEW.name());
        queryWrapper.le(MemberCoupon::getConsumeThreshold, totalPrice);
        queryWrapper.ge(MemberCoupon::getEndTime, new Date());
        return this.page(PageUtil.initPage(pageVo), queryWrapper);
    }

    /**
     * 获取当前会员当前商品可用的会员优惠券
     *
     * @param memberId 会员Id
     * @param couponIds 优惠券id列表
     * @return 会员优惠券列表
     */
    @Override
    public List<MemberCoupon> getCurrentGoodsCanUse(String memberId, List<String> couponIds, BigDecimal totalPrice) {
        LambdaQueryWrapper<MemberCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberCoupon::getMemberId, memberId);
        queryWrapper.eq(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.NEW.name());
        queryWrapper.in(MemberCoupon::getCouponId, couponIds);
        queryWrapper.ne(MemberCoupon::getScopeType, PromotionsScopeTypeEnum.ALL.name());
        queryWrapper.le(MemberCoupon::getConsumeThreshold, totalPrice);
        queryWrapper.ge(MemberCoupon::getEndTime, new Date());
        return this.list(queryWrapper);
    }

    /**
     * 获取当前会员全品类优惠券
     *
     * @param memberId 会员Id
     * @param storeId 店铺id
     * @return 会员优惠券列表
     */
    @Override
    public List<MemberCoupon> getAllScopeMemberCoupon(String memberId, List<String> storeId) {
        LambdaQueryWrapper<MemberCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberCoupon::getMemberId, memberId);
        queryWrapper.eq(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.NEW.name());
        queryWrapper.eq(MemberCoupon::getScopeType, PromotionsScopeTypeEnum.ALL.name());
        queryWrapper.ge(MemberCoupon::getEndTime, new Date()).and(i -> i.in(MemberCoupon::getStoreId, storeId)
                .or(j -> j.eq(MemberCoupon::getIsPlatform, true)));
        return this.list(queryWrapper);
    }

    @Override
    public long getMemberCouponsNum() {
        AuthUser authUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
        QueryWrapper<MemberCoupon> queryWrapper = Wrappers.query();
        queryWrapper.eq("member_id", authUser.getId());
        queryWrapper.eq("member_coupon_status", MemberCouponStatusEnum.NEW.name());
        queryWrapper.eq("delete_flag", false);
        return this.count(queryWrapper);
    }

    /**
     * 更新会员优惠券状态
     *
     * @param status 要变更的状态
     * @param id 会员优惠券id
     */
    @Override
    public void updateMemberCouponStatus(MemberCouponStatusEnum status, String id) {
        MemberCoupon memberCoupon = this.getById(id);
        if (memberCoupon == null) {
            throw new BusinessException(ResultEnum.COUPON_MEMBER_NOT_EXIST);
        }
        String memberCouponStatus = memberCoupon.getMemberCouponStatus();
        if (memberCouponStatus.equals(MemberCouponStatusEnum.NEW.name())
                || memberCouponStatus.equals(MemberCouponStatusEnum.USED.name())) {
            LambdaUpdateWrapper<MemberCoupon> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MemberCoupon::getId, id).set(MemberCoupon::getMemberCouponStatus, status.name());
            this.update(updateWrapper);
        } else {
            throw new BusinessException(ResultEnum.COUPON_MEMBER_STATUS_ERROR);
        }
    }

    @Override
    public void used(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            List<MemberCoupon> memberCoupons = this.listByIds(ids);

            // 如果查出来的优惠券数量不一致
            if (memberCoupons.size() != ids.size()) {
                throw new BusinessException(ResultEnum.COUPON_EXPIRED);
            }
            // 循环处理
            memberCoupons.forEach(item -> {
                if (!item.getMemberCouponStatus().equals(MemberCouponStatusEnum.NEW.name())) {
                    throw new BusinessException(ResultEnum.COUPON_EXPIRED);
                }
                item.setMemberCouponStatus(MemberCouponStatusEnum.USED.name());
                item.setConsumptionTime(new Date());
            });

            this.updateBatchById(memberCoupons);
        }
    }

    /**
     * 作废当前会员优惠券
     *
     * @param id id
     */
    @Override
    public void cancellation(String id) {
        LambdaUpdateWrapper<MemberCoupon> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MemberCoupon::getId, id);
        updateWrapper.set(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.CLOSED.name());
        this.update(updateWrapper);
    }

    /**
     * 关闭会员优惠券
     *
     * @param couponIds 优惠券id集合
     */
    @Override
    public void closeMemberCoupon(List<String> couponIds) {
        LambdaUpdateWrapper<MemberCoupon> memberCouponLambdaUpdateWrapper = new LambdaUpdateWrapper<MemberCoupon>()
                .in(MemberCoupon::getCouponId, couponIds)
                .set(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.CLOSED.name());
        this.update(memberCouponLambdaUpdateWrapper);
    }

    @Override
    public boolean recoveryMemberCoupon(List<String> memberCouponIds) {
        LambdaUpdateWrapper<MemberCoupon> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(MemberCoupon::getId, memberCouponIds);
        updateWrapper.set(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.NEW.name());
        return this.update(updateWrapper);
    }

    @Override
    public void voidCoupon(String couponId) {
        LambdaUpdateWrapper<MemberCoupon> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(MemberCoupon::getCouponId, couponId);
        updateWrapper.set(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.CLOSED.name());
        updateWrapper.set(MemberCoupon::getDeleteFlag, true);
        this.update(updateWrapper);
    }

    private void receiverCoupon(String couponId, String memberId, String memberName, Coupon coupon) {
        this.checkCouponLimit(couponId, memberId);
        MemberCoupon memberCoupon = new MemberCoupon(coupon);
        memberCoupon.setMemberId(memberId);
        memberCoupon.setMemberName(memberName);
        memberCoupon.setMemberCouponStatus(MemberCouponStatusEnum.NEW.name());
        memberCoupon.setIsPlatform(("platform").equals(coupon.getStoreId()));
        this.save(memberCoupon);
        couponService.receiveCoupon(couponId, 1);
    }
}
