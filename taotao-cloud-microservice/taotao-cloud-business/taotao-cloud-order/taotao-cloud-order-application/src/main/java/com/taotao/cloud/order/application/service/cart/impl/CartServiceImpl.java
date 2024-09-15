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

package com.taotao.cloud.order.application.service.cart.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.order.application.command.cart.dto.clientobject.CartSkuCO;
import com.taotao.cloud.order.application.service.cart.ICartService;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 购物车业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:47
 */
@Service
@AllArgsConstructor
public class CartServiceImpl implements ICartService {

    static String errorMessage = "购物车异常，请稍后重试";

    /** 缓存 */
    private final RedisRepository redisRepository;
    /** 会员优惠券 */
    private final IFeignMemberCouponApi memberCouponApi;
    /** 规格商品 */
    private final IFeignGoodsSkuApi goodsSkuApi;
    /** 促销商品 */
    private final IFeignPromotionGoodsApi promotionGoodsApi;
    /** 促销商品 */
    private final IFeignPointsGoodsApi pointsGoodsApi;
    /** 会员地址 */
    private final IFeignMemberAddressApi memberAddressApi;
    /** ES商品 */
    private final IFeignEsGoodsSearchApi esGoodsSearchApi;
    /** ES商品 */
    private final IFeignGoodsApi goodsApi;
    /** 拼团 */
    private final IFeignPintuanApi pintuanApi;
    /** 砍价活动 */
    private final IFeignKanjiaActivityApi kanjiaActivityApi;
    /** 砍价商品 */
    private final IFeignKanjiaActivityGoodsApi kanjiaActivityGoodsApi;
    /** 交易 */
    @Autowired
    private TradeBuilder tradeBuilder;

    @Override
    public boolean add(String skuId, Integer num, String cartType, Boolean cover) {
        if (num <= 0) {
            throw new BusinessException(ResultEnum.CART_NUM_ERROR);
        }

        CartTypeEnum cartTypeEnum = getCartType(cartType);
        GoodsSku dataSku = checkGoods(skuId, cartType);
        try {
            // 购物车方式购买需要保存之前的选择，其他方式购买，则直接抹除掉之前的记录
            TradeDTO tradeDTO;
            if (cartTypeEnum.equals(CartTypeEnum.CART)) {
                // 如果存在，则变更数量不做新增，否则新增一个商品进入集合
                tradeDTO = this.readDTO(cartTypeEnum);
                List<CartSkuCO> cartSkuCOS = tradeDTO.getSkuList();
                CartSkuCO cartSkuCO = cartSkuCOS.stream()
                        .filter(i -> i.getGoodsSku().getId().equals(skuId))
                        .findFirst()
                        .orElse(null);

                // 购物车中已经存在，更新数量
                if (cartSkuCO != null
                        && dataSku.getUpdateTime()
                                .equals(cartSkuCO.getGoodsSku().getUpdateTime())) {
                    // 如果覆盖购物车中商品数量
                    if (Boolean.TRUE.equals(cover)) {
                        cartSkuCO.setNum(num);
                        this.checkSetGoodsQuantity(cartSkuCO, skuId, num);
                    } else {
                        int oldNum = cartSkuCO.getNum();
                        int newNum = oldNum + num;
                        this.checkSetGoodsQuantity(cartSkuCO, skuId, newNum);
                    }

                    // 计算购物车小计
                    cartSkuCO.setSubTotal(CurrencyUtils.mul(cartSkuCO.getPurchasePrice(), cartSkuCO.getNum()));
                } else {
                    // 先清理一下 如果商品无效的话
                    cartSkuCOS.remove(cartSkuCO);
                    // 购物车中不存在此商品，则新建立一个
                    cartSkuCO = new CartSkuCO(dataSku);

                    cartSkuCO.setCartType(cartTypeEnum);
                    promotionGoodsApi.updatePromotion(cartSkuCO);
                    // 再设置加入购物车的数量
                    this.checkSetGoodsQuantity(cartSkuCO, skuId, num);
                    // 计算购物车小计
                    cartSkuCO.setSubTotal(CurrencyUtils.mul(cartSkuCO.getPurchasePrice(), cartSkuCO.getNum()));
                    cartSkuCOS.add(cartSkuCO);
                }

                // 新加入的商品都是选中的
                cartSkuCO.setChecked(true);
            } else {
                tradeDTO = new TradeDTO(cartTypeEnum);
                SecurityUser currentUser = SecurityUtils.getCurrentUser();
                tradeDTO.setMemberId(currentUser.getUserId());
                tradeDTO.setMemberName(currentUser.getUsername());
                List<CartSkuCO> cartSkuCOS = tradeDTO.getSkuList();

                // 购物车中不存在此商品，则新建立一个
                CartSkuCO cartSkuCO = new CartSkuCO(dataSku);
                cartSkuCO.setCartType(cartTypeEnum);
                promotionGoodsApi.updatePromotion(cartSkuCO);
                // 检测购物车数据
                checkCart(cartTypeEnum, cartSkuCO, skuId, num);
                // 计算购物车小计
                cartSkuCO.setSubTotal(CurrencyUtils.mul(cartSkuCO.getPurchasePrice(), cartSkuCO.getNum()));
                cartSkuCOS.add(cartSkuCO);
            }

            tradeDTO.setCartTypeEnum(cartTypeEnum);
            // 如购物车发生更改，则重置优惠券
            tradeDTO.setStoreCoupons(null);
            tradeDTO.setPlatformCoupon(null);
            this.resetTradeDTO(tradeDTO);
        } catch (Exception e) {
            LogUtils.error("购物车渲染异常", e);
            throw new BusinessException(errorMessage);
        }
        return true;
    }

