package com.taotao.cloud.order.biz.service.cart.render.impl;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.dto.order.PriceDetailDTO;
import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnums;
import com.taotao.cloud.order.api.vo.cart.CartSkuVO;
import com.taotao.cloud.order.api.vo.cart.CartVO;
import com.taotao.cloud.order.biz.service.cart.render.CartRenderStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 佣金计算
 */
@Service
public class CommissionRender implements CartRenderStep {


    /**
     * 商品分类
     */
    @Autowired
    private CategoryService categoryService;
    /**
     * 积分商品
     */
    @Autowired
    private PointsGoodsService pointsGoodsService;
    /**
     * 砍价商品
     */
    @Autowired
    private KanjiaActivityGoodsService kanjiaActivityGoodsService;

    @Override
    public RenderStepEnums step() {
        return RenderStepEnums.PLATFORM_COMMISSION;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        buildCartPrice(tradeDTO);
    }

    /**
     * 购物车佣金计算
     *
     * @param tradeDTO 购物车展示信息
     */
    void buildCartPrice(TradeDTO tradeDTO) {
        //购物车列表
        List<CartVO> cartVOS = tradeDTO.getCartList();

        //计算购物车价格
        for (CartVO cart : cartVOS) {
            //累加价格
            for (CartSkuVO cartSkuVO : cart.getCheckedSkuList()) {

                PriceDetailDTO priceDetailDTO = cartSkuVO.getPriceDetailDTO();
                //平台佣金根据分类计算
                String categoryId = cartSkuVO.getGoodsSku().getCategoryPath()
                        .substring(cartSkuVO.getGoodsSku().getCategoryPath().lastIndexOf(",") + 1);
                if (StrUtil.isNotEmpty(categoryId)) {
                    Double commissionRate = categoryService.getById(categoryId).getCommissionRate();
                    priceDetailDTO.setPlatFormCommissionPoint(commissionRate);
                }

                //如果积分订单 积分订单，单独操作订单结算金额和商家结算字段
                if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.POINTS)) {
                    PointsGoodsVO pointsGoodsVO = pointsGoodsService.getPointsGoodsDetailBySkuId(cartSkuVO.getGoodsSku().getId());
                    priceDetailDTO.setSettlementPrice(pointsGoodsVO.getSettlementPrice());
                }
                //如果砍价订单 计算金额，单独操作订单结算金额和商家结算字段
                else if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.KANJIA)) {
                    KanjiaActivityGoods kanjiaActivityGoods = kanjiaActivityGoodsService.getKanjiaGoodsBySkuId(cartSkuVO.getGoodsSku().getId());
                    priceDetailDTO.setSettlementPrice(kanjiaActivityGoods.getSettlementPrice());
                }
            }
        }
    }


}
