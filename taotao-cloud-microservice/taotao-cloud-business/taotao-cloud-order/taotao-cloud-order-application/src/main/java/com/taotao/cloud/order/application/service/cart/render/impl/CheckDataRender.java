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

package com.taotao.cloud.order.application.service.cart.render.impl;

import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.order.application.service.cart.render.ICartRenderStep;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 商品有效性校验
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:37
 */
@AllArgsConstructor
@Service
public class CheckDataRender implements ICartRenderStep {

    private final IFeignGoodsSkuApi goodsSkuApi;

    private final IOrderService orderService;

    private final IFeignPintuanApi pintuanApi;

    private final IFeignMemberApi memberApi;

    private final PointsGoodsService pointsGoodsService;

    @Override
    public RenderStepEnum step() {
        return RenderStepEnum.CHECK_DATA;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        // 预校验
        preCalibration(tradeDTO);

        // 校验商品有效性
        checkData(tradeDTO);

        // 店铺分组数据初始化
        groupStore(tradeDTO);
    }

    /**
     * 校验商品属性
     *
     * @param tradeDTO 购物车视图
     * @since 2022-04-28 08:52:11
     */
    private void checkData(TradeDTO tradeDTO) {
        List<CartSkuVO> cartSkuVOS = tradeDTO.getSkuList();

        // 循环购物车中的商品
        for (CartSkuVO cartSkuVO : cartSkuVOS) {

            // 如果失效，确认sku为未选中状态
            if (Boolean.TRUE.equals(cartSkuVO.getInvalid())) {
                // 设置购物车未选中
                cartSkuVO.setChecked(false);
            }

            // 缓存中的商品信息
            GoodsSku dataSku =
                    goodsSkuApi.getGoodsSkuByIdFromCache(cartSkuVO.getGoodsSku().getId());
            Map<String, Object> promotionMap = promotionGoodsService.getCurrentGoodsPromotion(
                    dataSku, tradeDTO.getCartTypeEnum().name());
            // 商品有效性判定
            if (dataSku == null
                    || dataSku.getUpdateTime().after(cartSkuVO.getGoodsSku().getUpdateTime())) {
                // 商品失效,将商品移除并重新填充商品
                cartSkuVOS.remove(cartSkuVO);
                // 设置新商品
                CartSkuVO newCartSkuVO = new CartSkuVO(dataSku, promotionMap);
                newCartSkuVO.setCartType(tradeDTO.getCartTypeEnum());
                newCartSkuVO.setNum(cartSkuVO.getNum());
                newCartSkuVO.setSubTotal(CurrencyUtil.mul(newCartSkuVO.getPurchasePrice(), cartSkuVO.getNum()));
                cartSkuVOS.add(newCartSkuVO);
                continue;
            }
            // 商品上架状态判定
            if (!GoodsAuthEnum.PASS.name().equals(dataSku.getAuthFlag())
                    || !GoodsStatusEnum.UPPER.name().equals(dataSku.getMarketEnable())) {
                // 设置购物车未选中
                cartSkuVO.setChecked(false);
                // 设置购物车此sku商品已失效
                cartSkuVO.setInvalid(true);
                // 设置失效消息
                cartSkuVO.setErrorMessage("商品已下架");
                continue;
            }
            // 商品库存判定
            if (dataSku.getQuantity() < cartSkuVO.getNum()) {
                // 设置购物车未选中
                cartSkuVO.setChecked(false);
                // 设置失效消息
                cartSkuVO.setErrorMessage("商品库存不足,现有库存数量[" + dataSku.getQuantity() + "]");
            }
            // 如果存在商品促销活动，则判定商品促销状态
            if (!cartSkuVO.getCartType().equals(CartTypeEnum.POINTS)
                    && (CollUtil.isNotEmpty(cartSkuVO.getNotFilterPromotionMap())
                            || Boolean.TRUE.equals(cartSkuVO.getGoodsSku().getPromotionFlag()))) {
                // 获取当前最新的促销信息
                cartSkuVO.setPromotionMap(this.promotionGoodsService.getCurrentGoodsPromotion(
                        cartSkuVO.getGoodsSku(), tradeDTO.getCartTypeEnum().name()));
                // 设定商品价格
                Double goodsPrice = cartSkuVO.getGoodsSku().getPromotionFlag() != null
                                && cartSkuVO.getGoodsSku().getPromotionFlag()
                        ? cartSkuVO.getGoodsSku().getPromotionPrice()
                        : cartSkuVO.getGoodsSku().getPrice();
                cartSkuVO.setPurchasePrice(goodsPrice);
                cartSkuVO.setUtilPrice(goodsPrice);
                cartSkuVO.setSubTotal(CurrencyUtil.mul(cartSkuVO.getPurchasePrice(), cartSkuVO.getNum()));
            }
        }
    }