    /**
     * 读取当前会员购物原始数据key
     *
     * @param cartTypeEnum 获取方式
     * @return 当前会员购物原始数据key {@link String }
     * @since 2022-05-16 16:51:16
     */
    private String getOriginKey(CartTypeEnum cartTypeEnum) {
        // 缓存key，默认使用购物车
        if (cartTypeEnum != null) {
            SecurityUser currentUser = SecurityUtils.getCurrentUser();
            return cartTypeEnum.getPrefix() + currentUser.getUserId();
        }
        throw new BusinessException(ResultEnum.ERROR);
    }

    @Override
    public TradeDTO readDTO(CartTypeEnum checkedWay) {
        TradeDTO tradeDTO = (TradeDTO) redisRepository.get(this.getOriginKey(checkedWay));
        if (tradeDTO == null) {
            tradeDTO = new TradeDTO(checkedWay);
            SecurityUser currentUser = SecurityUtils.getCurrentUser();
            tradeDTO.setMemberId(currentUser.getUserId());
            tradeDTO.setMemberName(currentUser.getUsername());
        }
        if (tradeDTO.getMemberAddress() == null) {
            tradeDTO.setMemberAddress(this.memberAddressApi.getDefaultMemberAddress());
        }
        return tradeDTO;
    }

    @Override
    public boolean checked(String skuId, boolean checked) {
        TradeDTO tradeDTO = this.readDTO(CartTypeEnum.CART);
        List<CartSkuCO> cartSkuCOS = tradeDTO.getSkuList();
        for (CartSkuCO cartSkuCO : cartSkuCOS) {
            if (cartSkuCO.getGoodsSku().getId().equals(skuId)) {
                cartSkuCO.setChecked(checked);
            }
        }
        redisRepository.set(this.getOriginKey(CartTypeEnum.CART), tradeDTO);
        return true;
    }

    @Override
    public boolean checkedStore(String storeId, boolean checked) {
        TradeDTO tradeDTO = this.readDTO(CartTypeEnum.CART);
        List<CartSkuCO> cartSkuCOS = tradeDTO.getSkuList();
        for (CartSkuCO cartSkuCO : cartSkuCOS) {
            if (cartSkuCO.getStoreId().equals(storeId)) {
                cartSkuCO.setChecked(checked);
            }
        }
        redisRepository.set(this.getOriginKey(CartTypeEnum.CART), tradeDTO);
        return true;
    }

