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

package com.taotao.cloud.order.infrastructure.roketmq.event.impl;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.sys.model.message.OrderMessage;
import com.taotao.cloud.order.sys.model.vo.order.OrderDetailVO;
import com.taotao.cloud.order.sys.model.vo.order.OrderItemVO;
import com.taotao.cloud.order.infrastructure.model.entity.order.OrderItem;
import com.taotao.cloud.order.infrastructure.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.service.business.order.IOrderService;
import com.taotao.cloud.promotion.api.feign.IFeignKanjiaActivityApi;
import com.taotao.cloud.promotion.api.feign.IFeignKanjiaActivityGoodsApi;
import com.taotao.cloud.promotion.api.feign.IFeignPointsGoodsApi;
import com.taotao.cloud.promotion.api.feign.IFeignPromotionGoodsApi;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityGoodsDTO;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.api.model.vo.PromotionGoodsVO;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

/**
 * 库存扣减，他表示了订单状态是否出库成功
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 15:04:31
 */
@Service
public class StockUpdateExecute implements OrderStatusChangeEvent {

    /** 出库失败消息 */
    static String outOfStockMessage = "库存不足，出库失败";
    /** Redis */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript<Boolean> quantityScript;
    /** 订单 */
    @Autowired
    private IOrderService orderService;
    /** 规格商品 */
    @Autowired
    private IFeignGoodsSkuApi goodsSkuApi;
    /** 促销商品 */
    @Autowired
    private IFeignPromotionGoodsApi promotionGoodsApi;
    /** 缓存 */
    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private IFeignKanjiaActivityApi kanjiaActivityApi;

    @Autowired
    private IFeignKanjiaActivityGoodsApi kanjiaActivityGoodsApi;

    @Autowired
    private IFeignPointsGoodsApi pointsGoodsApi;

    @Override
    public void orderChange(OrderMessage orderMessage) {
        switch (orderMessage.newStatus()) {
            case PAID -> {
                // 获取订单详情
                OrderDetailVO order = orderService.queryDetail(orderMessage.orderSn());
                // 库存key 和 扣减数量
                List<String> keys = new ArrayList<>();
                List<String> values = new ArrayList<>();
                for (OrderItemVO orderItem : order.orderItems()) {
                    keys.add(GoodsSkuService.getStockCacheKey(orderItem.getSkuId()));
                    int i = -orderItem.getNum();
                    values.add(Integer.toString(i));
                    setPromotionStock(keys, values, orderItem);
                }

                List<Integer> stocks = redisRepository.mGet(keys);
                // 如果缓存中不存在存在等量的库存值，则重新写入缓存，防止缓存击穿导致无法下单
                checkStocks(stocks, order);

                // 库存扣除结果
                Boolean skuResult = stringRedisTemplate.execute(quantityScript, keys, values.toArray());
                // 如果库存扣减都成功，则记录成交订单
                if (Boolean.TRUE.equals(skuResult)) {
                    LogUtils.info("库存扣减成功,参数为{};{}", keys, values);
                    // 库存确认之后对结构处理
                    orderService.afterOrderConfirm(orderMessage.orderSn());
                    // 成功之后，同步库存
                    synchroDB(order);
                } else {
                    LogUtils.info("库存扣件失败，变更缓存key{} 变更缓存value{}", keys, values);
                    // 失败之后取消订单
                    this.errorOrder(orderMessage.orderSn());
                }
            }
            case CANCELLED -> {
                // 获取订单详情
                OrderDetailVO order = orderService.queryDetail(orderMessage.orderSn());
                // 判定是否已支付 并且 非库存不足导致库存回滚 则需要考虑订单库存返还业务
                if (order.order().payStatus().equals(PayStatusEnum.PAID.name())
                        && !order.order().cancelReason().equals(outOfStockMessage)) {
                    // 库存key 和 还原数量
                    List<String> keys = new ArrayList<>();
                    List<String> values = new ArrayList<>();

                    // 返还商品库存，促销库存不与返还，不然前台展示层有展示逻辑错误
                    for (OrderItemVO orderItem : order.orderItems()) {
                        keys.add(GoodsSkuService.getStockCacheKey(orderItem.getSkuId()));
                        int i = orderItem.getNum();
                        values.add(Integer.toString(i));
                    }
                    // 批量脚本执行库存回退
                    Boolean skuResult = stringRedisTemplate.execute(quantityScript, keys, values.toArray());

                    // 返还失败，则记录日志
                    if (Boolean.FALSE.equals(skuResult)) {
                        LogUtils.error("库存回退异常，keys：{},回复库存值为: {}", keys, values);
                    }
                    rollbackOrderStock(order);
                }
                break;
            }
            default -> {}
        }
    }

    /**
     * 校验库存是否有效
     *
     * @param stocks stocks
     */
    private void checkStocks(List<Integer> stocks, OrderDetailVO order) {
        if (order.getOrderItems().size() == stocks.size()) {
            return;
        }
        initSkuCache(order.getOrderItems());
        initPromotionCache(order.getOrderItems());
    }

