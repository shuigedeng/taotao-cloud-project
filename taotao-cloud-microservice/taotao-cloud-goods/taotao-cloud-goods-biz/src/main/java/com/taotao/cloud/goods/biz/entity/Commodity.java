package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 小程序直播商品表
 */
@Entity
@Table(name = Commodity.TABLE_NAME)
@TableName(Commodity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Commodity.TABLE_NAME, comment = "小程序直播商品表")
public class Commodity extends BaseSuperEntity<Commodity, Long> {

	public static final String TABLE_NAME = "tt_commodity";
	/**
	 * 图片
	 */
	@Column(name = "goods_image", nullable = false, columnDefinition = "varchar(64) not null comment '图片'")
	private String goodsImage;

	/**
	 * 商品名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(64) not null comment '商品名称'")
	private String name;

	/**
	 * 1：一口价（只需要传入price，price2不传） 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
	 * 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传
	 */
	@Column(name = "price_type", nullable = false, columnDefinition = "int not null comment '价格类型'")
	private Integer priceType;

	/**
	 * 价格
	 */
	@Column(name = "price", nullable = false, columnDefinition = "decimal(10,2) not null comment '价格'")
	private BigDecimal price;

	/**
	 * 价格2
	 */
	@Column(name = "price2", nullable = false, columnDefinition = "decimal(10,2) not null comment '价格2'")
	private Double price2;

	/**
	 * 商品详情页的小程序路径
	 */
	@Column(name = "url", nullable = false, columnDefinition = "varchar(64) not null comment '商品详情页的小程序路径'")
	private String url;

	/**
	 * 微信程序直播商品ID
	 */
	@Column(name = "live_goods_id", nullable = false, columnDefinition = "varchar(64) not null comment '微信程序直播商品ID'")
	private Integer liveGoodsId;

	/**
	 * 审核单ID
	 */
	@Column(name = "audit_id", nullable = false, columnDefinition = "varchar(64) not null comment '审核单ID'")
	private String auditId;

	/**
	 * 审核状态
	 */
	@Column(name = "audit_status", nullable = false, columnDefinition = "varchar(64) not null comment '审核状态'")
	private String auditStatus;

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(64) not null comment '店铺ID'")
	private String storeId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id", nullable = false, columnDefinition = "varchar(64) not null comment '商品ID'")
	private String goodsId;

	/**
	 * skuId
	 */
	@Column(name = "sku_id", nullable = false, columnDefinition = "varchar(64) not null comment 'skuId'")
	private String skuId;

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Double getPrice2() {
		return price2;
	}

	public void setPrice2(Double price2) {
		this.price2 = price2;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getLiveGoodsId() {
		return liveGoodsId;
	}

	public void setLiveGoodsId(Integer liveGoodsId) {
		this.liveGoodsId = liveGoodsId;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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
}