    @Override
    public boolean checkedAll(boolean checked) {
        TradeDTO tradeDTO = this.readDTO(CartTypeEnum.CART);
        List<CartSkuCO> cartSkuCOS = tradeDTO.getSkuList();
        for (CartSkuCO cartSkuCO : cartSkuCOS) {
            cartSkuCO.setChecked(checked);
        }
        redisRepository.set(this.getOriginKey(CartTypeEnum.CART), tradeDTO);
        return true;
    }

    @Override
    public boolean delete(String[] skuIds) {
        TradeDTO tradeDTO = this.readDTO(CartTypeEnum.CART);
        List<CartSkuCO> cartSkuCOS = tradeDTO.getSkuList();
        List<CartSkuCO> deleteVos = new ArrayList<>();
        for (CartSkuCO cartSkuCO : cartSkuCOS) {
            for (String skuId : skuIds) {
                if (cartSkuCO.getGoodsSku().getId().equals(skuId)) {
                    deleteVos.add(cartSkuCO);
                }
            }
        }
        cartSkuCOS.removeAll(deleteVos);
        redisRepository.set(this.getOriginKey(CartTypeEnum.CART), tradeDTO);
        return true;
    }

    @Override
    public boolean clean() {
        redisRepository.del(this.getOriginKey(CartTypeEnum.CART));
        return true;
    }

    /**
     * 清洁检查
     *
     * @param tradeDTO 贸易dto
     * @since 2022-05-16 16:53:53
     */
    public void cleanChecked(TradeDTO tradeDTO) {
        List<CartSkuCO> cartSkuCOS = tradeDTO.getSkuList();
        List<CartSkuCO> deleteVos = new ArrayList<>();
        for (CartSkuCO cartSkuCO : cartSkuCOS) {
            if (Boolean.TRUE.equals(cartSkuCO.getChecked())) {
                deleteVos.add(cartSkuCO);
            }
        }
        cartSkuCOS.removeAll(deleteVos);
        // 清除选择的优惠券
        tradeDTO.setPlatformCoupon(null);
        tradeDTO.setStoreCoupons(null);
        // 清除添加过的备注
        tradeDTO.setStoreRemark(null);
        redisRepository.set(this.getOriginKey(tradeDTO.getCartTypeEnum()), tradeDTO);
    }

    @Override
    public boolean cleanChecked(CartTypeEnum way) {
        if (way.equals(CartTypeEnum.CART)) {
            TradeDTO tradeDTO = this.readDTO(CartTypeEnum.CART);
            this.cleanChecked(tradeDTO);
        } else {
            redisRepository.del(this.getOriginKey(way));
        }
        return true;
    }

    @Override
    public boolean resetTradeDTO(TradeDTO tradeDTO) {
        redisRepository.set(this.getOriginKey(tradeDTO.getCartTypeEnum()), tradeDTO);
        return true;
    }

    @Override
    public TradeDTO getCheckedTradeDTO(CartTypeEnum way) {
        return tradeBuilder.buildChecked(way);
    }

