package com.taotao.cloud.order.biz.entity.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.SnowFlake;
import cn.lili.modules.order.cart.entity.dto.TradeDTO;
import cn.lili.modules.order.cart.entity.vo.CartSkuVO;
import cn.lili.modules.order.cart.entity.vo.CartVO;
import cn.lili.modules.order.order.entity.dto.PriceDetailDTO;
import cn.lili.modules.order.order.entity.enums.CommentStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderComplaintStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderItemAfterSaleStatusEnum;
import cn.lili.modules.promotion.entity.vos.PromotionSkuVO;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

/**
 * 子订单
 *
 * 
 * @since 2020/11/17 7:30 下午
 */
@Data
@TableName("li_order_item")
@ApiModel(value = "子订单")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    private static final long serialVersionUID = 2108971190191410182L;

    @ApiModelProperty(value = "订单编号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String orderSn;

    @ApiModelProperty(value = "子订单编号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String sn;

    @ApiModelProperty(value = "单价")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double unitPrice;

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "小记")
    private Double subTotal;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "商品ID")
    private String goodsId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "货品ID")
    private String skuId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "销售量")
    private Integer num;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "交易编号")
    private String tradeSn;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "图片")
    private String image;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "分类ID")
    private String categoryId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "快照id")
    private String snapshotId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "规格json")
    private String specs;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "促销类型")
    private String promotionType;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "促销id")
    private String promotionId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "销售金额")
    private Double goodsPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "实际金额")
    private Double flowPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    /**
     * @see CommentStatusEnum
     */
    @ApiModelProperty(value = "评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，")
    private String commentStatus;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    /**
     * @see OrderItemAfterSaleStatusEnum
     */
    @ApiModelProperty(value = "售后状态")
    private String afterSaleStatus;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "价格详情")
    private String priceDetail;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    /**
     * @see OrderComplaintStatusEnum
     */
    @ApiModelProperty(value = "投诉状态")
    private String complainStatus;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "交易投诉id")
    private String complainId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "退货商品数量")
    private Integer returnGoodsNumber;


    public OrderItem(CartSkuVO cartSkuVO, CartVO cartVO, TradeDTO tradeDTO) {
        String oldId = this.getId();
        BeanUtil.copyProperties(cartSkuVO.getGoodsSku(), this);
        BeanUtil.copyProperties(cartSkuVO.getPriceDetailDTO(), this);
        BeanUtil.copyProperties(cartSkuVO, this);
        this.setId(oldId);
        if (cartSkuVO.getPriceDetailDTO().getJoinPromotion() != null && !cartSkuVO.getPriceDetailDTO().getJoinPromotion().isEmpty()) {
            this.setPromotionType(CollUtil.join(cartSkuVO.getPriceDetailDTO().getJoinPromotion().stream().map(PromotionSkuVO::getPromotionType).collect(Collectors.toList()), ","));
            this.setPromotionId(CollUtil.join(cartSkuVO.getPriceDetailDTO().getJoinPromotion().stream().map(PromotionSkuVO::getActivityId).collect(Collectors.toList()), ","));
        }
        this.setAfterSaleStatus(OrderItemAfterSaleStatusEnum.NEW.name());
        this.setCommentStatus(CommentStatusEnum.NEW.name());
        this.setComplainStatus(OrderComplaintStatusEnum.NEW.name());
        this.setPriceDetailDTO(cartSkuVO.getPriceDetailDTO());
        this.setOrderSn(cartVO.getSn());
        this.setTradeSn(tradeDTO.getSn());
        this.setImage(cartSkuVO.getGoodsSku().getThumbnail());
        this.setGoodsName(cartSkuVO.getGoodsSku().getGoodsName());
        this.setSkuId(cartSkuVO.getGoodsSku().getId());
        this.setCategoryId(cartSkuVO.getGoodsSku().getCategoryPath().substring(
                cartSkuVO.getGoodsSku().getCategoryPath().lastIndexOf(",") + 1
        ));
        this.setGoodsPrice(cartSkuVO.getGoodsSku().getPrice());
        this.setUnitPrice(cartSkuVO.getPurchasePrice());
        this.setSubTotal(cartSkuVO.getSubTotal());
        this.setSn(SnowFlake.createStr("OI"));


    }

    public PriceDetailDTO getPriceDetailDTO() {
        return JSONUtil.toBean(priceDetail, PriceDetailDTO.class);
    }

    public void setPriceDetailDTO(PriceDetailDTO priceDetail) {
        this.priceDetail = JSONUtil.toJsonStr(priceDetail);
    }

}
