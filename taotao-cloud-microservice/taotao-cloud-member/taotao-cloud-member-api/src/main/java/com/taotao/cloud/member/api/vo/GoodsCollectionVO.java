package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 会员商品收藏VO
 *
 * 
 * @since 2021/1/27 10:41 上午
 */
@Schema(description = "会员商品收藏VO")
public class GoodsCollectionVO {

	@Schema(description = "id")
	private String id;

	@Schema(description = "商品ID")
	private String goodsId;

	@Schema(description = "规格ID")
	private String skuId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品图片")
	private String image;

	@Schema(description = "商品价格")
	private Double price;

	@Schema(description = "已失效")
	private String marketEnable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getMarketEnable() {
		return marketEnable;
	}

	public void setMarketEnable(String marketEnable) {
		this.marketEnable = marketEnable;
	}
}