    @Override
    public Long getCanUseCoupon(CartTypeEnum checkedWay) {
        TradeDTO tradeDTO = this.readDTO(checkedWay);
        long count = 0L;
        BigDecimal totalPrice = tradeDTO.getSkuList().stream()
                .mapToBigDecimal(i -> i.getPurchasePrice() * i.getNum())
                .sum();
        if (tradeDTO.getSkuList() != null && !tradeDTO.getSkuList().isEmpty()) {
            List<String> ids = tradeDTO.getSkuList().parallelStream()
                    .filter(i -> Boolean.TRUE.equals(i.getChecked()))
                    .map(i -> i.getGoodsSku().getId())
                    .toList();

            List<EsGoodsIndex> esGoodsList = esGoodsSearchApi.getEsGoodsBySkuIds(ids);
            for (EsGoodsIndex esGoodsIndex : esGoodsList) {
                if (esGoodsIndex != null) {
                    if (esGoodsIndex.getPromotionMap() != null) {
                        List<String> couponIds = esGoodsIndex.getPromotionMap().keySet().parallelStream()
                                .filter(i -> i.contains(PromotionTypeEnum.COUPON.name()))
                                .map(i -> i.substring(i.lastIndexOf("-") + 1))
                                .toList();
                        if (!couponIds.isEmpty()) {
                            List<MemberCoupon> currentGoodsCanUse = memberCouponApi.getCurrentGoodsCanUse(
                                    tradeDTO.getMemberId(), couponIds, totalPrice);
                            count = currentGoodsCanUse.size();
                        }
                    }
                }
            }

            List<String> storeIds = new ArrayList<>();
            for (CartSkuCO cartSkuCO : tradeDTO.getSkuList()) {
                if (!storeIds.contains(cartSkuCO.getStoreId())) {
                    storeIds.add(cartSkuCO.getStoreId());
                }
            }

            // 获取可操作的优惠券集合
            List<MemberCoupon> allScopeMemberCoupon =
                    memberCouponApi.getAllScopeMemberCoupon(tradeDTO.getMemberId(), storeIds);
            if (allScopeMemberCoupon != null && !allScopeMemberCoupon.isEmpty()) {
                // 过滤满足消费门槛
                count += allScopeMemberCoupon.stream()
                        .filter(i -> i.getConsumeThreshold() <= totalPrice)
                        .count();
            }
        }
        return count;
    }

    @Override
    public TradeDTO getAllTradeDTO() {
        return tradeBuilder.buildCart(CartTypeEnum.CART);
    }

    /**
     * 校验商品有效性，判定失效和库存，促销活动价格
     *
     * @param skuId 商品skuId
     * @param cartType 购物车类型
     */
    private GoodsSkuSpecGalleryVO checkGoods(Long skuId, String cartType) {
        GoodsSkuSpecGalleryVO dataSku = this.goodsSkuApi.getGoodsSkuByIdFromCache(skuId);
        if (dataSku == null) {
            throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
        }

        GoodsSkuVOBuilder goodsSkuVOBuilder = GoodsSkuVOBuilder.builder(dataSku);
        GoodsSkuBaseVOBuilder goodsSkuBaseVOBuilder = GoodsSkuBaseVOBuilder.builder(dataSku.goodsSkuBase());

        if (!GoodsAuthEnum.PASS.name().equals(dataSku.goodsSkuBase().isAuth())
                || !GoodsStatusEnum.UPPER.name().equals(dataSku.goodsSkuBase().marketEnable())) {
            throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
        }

        BigDecimal validSeckillGoodsPrice = promotionGoodsApi.getValidPromotionsGoodsPrice(
                skuId, Collections.singletonList(PromotionTypeEnum.SECKILL.name()));
        if (validSeckillGoodsPrice != null) {
            goodsSkuBaseVOBuilder.promotionFlag(true);
            goodsSkuBaseVOBuilder.promotionPrice(validSeckillGoodsPrice);
        }

        BigDecimal validPintuanGoodsPrice = promotionGoodsApi.getValidPromotionsGoodsPrice(
                skuId, Collections.singletonList(PromotionTypeEnum.PINTUAN.name()));
        if (validPintuanGoodsPrice != null && CartTypeEnum.PINTUAN.name().equals(cartType)) {
            goodsSkuBaseVOBuilder.promotionFlag(true);
            goodsSkuBaseVOBuilder.promotionPrice(validPintuanGoodsPrice);
        }
        return goodsSkuVOBuilder.goodsSkuBase(goodsSkuBaseVOBuilder.build()).build();
    }

