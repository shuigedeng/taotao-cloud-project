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

package com.taotao.cloud.order.application.command.cart.dto;

import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import com.taotao.cloud.order.api.enums.cart.SuperpositionPromotionEnum;
import com.taotao.cloud.order.application.command.cart.dto.clientobject.CartCO;
import com.taotao.cloud.order.application.command.cart.dto.clientobject.CartSkuCO;
import com.taotao.cloud.order.application.command.cart.dto.clientobject.PriceDetailCO;
import com.taotao.cloud.order.sys.model.dto.order.PriceDetailDTOBuilder;
import com.taotao.cloud.order.application.model.dto.order.PriceDetailDTO;
import com.taotao.cloud.order.application.model.vo.cart.CartSkuVO;
import com.taotao.cloud.order.application.model.vo.cart.CartVO;
import com.taotao.cloud.order.application.model.vo.cart.PriceDetailVO;
import com.taotao.cloud.order.application.model.vo.order.OrderVO;
import com.taotao.cloud.order.application.model.vo.order.ReceiptVO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 购物车视图
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:17:07
 */
@Data
@Builder
@AllArgsConstructor
@Schema(description = "购物车视图")
public class TradeAddCmd implements Serializable {

    @Serial
    private static final long serialVersionUID = -3137165707807057810L;

    @Schema(description = "sn")
    private String sn;

    @Schema(description = "是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空")
    private String parentOrderSn;

    @Schema(description = "购物车列表")
    private List<CartCO> cartList;

    @Schema(description = "整笔交易中所有的规格商品")
    private List<CartSkuCO> skuList;

    @Schema(description = "购物车车计算后的总价")
    private PriceDetailCO priceDetailCO;

    @Schema(description = "购物车车计算后的总价")
    private PriceDetailDTO priceDetailDTO;

    @Schema(description = "发票信息")
    private ReceiptVO receiptVO;

    @Schema(description = "是否需要发票")
    private Boolean needReceipt;

    @Schema(description = "不支持配送方式")
    private List<CartSkuCO> notSupportFreight;

    /** 购物车类型 */
    private CartTypeEnum cartTypeEnum;

    /** 店铺备注 */
    private List<StoreRemarkAddCmd> storeRemark;

    /**
     * sku促销连线 包含满优惠
     *
     * <p>KEY值为 sku_id+"_"+SuperpositionPromotionEnum VALUE值为 对应的活动ID
     *
     * @see SuperpositionPromotionEnum
     */
    private Map<String, String> skuPromotionDetail;

    /** 使用平台优惠券，一笔订单只能使用一个平台优惠券 */
    private MemberCouponAddCmd platformCoupon;

    /** key 为商家id value 为商家优惠券 店铺优惠券 */
    private Map<String, MemberCouponAddCmd> storeCoupons;

    /** 可用优惠券列表 */
    private List<MemberCouponAddCmd> canUseCoupons;

    /** 无法使用优惠券无法使用的原因 */
    private List<MemberCouponAddCmd> cantUseCoupons;

    /** 收货地址 */
    private MemberAddressDTO memberAddress;

    /** 客户端类型 */
    private String clientType;

    /** 买家名称 */
    private String memberName;

    /** 买家id */
    private Long memberId;

    /** 分销商id */
    private Long distributionId;

    /** 订单vo */
    private List<OrderVO> orderVO;

    public TradeAddCmd(CartTypeEnum cartTypeEnum) {
        this.cartTypeEnum = cartTypeEnum;
        this.skuList = new ArrayList<>();
        this.cartList = new ArrayList<>();
        this.skuPromotionDetail = new HashMap<>();
        this.storeCoupons = new HashMap<>();
        this.storeCoupons = new HashMap<>();
        this.priceDetailDTO = PriceDetailDTOBuilder.builder().build();
        this.cantUseCoupons = new ArrayList<>();
        this.canUseCoupons = new ArrayList<>();
        this.needReceipt = false;
    }

    public TradeAddCmd() {
        this(CartTypeEnum.CART);
    }

    /** 过滤购物车中已选择的sku */
    public List<CartSkuCO> getCheckedSkuList() {
        if (skuList != null && !skuList.isEmpty()) {
            return skuList.stream().filter(CartSkuCO::checked).toList();
        }
        return skuList;
    }

    public static class MemberAddressDTO {}
}