    /**
     * 缓存中sku库存值不存在时，将不存在的信息重新写入一边
     *
     * @param orderItems
     */
    private void initSkuCache(List<OrderItemVO> orderItems) {
        orderItems.forEach(orderItem -> {
            // 如果不存在
            if (!redisRepository.hasKey(GoodsSkuService.getStockCacheKey(orderItem.getSkuId()))) {
                // 内部会自动写入，这里不需要进行二次处理
                goodsSkuApi.getStock(orderItem.getSkuId());
            }
        });
    }

    /**
     * 初始化促销商品缓存
     *
     * @param orderItems orderItems
     */
    private void initPromotionCache(List<OrderItemVO> orderItems) {
        // 如果促销类型需要库存判定，则做对应处理
        orderItems.forEach(orderItem -> {
            if (orderItem.getPromotionType() != null) {
                // 如果此促销有库存概念，则计入
                if (PromotionTypeEnum.haveStock(orderItem.getPromotionType())) {
                    PromotionTypeEnum promotionTypeEnum = PromotionTypeEnum.valueOf(orderItem.getPromotionType());
                    String cacheKey = PromotionGoodsService.getPromotionGoodsStockCacheKey(
                            promotionTypeEnum, orderItem.getPromotionId(), orderItem.getSkuId());

                    switch (promotionTypeEnum) {
                        case KANJIA -> {
                            redisRepository.set(
                                    cacheKey,
                                    kanjiaActivityGoodsApi
                                            .getKanjiaGoodsBySkuId(orderItem.getSkuId())
                                            .getStock());
                        }
                        case POINTS_GOODS -> {
                            redisRepository.set(
                                    cacheKey,
                                    pointsGoodsApi
                                            .getPointsGoodsDetailBySkuId(orderItem.getSkuId())
                                            .getActiveStock());
                        }
                        case SECKILL, PINTUAN -> {
                            redisRepository.set(
                                    cacheKey,
                                    promotionGoodsApi.getPromotionGoodsStock(
                                            promotionTypeEnum, orderItem.getPromotionId(), orderItem.getSkuId()));
                        }
                        default -> {}
                    }
                }
            }
        });
    }

    /**
     * 订单出库失败
     *
     * @param orderSn 失败入库订单信息
     */
    private void errorOrder(String orderSn) {
        orderService.systemCancel(orderSn, outOfStockMessage);
    }

    /**
     * 写入需要更改促销库存的商品
     *
     * @param keys 缓存key值
     * @param values 缓存value值
     * @param sku 购物车信息
     */
    private void setPromotionStock(List<String> keys, List<String> values, OrderItem sku) {
        if (sku.getPromotionType() != null) {
            // 如果此促销有库存概念，则计入
            if (!PromotionTypeEnum.haveStock(sku.getPromotionType())) {
                return;
            }
            PromotionTypeEnum promotionTypeEnum = PromotionTypeEnum.valueOf(sku.getPromotionType());
            keys.add(PromotionGoodsService.getPromotionGoodsStockCacheKey(
                    promotionTypeEnum, sku.getPromotionId(), sku.getSkuId()));
            int i = -sku.getNum();
            values.add(Integer.toString(i));
        }
    }

