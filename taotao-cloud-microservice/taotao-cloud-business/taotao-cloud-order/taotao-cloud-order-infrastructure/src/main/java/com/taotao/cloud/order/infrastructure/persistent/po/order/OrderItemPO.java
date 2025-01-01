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

package com.taotao.cloud.order.infrastructure.persistent.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.order.sys.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.sys.model.dto.order.PriceDetailDTO;
import com.taotao.cloud.order.sys.model.vo.cart.CartSkuVO;
import com.taotao.cloud.order.sys.model.vo.cart.CartVO;
import com.taotao.cloud.promotion.api.model.vo.PromotionSkuVO;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 子订单表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:35
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = OrderItemPO.TABLE_NAME)
@TableName(OrderItemPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderItemPO.TABLE_NAME, comment = "子订单表")
public class OrderItemPO extends BaseSuperEntity<OrderItemPO, Long> {

    public static final String TABLE_NAME = "tt_order_item";

    /** 订单编号 */
    @Column(name = "order_sn", columnDefinition = "varchar(64) not null comment '订单编号'")
    private String orderSn;

    /** 子订单编号 */
    @Column(name = "sn", columnDefinition = "varchar(64) not null comment '子订单编号'")
    private String sn;

    /** 单价 */
    @Column(name = "unit_price", columnDefinition = "varchar(64) not null comment '单价'")
    private BigDecimal unitPrice;

    /** 小记 */
    @Column(name = "sub_total", columnDefinition = "varchar(64) not null comment '小记'")
    private BigDecimal subTotal;

    /** 商品ID */
    @Column(name = "goods_id", columnDefinition = "varchar(64) not null comment '商品ID'")
    private Long goodsId;

    /** 货品ID */
    @Column(name = "sku_id", columnDefinition = "varchar(64) not null comment '货品ID'")
    private Long skuId;

    /** 销售量 */
    @Column(name = "num", columnDefinition = "varchar(64) not null comment '销售量'")
    private Integer num;

    /** 交易编号 */
    @Column(name = "trade_sn", columnDefinition = "varchar(64) not null comment '交易编号'")
    private String tradeSn;

    /** 图片 */
    @Column(name = "image", columnDefinition = "varchar(64) not null comment '图片'")
    private String image;

    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(64) not null comment '商品名称'")
    private String goodsName;

    /** 分类ID */
    @Column(name = "category_id", columnDefinition = "varchar(64) not null comment '分类ID'")
    private Long categoryId;

    /** 快照id */
    @Column(name = "snapshot_id", columnDefinition = "varchar(64) not null comment '快照id'")
    private Long snapshotId;

    /** 规格json */
    @Column(name = "specs", columnDefinition = "json not null comment '规格json'")
    private String specs;

    /** 促销类型 */
    @Column(name = "promotion_type", columnDefinition = "varchar(64) not null comment '促销类型'")
    private String promotionType;

    /** 促销id */
    @Column(name = "promotion_id", columnDefinition = "varchar(64) not null comment '促销id'")
    private Long promotionId;

    /** 销售金额 */
    @Column(name = "goods_price", columnDefinition = "varchar(64) not null comment '销售金额'")
    private BigDecimal goodsPrice;

    /** 实际金额 */
    @Column(name = "flow_price", columnDefinition = "varchar(64) not null comment '实际金额'")
    private BigDecimal flowPrice;

    /**
     * 评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，
     *
     * @see CommentStatusEnum
     */
    @Column(
            name = "comment_status",
            columnDefinition =
                    "varchar(64) not null comment" + " '评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，'")
    private String commentStatus;

    /**
     * 售后状态
     *
     * @see OrderItemAfterSaleStatusEnum
     */
    @Column(name = "after_sale_status", columnDefinition = "varchar(64) not null comment '售后状态'")
    private String afterSaleStatus;

    /** 价格详情 */
    @Column(name = "price_detail", columnDefinition = "varchar(64) not null comment '价格详情'")
    private String priceDetail;

    /**
     * 投诉状态
     *
     * @see OrderComplaintStatusEnum
     */
    @Column(name = "complain_status", columnDefinition = "varchar(64) not null comment '投诉状态'")
    private String complainStatus;

    /** 交易投诉id */
    @Column(name = "complain_id", columnDefinition = "varchar(64) not null comment '交易投诉id'")
    private Long complainId;

    /** 退货商品数量 */
    @Column(name = "return_goods_number", columnDefinition = "varchar(64) not null comment '退货商品数量'")
    private Integer returnGoodsNumber;

    public OrderItemPO(CartSkuVO cartSkuVO, CartVO cartVO, TradeDTO tradeDTO) {
        Long oldId = this.getId();
        BeanUtils.copyProperties(cartSkuVO.getGoodsSku(), this);
        BeanUtils.copyProperties(cartSkuVO.getPriceDetailDTO(), this);
        BeanUtils.copyProperties(cartSkuVO, this);
        this.setId(oldId);
        if (cartSkuVO.getPriceDetailDTO().getJoinPromotion() != null
                && !cartSkuVO.getPriceDetailDTO().getJoinPromotion().isEmpty()) {
            this.setPromotionType(CollUtil.join(
                    cartSkuVO.getPriceDetailDTO().getJoinPromotion().stream()
                            .map(PromotionSkuVO::getPromotionType)
                            .toList(),
                    ","));
            this.setPromotionId(CollUtil.join(
                    cartSkuVO.getPriceDetailDTO().getJoinPromotion().stream()
                            .map(PromotionSkuVO::getActivityId)
                            .toList(),
                    ","));
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
        this.setCategoryId(cartSkuVO
                .getGoodsSku()
                .getCategoryPath()
                .substring(cartSkuVO.getGoodsSku().getCategoryPath().lastIndexOf(",") + 1));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderItemPO orderItemPO = (OrderItemPO) o;
        return getId() != null && Objects.equals(getId(), orderItemPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