    /**
     * 检查并设置购物车商品数量
     *
     * @param cartSkuCO 购物车商品对象
     * @param skuId 商品id
     * @param num 购买数量
     */
    private CartSkuCO checkSetGoodsQuantity(CartSkuCO cartSkuCO, String skuId, Integer num) {
        Integer enableStock = goodsSkuApi.getStock(skuId);

        // 如果sku的可用库存小于等于0或者小于用户购买的数量，则不允许购买
        if (enableStock <= 0 || enableStock < num) {
            throw new BusinessException(ResultEnum.GOODS_SKU_QUANTITY_NOT_ENOUGH);
        }

        CartSkuVOBuilder cartSkuVOBuilder = CartSkuVOBuilder.builder(cartSkuCO);
        if (enableStock <= num) {
            cartSkuVOBuilder.num(enableStock);
        } else {
            cartSkuVOBuilder.num(num);
        }

        if (cartSkuCO.num() > 99) {
            cartSkuVOBuilder.num(99);
        }
        return cartSkuVOBuilder.build();
    }

    @Override
    public boolean shippingAddress(String shippingAddressId, String way) {

        // 默认购物车
        CartTypeEnum cartTypeEnum = CartTypeEnum.CART;
        if (CharSequenceUtil.isNotEmpty(way)) {
            cartTypeEnum = CartTypeEnum.valueOf(way);
        }

        TradeDTO tradeDTO = this.readDTO(cartTypeEnum);
        MemberAddressVO memberAddress = memberAddressApi.getById(shippingAddressId);
        tradeDTO.setMemberAddress(memberAddress);
        this.resetTradeDTO(tradeDTO);
        return true;
    }

    @Override
    public boolean shippingReceipt(ReceiptVO receiptVO, String way) {
        CartTypeEnum cartTypeEnum = CartTypeEnum.CART;
        if (CharSequenceUtil.isNotEmpty(way)) {
            cartTypeEnum = CartTypeEnum.valueOf(way);
        }
        TradeDTO tradeDTO = this.readDTO(cartTypeEnum);
        tradeDTO.setNeedReceipt(true);
        tradeDTO.setReceiptVO(receiptVO);
        this.resetTradeDTO(tradeDTO);
        return true;
    }

    @Override
    public boolean shippingMethod(String storeId, String deliveryMethod, String way) {
        CartTypeEnum cartTypeEnum = CartTypeEnum.CART;
        if (CharSequenceUtil.isNotEmpty(way)) {
            cartTypeEnum = CartTypeEnum.valueOf(way);
        }

        TradeDTO tradeDTO = this.readDTO(cartTypeEnum);
        for (CartVO cartVO : tradeDTO.getCartList()) {
            if (cartVO.cartBase().storeId().equals(storeId)) {
                cartVO.setDeliveryMethod(
                        DeliveryMethodEnum.valueOf(deliveryMethod).name());
            }
        }
        this.resetTradeDTO(tradeDTO);
        return true;
    }

    @Override
    public Long getCartNum(Boolean checked) {
        // 构建购物车
        TradeDTO tradeDTO = this.getAllTradeDTO();
        // 过滤sku列表
        List<CartSkuCO> collect = tradeDTO.getSkuList().stream()
                .filter(i -> Boolean.FALSE.equals(i.getInvalid()))
                .toList();
        long count = 0L;
        if (!tradeDTO.getSkuList().isEmpty()) {
            if (checked != null) {
                count = collect.stream()
                        .filter(i -> i.getChecked().equals(checked))
                        .count();
            } else {
                count = collect.size();
            }
        }
        return count;
    }

    @Override
    public boolean selectCoupon(String couponId, String way, boolean use) {
        SecurityUser currentUser = SecurityUtils.getCurrentUser();
        // 获取购物车，然后重新写入优惠券
        CartTypeEnum cartTypeEnum = getCartType(way);
        TradeDTO tradeDTO = this.readDTO(cartTypeEnum);

        MemberCoupon memberCoupon = memberCouponApi.getOne(new LambdaQueryWrapper<MemberCoupon>()
                .eq(MemberCoupon::getMemberCouponStatus, MemberCouponStatusEnum.NEW.name())
                .eq(MemberCoupon::getMemberId, currentUser.getUserId())
                .eq(MemberCoupon::getId, couponId));
        if (memberCoupon == null) {
            throw new BusinessException(ResultEnum.COUPON_EXPIRED);
        }
        // 使用优惠券 与否
        if (use) {
            this.useCoupon(tradeDTO, memberCoupon, cartTypeEnum);
        } else {
            if (Boolean.TRUE.equals(memberCoupon.getIsPlatform())) {
                tradeDTO.setPlatformCoupon(null);
            } else {
                tradeDTO.getStoreCoupons().remove(memberCoupon.getStoreId());
            }
        }
        this.resetTradeDTO(tradeDTO);
        return true;
    }

