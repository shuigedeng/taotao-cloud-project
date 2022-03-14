package com.taotao.cloud.order.biz.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 子订单表
 */
@Entity
@Table(name = OrderItem.TABLE_NAME)
@TableName(OrderItem.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderItem.TABLE_NAME, comment = "子订单表")
public class OrderItem extends BaseSuperEntity<OrderItem, Long> {

	public static final String TABLE_NAME = "tt_order_item";

	/**
	 * 订单编号
	 */
	@Column(name = "order_sn", nullable = false, columnDefinition = "varchar(64) not null comment '订单编号'")
	private String orderSn;

	/**
	 * 子订单编号
	 */
	@Column(name = "sn", nullable = false, columnDefinition = "varchar(64) not null comment '子订单编号'")
	private String sn;

	/**
	 * 单价
	 */
	@Column(name = "unit_price", nullable = false, columnDefinition = "varchar(64) not null comment '单价'")
	private BigDecimal unitPrice;

	/**
	 * 小记
	 */
	@Column(name = "sub_total", nullable = false, columnDefinition = "varchar(64) not null comment '小记'")
	private BigDecimal subTotal;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id", nullable = false, columnDefinition = "varchar(64) not null comment '商品ID'")
	private String goodsId;

	/**
	 * 货品ID
	 */
	@Column(name = "sku_id", nullable = false, columnDefinition = "varchar(64) not null comment '货品ID'")
	private String skuId;

	/**
	 * 销售量
	 */
	@Column(name = "num", nullable = false, columnDefinition = "varchar(64) not null comment '销售量'")
	private Integer num;

	/**
	 * 交易编号
	 */
	@Column(name = "trade_sn", nullable = false, columnDefinition = "varchar(64) not null comment '交易编号'")
	private String tradeSn;

	/**
	 * 图片
	 */
	@Column(name = "image", nullable = false, columnDefinition = "varchar(64) not null comment '图片'")
	private String image;

	/**
	 * 商品名称
	 */
	@Column(name = "goods_name", nullable = false, columnDefinition = "varchar(64) not null comment '商品名称'")
	private String goodsName;

	/**
	 * 分类ID
	 */
	@Column(name = "category_id", nullable = false, columnDefinition = "varchar(64) not null comment '分类ID'")
	private String categoryId;

	/**
	 * 快照id
	 */
	@Column(name = "snapshot_id", nullable = false, columnDefinition = "varchar(64) not null comment '快照id'")
	private String snapshotId;

	/**
	 * 规格json
	 */
	@Column(name = "specs", nullable = false, columnDefinition = "varchar(64) not null comment '规格json'")
	private String specs;

	/**
	 * 促销类型
	 */
	@Column(name = "promotion_type", nullable = false, columnDefinition = "varchar(64) not null comment '促销类型'")
	private String promotionType;

	/**
	 * 促销id
	 */
	@Column(name = "promotion_id", nullable = false, columnDefinition = "varchar(64) not null comment '促销id'")
	private String promotionId;

	/**
	 * 销售金额
	 */
	@Column(name = "goods_price", nullable = false, columnDefinition = "varchar(64) not null comment '销售金额'")
	private BigDecimal goodsPrice;

	/**
	 * 实际金额
	 */
	@Column(name = "flow_price", nullable = false, columnDefinition = "varchar(64) not null comment '实际金额'")
	private BigDecimal flowPrice;

	/**
	 * 评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，
	 *
	 * @see CommentStatusEnum
	 */
	@Column(name = "comment_status", nullable = false, columnDefinition = "varchar(64) not null comment '评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，'")
	private String commentStatus;

	/**
	 * 售后状态
	 *
	 * @see OrderItemAfterSaleStatusEnum
	 */
	@Column(name = "after_sale_status", nullable = false, columnDefinition = "varchar(64) not null comment '售后状态'")
	private String afterSaleStatus;

	/**
	 * 价格详情
	 */
	@Column(name = "price_detail", nullable = false, columnDefinition = "varchar(64) not null comment '价格详情'")
	private String priceDetail;

	/**
	 * 投诉状态
	 *
	 * @see OrderComplaintStatusEnum
	 */
	@Column(name = "complain_status", nullable = false, columnDefinition = "varchar(64) not null comment '投诉状态'")
	private String complainStatus;