    /**
     * 同步库存和促销库存
     *
     * <p>需修改：DB：商品库存、Sku商品库存、活动商品库存 1.获取需要修改的Sku列表、活动商品列表 2.写入sku商品库存，批量修改 3.写入促销商品的卖出数量、剩余数量,批量修改
     * 4.调用方法修改商品库存
     *
     * @param order 订单
     */
    private void synchroDB(OrderDetailVO order) {
        // sku商品
        List<GoodsSkuSpecGalleryVO> goodsSkus = new ArrayList<>();
        // 促销商品
        List<PromotionGoodsVO> promotionGoods = new ArrayList<>();
        // sku库存key 集合
        List<String> skuKeys = new ArrayList<>();
        // 促销库存key 集合
        List<String> promotionKey = new ArrayList<>();

        // 循环订单
        for (OrderItemVO orderItem : order.getOrderItems()) {
            skuKeys.add(GoodsSkuService.getStockCacheKey(orderItem.getSkuId()));

            GoodsSkuSpecGalleryVO goodsSku = new GoodsSkuSpecGalleryVO();
            goodsSku.setId(orderItem.getSkuId());
            goodsSku.setGoodsId(orderItem.getGoodsId());
            // 如果有促销信息
            if (null != orderItem.getPromotionType()
                    && null != orderItem.getPromotionId()
                    && PromotionTypeEnum.haveStock(orderItem.getPromotionType())) {
                // 如果促销有库存信息
                PromotionTypeEnum promotionTypeEnum = PromotionTypeEnum.valueOf(orderItem.getPromotionType());

                // 修改砍价商品库存
                if (promotionTypeEnum.equals(PromotionTypeEnum.KANJIA)) {
                    KanjiaActivityVO kanjiaActivity = kanjiaActivityApi.getById(orderItem.getPromotionId());
                    KanjiaActivityGoodsDTO kanjiaActivityGoodsDTO =
                            kanjiaActivityGoodsApi.getKanjiaGoodsDetail(kanjiaActivity.getKanjiaActivityGoodsId());

                    Integer stock = Integer.parseInt(redisRepository
                            .get(PromotionGoodsService.getPromotionGoodsStockCacheKey(
                                    promotionTypeEnum, orderItem.getPromotionId(), orderItem.getSkuId()))
                            .toString());
                    kanjiaActivityGoodsDTO.setStock(stock);

                    kanjiaActivityGoodsApi.updateById(kanjiaActivityGoodsDTO);
                    // 修改积分商品库存
                } else if (promotionTypeEnum.equals(PromotionTypeEnum.POINTS_GOODS)) {
                    PointsGoodsVO pointsGoodsVO = pointsGoodsApi.getPointsGoodsDetail(orderItem.getPromotionId());
                    Integer stock = Integer.parseInt(redisRepository
                            .get(PromotionGoodsService.getPromotionGoodsStockCacheKey(
                                    promotionTypeEnum, orderItem.getPromotionId(), orderItem.getSkuId()))
                            .toString());
                    pointsGoodsVO.setActiveStock(stock);
                    pointsGoodsApi.updateById(pointsGoodsVO);
                } else {
                    PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
                    searchParams.setPromotionType(promotionTypeEnum.name());
                    searchParams.setPromotionId(orderItem.getPromotionId());
                    searchParams.setSkuId(orderItem.getSkuId());
                    PromotionGoodsVO pGoods = promotionGoodsApi.getPromotionsGoods(searchParams);
                    // 记录需要更新的促销库存信息
                    promotionKey.add(PromotionGoodsService.getPromotionGoodsStockCacheKey(
                            promotionTypeEnum, orderItem.getPromotionId(), orderItem.getSkuId()));
                    if (pGoods != null) {
                        promotionGoods.add(pGoods);
                    }
                }
            }
            goodsSkus.add(goodsSku);
        }

        // 批量获取商品库存
        List<GoodsSkuSpecGalleryVO> skuStocks = redisRepository.mGet(skuKeys);
        // 循环写入商品库存
        for (int i = 0; i < skuStocks.size(); i++) {
            goodsSkus.get(i).setQuantity(Convert.toInt(skuStocks.get(i).toString()));
        }
        // 批量修改商品库存
        goodsSkuApi.updateBatchById(goodsSkus);

        // 促销库存处理
        if (!promotionKey.isEmpty()) {
            List<PromotionGoodsVO> promotionStocks = redisRepository.mGet(promotionKey);
            for (int i = 0; i < promotionKey.size(); i++) {
                promotionGoods
                        .get(i)
                        .setQuantity(Convert.toInt(promotionStocks.get(i).toString()));
                Integer num = promotionGoods.get(i).getNum();
                promotionGoods
                        .get(i)
                        .setNum((num != null ? num : 0) + order.getOrder().getGoodsNum());
            }
            promotionGoodsApi.updateBatchById(promotionGoods);
        }
        // 商品库存，包含sku库存集合，批量更新商品库存相关
        goodsSkuApi.updateGoodsStuck(goodsSkus);

        LogUtils.info("订单确认，库存同步：商品信息--{}；促销信息---{}", goodsSkus, promotionGoods);
    }

    /**
     * 恢复商品库存
     *
     * @param order 订单
     */
    private void rollbackOrderStock(OrderDetailVO order) {
        // sku商品
        List<GoodsSkuSpecGalleryVO> goodsSkus = new ArrayList<>();
        // sku库存key 集合
        List<String> skuKeys = new ArrayList<>();
        // 循环订单
        for (OrderItemVO orderItem : order.getOrderItems()) {
            skuKeys.add(GoodsSkuService.getStockCacheKey(orderItem.getSkuId()));
            GoodsSkuSpecGalleryVO goodsSku = new GoodsSkuSpecGalleryVO();
            goodsSku.setId(orderItem.getSkuId());
            goodsSkus.add(goodsSku);
        }
        // 批量获取商品库存
        List<GoodsSkuSpecGalleryVO> skuStocks = redisRepository.mGet(skuKeys);
        // 循环写入商品SKU库存
        for (int i = 0; i < skuStocks.size(); i++) {
            goodsSkus.get(i).setQuantity(Convert.toInt(skuStocks.get(i).toString()));
        }
        LogUtils.info("订单取消，库存还原：{}", goodsSkus);

        // 批量修改商品库存
        goodsSkuApi.updateBatchById(goodsSkus);
        goodsSkuApi.updateGoodsStuck(goodsSkus);
    }
}