    @Override
    public Trade createTrade(TradeDTO tradeParams) {
        // 获取购物车
        CartTypeEnum cartTypeEnum = getCartType(tradeParams.getWay());
        TradeDTO tradeDTO = this.readDTO(cartTypeEnum);
        // 设置基础属性
        tradeDTO.setClientType(tradeParams.getClient());
        tradeDTO.setStoreRemark(tradeParams.getRemark());
        tradeDTO.setParentOrderSn(tradeParams.getParentOrderSn());
        // 订单无收货地址校验
        if (tradeDTO.getMemberAddress() == null) {
            throw new BusinessException(ResultEnum.MEMBER_ADDRESS_NOT_EXIST);
        }
        // 将购物车信息写入缓存，后续逻辑调用校验
        this.resetTradeDTO(tradeDTO);
        // 构建交易
        Trade trade = tradeBuilder.createTrade(cartTypeEnum);
        this.cleanChecked(tradeDTO);
        return trade;
    }

    /**
     * 获取购物车类型
     *
     * @param way 购物车类型
     * @return {@link CartTypeEnum }
     * @since 2022-05-16 16:49:56
     */
    private CartTypeEnum getCartType(String way) {
        // 默认购物车
        CartTypeEnum cartTypeEnum = CartTypeEnum.CART;
        if (CharSequenceUtil.isNotEmpty(way)) {
            try {
                cartTypeEnum = CartTypeEnum.valueOf(way);
            } catch (IllegalArgumentException e) {
                LogUtils.error("获取购物车类型出现错误：", e);
            }
        }
        return cartTypeEnum;
    }

    /**
     * 使用优惠券判定
     *
     * @param tradeDTO 交易对象
     * @param memberCoupon 会员优惠券
     * @param cartTypeEnum 购物车
     */
    private void useCoupon(TradeDTO tradeDTO, MemberCoupon memberCoupon, CartTypeEnum cartTypeEnum) {

        // 截取符合优惠券的商品
        List<CartSkuCO> cartSkuCOS = checkCoupon(memberCoupon, tradeDTO);

        // 定义使用优惠券的信息商品信息
        Map<String, BigDecimal> skuPrice = new HashMap<>(1);

        // 购物车价格
        BigDecimal cartPrice = BigDecimal.ZERO;

        // 循环符合优惠券的商品
        for (CartSkuCO cartSkuCO : cartSkuCOS) {
            if (!cartSkuCO.getChecked()) {
                continue;
            }
            // 获取商品的促销信息
            Optional<PromotionGoods> promotionOptional = cartSkuCO.getPromotions().parallelStream()
                    .filter(promotionGoods ->
                            (promotionGoods.getPromotionType().equals(PromotionTypeEnum.PINTUAN.name())
                                            && cartTypeEnum.equals(CartTypeEnum.PINTUAN))
                                    || promotionGoods.getPromotionType().equals(PromotionTypeEnum.SECKILL.name()))
                    .findAny();
            // 有促销金额则用促销金额，否则用商品原价
            if (promotionOptional.isPresent()) {
                cartPrice = CurrencyUtils.add(
                        cartPrice, CurrencyUtils.mul(promotionOptional.get().getPrice(), cartSkuCO.getNum()));
                skuPrice.put(
                        cartSkuCO.getGoodsSku().getId(),
                        CurrencyUtils.mul(promotionOptional.get().getPrice(), cartSkuCO.getNum()));
            } else {
                cartPrice = CurrencyUtils.add(
                        cartPrice, CurrencyUtils.mul(cartSkuCO.getGoodsSku().getPrice(), cartSkuCO.getNum()));
                skuPrice.put(
                        cartSkuCO.getGoodsSku().getId(),
                        CurrencyUtils.mul(cartSkuCO.getGoodsSku().getPrice(), cartSkuCO.getNum()));
            }
        }

        // 如果购物车金额大于消费门槛则使用
        if (cartPrice >= memberCoupon.getConsumeThreshold()) {
            // 如果是平台优惠券
            if (memberCoupon.getIsPlatform()) {
                tradeDTO.setPlatformCoupon(new MemberCouponDTO(skuPrice, memberCoupon));
            } else {
                tradeDTO.getStoreCoupons().put(memberCoupon.getStoreId(), new MemberCouponDTO(skuPrice, memberCoupon));
            }
        }
    }