	/**
	 * 交易投诉id
	 */
	@Column(name = "complain_id", nullable = false, columnDefinition = "varchar(64) not null comment '交易投诉id'")
	private String complainId;

	/**
	 * 退货商品数量
	 */
	@Column(name = "return_goods_number", nullable = false, columnDefinition = "varchar(64) not null comment '退货商品数量'")
	private Integer returnGoodsNumber;

	//public OrderItem(CartSkuVO cartSkuVO, CartVO cartVO, TradeDTO tradeDTO) {
	//    String oldId = this.getId();
	//    BeanUtil.copyProperties(cartSkuVO.getGoodsSku(), this);
	//    BeanUtil.copyProperties(cartSkuVO.getPriceDetailDTO(), this);
	//    BeanUtil.copyProperties(cartSkuVO, this);
	//    this.setId(oldId);
	//    if (cartSkuVO.getPriceDetailDTO().getJoinPromotion() != null && !cartSkuVO.getPriceDetailDTO().getJoinPromotion().isEmpty()) {
	//        this.setPromotionType(CollUtil.join(cartSkuVO.getPriceDetailDTO().getJoinPromotion().stream().map(PromotionSkuVO::getPromotionType).collect(Collectors.toList()), ","));
	//        this.setPromotionId(CollUtil.join(cartSkuVO.getPriceDetailDTO().getJoinPromotion().stream().map(PromotionSkuVO::getActivityId).collect(Collectors.toList()), ","));
	//    }
	//    this.setAfterSaleStatus(OrderItemAfterSaleStatusEnum.NEW.name());
	//    this.setCommentStatus(CommentStatusEnum.NEW.name());
	//    this.setComplainStatus(OrderComplaintStatusEnum.NEW.name());
	//    this.setPriceDetailDTO(cartSkuVO.getPriceDetailDTO());
	//    this.setOrderSn(cartVO.getSn());
	//    this.setTradeSn(tradeDTO.getSn());
	//    this.setImage(cartSkuVO.getGoodsSku().getThumbnail());
	//    this.setGoodsName(cartSkuVO.getGoodsSku().getGoodsName());
	//    this.setSkuId(cartSkuVO.getGoodsSku().getId());
	//    this.setCategoryId(cartSkuVO.getGoodsSku().getCategoryPath().substring(
	//            cartSkuVO.getGoodsSku().getCategoryPath().lastIndexOf(",") + 1
	//    ));
	//    this.setGoodsPrice(cartSkuVO.getGoodsSku().getPrice());
	//    this.setUnitPrice(cartSkuVO.getPurchasePrice());
	//    this.setSubTotal(cartSkuVO.getSubTotal());
	//    this.setSn(SnowFlake.createStr("OI"));
	//
	//
	//}
	//
	//public PriceDetailDTO getPriceDetailDTO() {
	//    return JSONUtil.toBean(priceDetail, PriceDetailDTO.class);
	//}
	//
	//public void setPriceDetailDTO(PriceDetailDTO priceDetail) {
	//    this.priceDetail = JSONUtil.toJsonStr(priceDetail);
	//}


	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getTradeSn() {
		return tradeSn;
	}

	public void setTradeSn(String tradeSn) {
		this.tradeSn = tradeSn;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public BigDecimal getFlowPrice() {
		return flowPrice;
	}

	public void setFlowPrice(BigDecimal flowPrice) {
		this.flowPrice = flowPrice;
	}

	public String getCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}

	public String getAfterSaleStatus() {
		return afterSaleStatus;
	}

	public void setAfterSaleStatus(String afterSaleStatus) {
		this.afterSaleStatus = afterSaleStatus;
	}

	public String getPriceDetail() {
		return priceDetail;
	}

	public void setPriceDetail(String priceDetail) {
		this.priceDetail = priceDetail;
	}

	public String getComplainStatus() {
		return complainStatus;
	}

	public void setComplainStatus(String complainStatus) {
		this.complainStatus = complainStatus;
	}

	public String getComplainId() {
		return complainId;
	}

	public void setComplainId(String complainId) {
		this.complainId = complainId;
	}

	public Integer getReturnGoodsNumber() {
		return returnGoodsNumber;
	}

	public void setReturnGoodsNumber(Integer returnGoodsNumber) {
		this.returnGoodsNumber = returnGoodsNumber;
	}
}
