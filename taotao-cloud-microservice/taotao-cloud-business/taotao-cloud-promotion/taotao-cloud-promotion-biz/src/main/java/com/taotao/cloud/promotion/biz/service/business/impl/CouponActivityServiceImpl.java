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

import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.member.api.model.vo.MemberVO;
import com.taotao.cloud.promotion.api.enums.CouponActivitySendTypeEnum;
import com.taotao.cloud.promotion.api.enums.CouponActivityTypeEnum;
import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.dto.CouponActivityDTO;
import com.taotao.cloud.promotion.api.model.vo.CouponActivityVO;
import com.taotao.cloud.promotion.biz.mapper.CouponActivityMapper;
import com.taotao.cloud.promotion.biz.model.entity.Coupon;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivity;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivityItem;
import com.taotao.cloud.promotion.biz.model.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.business.ICouponActivityItemService;
import com.taotao.cloud.promotion.biz.service.business.ICouponActivityService;
import com.taotao.cloud.promotion.biz.service.business.ICouponService;
import com.taotao.cloud.promotion.biz.service.business.IMemberCouponService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 优惠券活动业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:45:59
 */
@Service
public class CouponActivityServiceImpl extends AbstractPromotionsServiceImpl<CouponActivityMapper, CouponActivity>
        implements ICouponActivityService {

    @Autowired
    private ICouponService couponService;

    @Autowired
    private IMemberCouponService memberCouponService;

    @Autowired
    private ICouponActivityItemService couponActivityItemService;

    @Autowired
    private IFeignMemberApi memberApi;

    @Override
    public CouponActivityVO getCouponActivityVO(String couponActivityId) {
        CouponActivity couponActivity = this.getById(couponActivityId);
        CouponActivityVO couponActivityVO = new CouponActivityVO();
        BeanUtils.copyProperties(couponActivity, couponActivityVO);
        couponActivityVO.setCouponActivityItems(
                couponActivityItemService.getCouponActivityItemListVO(couponActivityId));
        return couponActivityVO;
    }

    @Override
    public void specify(Long couponActivityId) {
        // 获取优惠券
        CouponActivity couponActivity = this.getById(couponActivityId);
        // 获取活动优惠券发送范围
        List<Map<String, Object>> member = this.getMemberList(couponActivity);

        // 会员拆成多个小组进行发送
        List<List<Map<String, Object>>> memberGroup = new ArrayList<>();

        // 循环分组
        for (int i = 0; i < (member.size() / 100 + (member.size() % 100 == 0 ? 0 : 1)); i++) {
            int endPoint = Math.min((100 + (i * 100)), member.size());
            memberGroup.add(member.subList((i * 100), endPoint));
        }

        // 优惠优惠券活动的优惠券列表
        List<CouponActivityItem> couponActivityItems =
                couponActivityItemService.getCouponActivityList(couponActivity.getId());
        // 发送优惠券
        for (List<Map<String, Object>> memberList : memberGroup) {
            sendCoupon(memberList, couponActivityItems);
        }
    }

    @Override
    public void registered(List<CouponActivity> couponActivityList, MemberVO member) {
        for (CouponActivity couponActivity : couponActivityList) {
            // 获取会员信息
            List<Map<String, Object>> memberList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>(2);
            map.put("id", member.getId());
            map.put("nick_name", member.getNickName());
            memberList.add(map);

            // 优惠优惠券活动的优惠券列表
            List<CouponActivityItem> couponActivityItems =
                    couponActivityItemService.getCouponActivityList(couponActivity.getId());

            // 发送优惠券
            sendCoupon(memberList, couponActivityItems);
        }
    }

    /**
     * 初始化促销字段
     *
     * @param promotions 促销实体
     */
    @Override
    public void initPromotion(CouponActivity promotions) {
        super.initPromotion(promotions);

        if (promotions instanceof CouponActivityDTO) {
            CouponActivityDTO couponActivityDTO = (CouponActivityDTO) promotions;
            // 如果有会员，则写入会员信息
            if (couponActivityDTO.getMemberDTOS() != null
                    && !couponActivityDTO.getMemberDTOS().isEmpty()) {
                couponActivityDTO.setActivityScopeInfo(JSONUtil.toJsonStr(couponActivityDTO.getMemberDTOS()));
            }
        }
    }

    /**
     * 检查优惠券活动参数
     *
     * @param couponActivity 优惠券活动实体
     */
    @Override
    public void checkPromotions(CouponActivity couponActivity) {
        super.checkPromotions(couponActivity);

        if (couponActivity instanceof CouponActivityDTO) {
            CouponActivityDTO couponActivityDTO = (CouponActivityDTO) couponActivity;
            // 指定会员判定
            if (couponActivity.getActivityScope().equals(CouponActivitySendTypeEnum.DESIGNATED.name())
                    && couponActivityDTO.getMemberDTOS().isEmpty()) {
                throw new BusinessException(ResultEnum.COUPON_ACTIVITY_MEMBER_ERROR);
            }
            // 检查优惠券
            this.checkCouponActivityItem(couponActivityDTO.getCouponActivityItems());
        }
    }

    /**
     * 更新优惠券活动商品信息
     *
     * @param couponActivity 优惠券活动实体
     */
    @Override
    public void updatePromotionsGoods(CouponActivity couponActivity) {
        super.updatePromotionsGoods(couponActivity);
        if (couponActivity instanceof CouponActivityDTO
                && !PromotionsStatusEnum.CLOSE.name().equals(couponActivity.getPromotionStatus())
                && PromotionsScopeTypeEnum.PORTION_GOODS.name().equals(couponActivity.getScopeType())) {

            CouponActivityDTO couponActivityDTO = (CouponActivityDTO) couponActivity;
            // 创建优惠券活动子列表
            for (CouponActivityItem couponActivityItem : couponActivityDTO.getCouponActivityItems()) {
                couponActivityItem.setActivityId(couponActivityDTO.getId());
            }
            // 更新优惠券活动项信息
            couponActivityItemService.saveBatch(couponActivityDTO.getCouponActivityItems());
        }
    }

    /**
     * 更新优惠券活动信息到商品索引
     *
     * @param couponActivity 促销实体
     */
    @Override
    public void updateEsGoodsIndex(CouponActivity couponActivity) {
        // 如果是精准发券，进行发送优惠券
        if (!PromotionsStatusEnum.CLOSE.name().equals(couponActivity.getPromotionStatus())
                && couponActivity.getCouponActivityType().equals(CouponActivityTypeEnum.SPECIFY.name())) {
            this.specify(couponActivity.getId());
        }
    }

    /**
     * 当前促销类型
     *
     * @return 当前促销类型
     */
    @Override
    public PromotionTypeEnum getPromotionType() {
        return PromotionTypeEnum.COUPON_ACTIVITY;
    }

    /**
     * 发送优惠券 1.循环优惠券列表 2.判断优惠券每个会员发送数量 3.循环会员列表，发送优惠券 4.记录优惠券发送数量
     *
     * @param memberList 用户列表
     * @param couponActivityItems 优惠券列表
     */
    private void sendCoupon(List<Map<String, Object>> memberList, List<CouponActivityItem> couponActivityItems) {

        for (CouponActivityItem couponActivityItem : couponActivityItems) {
            // 获取优惠券
            Coupon coupon = couponService.getById(couponActivityItem.getCouponId());
            // 判断优惠券是否存在
            if (coupon != null) {
                List<MemberCoupon> memberCouponList = new LinkedList<>();
                // 循环优惠券的领取数量
                int j = couponActivityItem.getNum();
                for (int i = 1; i <= j; i++) {
                    // 循环会员列表，添加优惠券
                    for (Map<String, Object> map : memberList) {
                        MemberCoupon memberCoupon = new MemberCoupon(coupon);
                        memberCoupon.setMemberId(Long.valueOf(map.get("id").toString()));
                        memberCoupon.setMemberName(map.get("nick_name").toString());
                        memberCoupon.setMemberCouponStatus(MemberCouponStatusEnum.NEW.name());
                        memberCoupon.setIsPlatform(Long.valueOf(0).equals(coupon.getStoreId()));
                        memberCouponList.add(memberCoupon);
                    }
                }
                // 批量添加优惠券
                memberCouponService.saveBatch(memberCouponList);
                // 添加优惠券已领取数量
                couponService.receiveCoupon(
                        couponActivityItem.getCouponId(), memberCouponList.size() * couponActivityItem.getNum());
            } else {
                log.error("赠送优惠券失败,当前优惠券不存在:" + couponActivityItem.getCouponId());
            }
        }
    }

    /**
     * 获取优惠券的范围范围 此方法用于精准发券
     *
     * @param couponActivity 优惠券活动
     * @return 获取优惠券的会员列表
     */
    private List<Map<String, Object>> getMemberList(CouponActivity couponActivity) {
        // 判断优惠券的发送范围，获取会员列表
        if ("ALL".equals(couponActivity.getActivityScope())) {
            return this.memberApi.listFieldsByMemberIds("id,nick_name", null);
        } else {
            List<String> ids = new ArrayList<>();
            if (JSONUtil.isJsonArray(couponActivity.getActivityScopeInfo())) {
                JSONArray array = JSONUtil.parseArray(couponActivity.getActivityScopeInfo());
                ids = array.toList(Map.class)
					.stream()
                        .map(i -> i.get("id").toString())
                        .toList();
            }
            return memberApi.listFieldsByMemberIds("id,nick_name", ids);
        }
    }

    /**
     * 检查优惠券
     *
     * @param couponActivityItems 优惠券列表
     */
    private void checkCouponActivityItem(List<CouponActivityItem> couponActivityItems) {
        // 优惠券数量判定
        if (couponActivityItems.isEmpty()) {
            throw new BusinessException(ResultEnum.COUPON_ACTIVITY_ITEM_ERROR);
        } else if (couponActivityItems.size() > 10) {
            throw new BusinessException(ResultEnum.COUPON_ACTIVITY_ITEM_MUST_NUM_ERROR);
        } else {
            for (CouponActivityItem item : couponActivityItems) {
                if (item.getNum() == null || item.getNum() <= 0) {
                    throw new BusinessException(ResultEnum.COUPON_ACTIVITY_ITEM_NUM_ERROR);
                }
            }
        }
    }
}