    /**
     * 获取可以使用优惠券的商品信息
     *
     * @param memberCoupon 用于计算优惠券结算详情
     * @param tradeDTO 购物车信息
     * @return 是否可以使用优惠券
     */
    private List<CartSkuCO> checkCoupon(MemberCoupon memberCoupon, TradeDTO tradeDTO) {
        List<CartSkuCO> cartSkuCOS;
        // 如果是店铺优惠券，判定的内容
        if (!memberCoupon.getIsPlatform()) {
            cartSkuCOS = tradeDTO.getSkuList().stream()
                    .filter(i -> i.getStoreId().equals(memberCoupon.getStoreId()))
                    .toList();
        }
        // 否则为平台优惠券，筛选商品为全部商品
        else {
            cartSkuCOS = tradeDTO.getSkuList();
        }

        // 当初购物车商品中是否存在符合优惠券条件的商品sku
        if (memberCoupon.getScopeType().equals(PromotionsScopeTypeEnum.ALL.name())) {
            return cartSkuCOS;
        } else if (memberCoupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS_CATEGORY.name())) {
            // 分类路径是否包含
            return cartSkuCOS.stream()
                    .filter(i -> i.getGoodsSku().getCategoryPath().indexOf("," + memberCoupon.getScopeId() + ",") <= 0)
                    .toList();
        } else if (memberCoupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS.name())) {
            // 范围关联ID是否包含
            return cartSkuCOS.stream()
                    .filter(i -> memberCoupon
                                    .getScopeId()
                                    .indexOf("," + i.getGoodsSku().getId() + ",")
                            <= 0)
                    .toList();
        } else if (memberCoupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_SHOP_CATEGORY.name())) {
            // 店铺分类路径是否包含
            return cartSkuCOS.stream()
                    .filter(i ->
                            i.getGoodsSku().getStoreCategoryPath().indexOf("," + memberCoupon.getScopeId() + ",") <= 0)
                    .toList();
        }
        return new ArrayList<>();
    }

    /**
     * 检测购物车
     *
     * @param cartTypeEnum 购物车枚举
     * @param cartSkuCO SKUVO
     * @param skuId SkuId
     * @param num 数量
     */
    private void checkCart(CartTypeEnum cartTypeEnum, CartSkuCO cartSkuCO, String skuId, Integer num) {

        this.checkSetGoodsQuantity(cartSkuCO, skuId, num);
        // 拼团判定
        if (cartTypeEnum.equals(CartTypeEnum.PINTUAN)) {
            // 砍价判定
            checkPintuan(cartSkuCO);
        } else if (cartTypeEnum.equals(CartTypeEnum.KANJIA)) {
            // 检测购物车的数量
            checkKanjia(cartSkuCO);
        } else if (cartTypeEnum.equals(CartTypeEnum.POINTS)) {
            // 检测购物车的数量
            checkPoint(cartSkuCO);
        }
    }

    /**
     * 校验拼团信息
     *
     * @param cartSkuCO 购物车信息
     */
    private void checkPintuan(CartSkuCO cartSkuCO) {
        // 拼团活动，需要对限购数量进行判定
        // 获取拼团信息
        List<PromotionGoods> currentPromotion = cartSkuCO.getPromotions().stream()
                .filter(promotionGoods -> (promotionGoods.getPromotionType().equals(PromotionTypeEnum.PINTUAN.name())))
                .toList();
        // 拼团活动判定
        if (!currentPromotion.isEmpty()) {
            PromotionGoods promotionGoods = currentPromotion.get(0);
            // 写入拼团信息
            cartSkuCO.setPintuanId(promotionGoods.getPromotionId());
            // 写入成交信息
            cartSkuCO.setUtilPrice(promotionGoods.getPrice());
            cartSkuCO.setPurchasePrice(promotionGoods.getPrice());
        } else {
            // 如果拼团活动被异常处理，则在这里安排mq重新写入商品索引
            goodsSkuApi.generateEs(goodsApi.getById(cartSkuCO.getGoodsSku().getGoodsId()));
            throw new BusinessException(ResultEnum.CART_PINTUAN_NOT_EXIST_ERROR);
        }
        // 检测拼团限购数量
        Pintuan pintuan = pintuanApi.getById(cartSkuCO.getPintuanId());
        Integer limitNum = pintuan.getLimitNum();
        if (limitNum != 0 && cartSkuCO.getNum() > limitNum) {
            throw new BusinessException(ResultEnum.CART_PINTUAN_LIMIT_ERROR);
        }
    }

    /**
     * 校验砍价信息
     *
     * @param cartSkuCO 购物车信息
     */
    private void checkKanjia(CartSkuCO cartSkuCO) {
        // 根据skuId获取砍价商品
        KanjiaActivityGoods kanjiaActivityGoodsDTO = kanjiaActivityGoodsApi.getKanjiaGoodsBySkuId(
                cartSkuCO.getGoodsSku().getId());

        // 查找当前会员的砍价商品活动
        KanjiaActivitySearchParams kanjiaActivitySearchParams = new KanjiaActivitySearchParams();
        kanjiaActivitySearchParams.setKanjiaActivityGoodsId(kanjiaActivityGoodsDTO.getId());
        kanjiaActivitySearchParams.setMemberId(UserContext.getCurrentUser().getId());
        kanjiaActivitySearchParams.setStatus(KanJiaStatusEnum.SUCCESS.name());
        KanjiaActivity kanjiaActivity = kanjiaActivityApi.getKanjiaActivity(kanjiaActivitySearchParams);

        // 校验砍价活动是否满足条件
        // 判断发起砍价活动
        if (kanjiaActivity == null) {
            throw new BusinessException(ResultEnum.KANJIA_ACTIVITY_NOT_FOUND_ERROR);
            // 判断砍价活动是否已满足条件
        } else if (!KanJiaStatusEnum.SUCCESS.name().equals(kanjiaActivity.getStatus())) {
            cartSkuCO.setKanjiaId(kanjiaActivity.getId());
            cartSkuCO.setPurchasePrice(BigDecimal.ZERO);
            throw new BusinessException(ResultEnum.KANJIA_ACTIVITY_NOT_PASS_ERROR);
        }
        // 砍价商品默认一件货物
        cartSkuCO.setKanjiaId(kanjiaActivity.getId());
        cartSkuCO.setNum(1);
    }

    /**
     * 校验积分商品信息
     *
     * @param cartSkuCO 购物车信息
     */
    private void checkPoint(CartSkuCO cartSkuCO) {
        PointsGoodsVO pointsGoodsVO = pointsGoodsApi.getPointsGoodsDetailBySkuId(
                cartSkuCO.getGoodsSku().getId());

        if (pointsGoodsVO != null) {

            if (pointsGoodsVO.getActiveStock() < 1) {
                throw new BusinessException(ResultEnum.POINT_GOODS_ACTIVE_STOCK_INSUFFICIENT);
            }
            cartSkuCO.setPoint(pointsGoodsVO.getPoints());
            cartSkuCO.setPurchasePrice(0D);
            cartSkuCO.setPointsId(pointsGoodsVO.getId());
        }
    }
}
