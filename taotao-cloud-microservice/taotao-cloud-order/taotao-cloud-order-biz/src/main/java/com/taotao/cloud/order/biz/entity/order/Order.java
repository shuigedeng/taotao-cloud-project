package com.taotao.cloud.order.biz.entity.order;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.enums.PromotionTypeEnum;
import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.goods.entity.enums.GoodsTypeEnum;
import cn.lili.modules.order.cart.entity.dto.TradeDTO;
import cn.lili.modules.order.cart.entity.enums.CartTypeEnum;
import cn.lili.modules.order.cart.entity.enums.DeliveryMethodEnum;
import cn.lili.modules.order.cart.entity.vo.CartVO;
import cn.lili.modules.order.order.entity.dto.PriceDetailDTO;
import cn.lili.modules.order.order.entity.enums.*;
import cn.lili.modules.promotion.entity.dos.PromotionGoods;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Optional;

/**
 * 订单
 *
 * 
 * @since 2020/11/17 7:30 下午
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_order")
@ApiModel(value = "订单")
@NoArgsConstructor
public class Order extends BaseEntity {


    private static final long serialVersionUID = 2233811628066468683L;
	/**
	 * 应用ID
	 */
    @ApiModelProperty("订单编号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String sn;
	/**
	 * 应用ID
	 */
    @ApiModelProperty("交易编号 关联Trade")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String tradeSn;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "店铺ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeId;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "店铺名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeName;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "会员ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String memberId;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "用户名")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String memberName;

    /**
     * @see OrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String orderStatus;

    /**
     * @see PayStatusEnum
     */
    @ApiModelProperty(value = "付款状态")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String payStatus;
    /**
     * @see DeliverStatusEnum
     */
    @ApiModelProperty(value = "货运状态")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String deliverStatus;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "第三方付款流水号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String receivableNo;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "支付方式")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String paymentMethod;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Date paymentTime;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "收件人姓名")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String consigneeName;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "收件人手机")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String consigneeMobile;

    /**
     * @see DeliveryMethodEnum
     */
    @ApiModelProperty(value = "配送方式")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String deliveryMethod;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "地址名称， '，'分割")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String consigneeAddressPath;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "地址id，'，'分割 ")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String consigneeAddressIdPath;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "详细地址")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String consigneeDetail;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "总价格")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double flowPrice;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "商品价格")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double goodsPrice;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "运费")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double freightPrice;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "优惠的金额")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double discountPrice;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "修改价格")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double updatePrice;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "发货单号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String logisticsNo;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "物流公司CODE")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String logisticsCode;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "物流公司名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String logisticsName;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "订单商品总重量")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double weight;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "商品数量")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer goodsNum;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "买家订单备注")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String remark;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "订单取消原因")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String cancelReason;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Date completeTime;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "送货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Date logisticsTime;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "支付方式返回的交易号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String payOrderNo;

    /**
     * @see ClientTypeEnum
     */
    @ApiModelProperty(value = "订单来源")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String clientType;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "是否需要发票")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Boolean needReceipt;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String parentOrderSn = "";
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "是否为某订单类型的订单，如果是则为订单类型的id，否则为空")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String promotionId;

    /**
     * @see OrderTypeEnum
     */
    @ApiModelProperty(value = "订单类型")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String orderType;

    /**
     * @see OrderPromotionTypeEnum
     */
    @ApiModelProperty(value = "订单促销类型")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String orderPromotionType;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "价格详情")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String priceDetail;

	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "订单是否支持原路退回")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Boolean canReturn;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "提货码")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String verificationCode;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "分销员ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String distributionId;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "使用的店铺会员优惠券id(,区分)")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String useStoreMemberCouponIds;
	/**
	 * 应用ID
	 */
    @ApiModelProperty(value = "使用的平台会员优惠券id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String usePlatformMemberCouponId;

    /**
     * 构建订单
     *
     * @param cartVO   购物车VO
     * @param tradeDTO 交易DTO
     */
    public Order(CartVO cartVO, TradeDTO tradeDTO) {
        String oldId = this.getId();
        BeanUtil.copyProperties(tradeDTO, this);
        BeanUtil.copyProperties(cartVO.getPriceDetailDTO(), this);
        BeanUtil.copyProperties(cartVO, this);
        //填写订单类型
        this.setTradeType(cartVO, tradeDTO);
        setId(oldId);

        //设置默认支付状态
        this.setOrderStatus(OrderStatusEnum.UNPAID.name());
        this.setPayStatus(PayStatusEnum.UNPAID.name());
        this.setDeliverStatus(DeliverStatusEnum.UNDELIVERED.name());
        this.setTradeSn(tradeDTO.getSn());
        this.setRemark(cartVO.getRemark());
        this.setFreightPrice(tradeDTO.getPriceDetailDTO().getFreightPrice());
        //会员收件信息
        this.setConsigneeAddressIdPath(tradeDTO.getMemberAddress().getConsigneeAddressIdPath());
        this.setConsigneeAddressPath(tradeDTO.getMemberAddress().getConsigneeAddressPath());
        this.setConsigneeDetail(tradeDTO.getMemberAddress().getDetail());
        this.setConsigneeMobile(tradeDTO.getMemberAddress().getMobile());
        this.setConsigneeName(tradeDTO.getMemberAddress().getName());
        //平台优惠券判定
        if (tradeDTO.getPlatformCoupon() != null) {
            this.setUsePlatformMemberCouponId(tradeDTO.getPlatformCoupon().getMemberCoupon().getId());
        }
        //店铺优惠券判定
        if (tradeDTO.getStoreCoupons() != null && !tradeDTO.getStoreCoupons().isEmpty()) {
            StringBuilder storeCouponIds = new StringBuilder();
            for (String s : tradeDTO.getStoreCoupons().keySet()) {
                storeCouponIds.append(s).append(",");
            }
            this.setUseStoreMemberCouponIds(storeCouponIds.toString());
        }

    }


    /**
     * 填写交易（订单）类型
     * 1.判断是普通、促销订单
     * 2.普通订单进行区分：实物订单、虚拟订单
     * 3.促销订单判断货物进行区分实物、虚拟商品。
     * 4.拼团订单需要填写父订单ID
     *
     * @param cartVO   购物车VO
     * @param tradeDTO 交易DTO
     */
    private void setTradeType(CartVO cartVO, TradeDTO tradeDTO) {

        //判断是否为普通订单、促销订单
        if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.CART) || tradeDTO.getCartTypeEnum().equals(CartTypeEnum.BUY_NOW)) {
            this.setOrderType(OrderTypeEnum.NORMAL.name());
        } else if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.VIRTUAL)) {
            this.setOrderType(OrderTypeEnum.VIRTUAL.name());
        } else {
            //促销订单（拼团、积分）-判断购买的是虚拟商品还是实物商品
            String goodsType = cartVO.getCheckedSkuList().get(0).getGoodsSku().getGoodsType();
            if (StrUtil.isEmpty(goodsType) || goodsType.equals(GoodsTypeEnum.PHYSICAL_GOODS.name())) {
                this.setOrderType(OrderTypeEnum.NORMAL.name());
            } else {
                this.setOrderType(OrderTypeEnum.VIRTUAL.name());
            }
            //填写订单的促销类型
            this.setOrderPromotionType(tradeDTO.getCartTypeEnum().name());

            //判断是否为拼团订单，如果为拼团订单获取拼团ID，判断是否为主订单
            if (tradeDTO.getCartTypeEnum().name().equals(PromotionTypeEnum.PINTUAN.name())) {
                Optional<String> pintuanId = cartVO.getCheckedSkuList().get(0).getPromotions().stream()
                        .filter(i -> i.getPromotionType().equals(PromotionTypeEnum.PINTUAN.name())).map(PromotionGoods::getPromotionId).findFirst();
                promotionId = pintuanId.get();
            }
        }
    }


    public PriceDetailDTO getPriceDetailDTO() {

        try {
            return JSONUtil.toBean(priceDetail, PriceDetailDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void setPriceDetailDTO(PriceDetailDTO priceDetail) {
        this.priceDetail = JSONUtil.toJsonStr(priceDetail);
    }


}