    /**
     * 店铺分组
     *
     * @param tradeDTO tradeDTO
     */
    private void groupStore(TradeDTO tradeDTO) {
        // 渲染的购物车
        List<CartVO> cartList = new ArrayList<>();

        // 根据店铺分组
        Map<String, List<CartSkuVO>> storeCollect =
                tradeDTO.getSkuList().parallelStream().collect(Collectors.groupingBy(CartSkuVO::getStoreId));
        for (Map.Entry<String, List<CartSkuVO>> storeCart : storeCollect.entrySet()) {
            if (!storeCart.getValue().isEmpty()) {
                CartVO cartVO = new CartVO(storeCart.getValue().get(0));
                if (CharSequenceUtil.isEmpty(cartVO.getDeliveryMethod())) {
                    cartVO.setDeliveryMethod(DeliveryMethodEnum.LOGISTICS.name());
                }
                cartVO.setSkuList(storeCart.getValue());
                storeCart.getValue().stream()
                        .filter(i -> Boolean.TRUE.equals(i.getChecked()))
                        .findFirst()
                        .ifPresent(cartSkuVO -> cartVO.setChecked(true));
                cartList.add(cartVO);
            }
        }
        tradeDTO.setCartList(cartList);
    }

    /**
     * 订单预校验 1、自己拼团自己创建都拼团判定、拼团限购 2、积分购买，积分足够与否
     *
     * @param tradeDTO tradeDTO
     */
    private void preCalibration(TradeDTO tradeDTO) {

        // 拼团订单预校验
        if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.PINTUAN)) {
            // 拼团判定，不能参与自己创建的拼团
            if (tradeDTO.getParentOrderSn() != null) {
                // 订单接收
                Order parentOrder = orderService.getBySn(tradeDTO.getParentOrderSn());
                // 参与活动判定
                if (parentOrder
                        .getMemberId()
                        .equals(UserContext.getCurrentUser().getId())) {
                    throw new BusinessException(ResultEnum.PINTUAN_JOIN_ERROR);
                }
            }
            // 判断拼团商品的限购数量
            Optional<String> pintuanId = tradeDTO.getSkuList().get(0).getPromotions().stream()
                    .filter(i -> i.getPromotionType().equals(PromotionTypeEnum.PINTUAN.name()))
                    .map(PromotionGoods::getPromotionId)
                    .findFirst();
            if (pintuanId.isPresent()) {
                Pintuan pintuan = pintuanApi.getById(pintuanId.get());
                Integer limitNum = pintuan.getLimitNum();
                for (CartSkuVO cartSkuVO : tradeDTO.getSkuList()) {
                    if (limitNum != 0 && cartSkuVO.getNum() > limitNum) {
                        throw new BusinessException(ResultEnum.PINTUAN_LIMIT_NUM_ERROR);
                    }
                }
            }
            // 积分商品，判断用户积分是否满足
        } else if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.POINTS)) {
            String skuId = tradeDTO.getSkuList().get(0).getGoodsSku().getId();
            // 获取积分商品VO
            PointsGoodsVO pointsGoodsVO = pointsGoodsService.getPointsGoodsDetailBySkuId(skuId);
            if (pointsGoodsVO == null) {
                throw new BusinessException(ResultEnum.POINT_GOODS_ERROR);
            }
            Member member = memberApi.getUserInfo();
            if (member.getPoint() < pointsGoodsVO.getPoints()) {
                throw new BusinessException(ResultEnum.USER_POINTS_ERROR);
            }
        }
    }
}
