package com.taotao.cloud.order.api.vo.cart;

import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 购物车中的产品
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "购物车中的产品")
public class CartSkuVO extends CartBase implements Serializable {


	private static final long serialVersionUID = -894598033321906974L;


	private String sn;
	/**
	 * 对应的sku DO
	 */
	private GoodsSku goodsSku;

	/**
	 * 分销描述
	 */
	private DistributionGoods distributionGoods;

	@Schema(description = "购买数量")
	private Integer num;

	@Schema(description = "购买时的成交价")
	private Double purchasePrice;

	@Schema(description = "小记")
	private Double subTotal;

	@Schema(description = "小记")
	private Double utilPrice;
	/**
	 * 是否选中，要去结算 0:未选中 1:已选中，默认
	 */
	@Schema(description = "是否选中，要去结算")
	private Boolean checked;

	@Schema(description = "是否免运费")
	private Boolean isFreeFreight;

	@Schema(description = "是否失效 ")
	private Boolean invalid;

	@Schema(description = "购物车商品错误消息")
	private String errorMessage;

	@Schema(description = "是否可配送")
	private Boolean isShip;

	@Schema(description = "拼团id 如果是拼团购买 此值为拼团活动id，" +
		"当pintuanId为空，则表示普通购买（或者拼团商品，单独购买）")
	private String pintuanId;

	@Schema(description = "砍价ID")
	private String kanjiaId;

	@Schema(description = "积分兑换ID")
	private String pointsId;

	@Schema(description = "积分购买 积分数量")
	private Long point;

	@Schema(description = "可参与的单品活动")
	private List<PromotionGoods> promotions;

	@Schema(description = "参与促销活动更新时间(一天更新一次) 例如时间为：2020-01-01  00：00：01")
	private Date updatePromotionTime;

	/**
	 * @see CartTypeEnum
	 */
	@Schema(description = "购物车类型")
	private CartTypeEnum cartType;

	/**
	 * 在构造器里初始化促销列表，规格列表
	 */
	public CartSkuVO(GoodsSku goodsSku) {
		this.goodsSku = goodsSku;
		this.checked = true;
		this.invalid = false;
		//默认时间为0，让系统为此商品更新缓存
		this.updatePromotionTime = new Date(0);
		this.errorMessage = "";
		this.isShip = true;
		this.purchasePrice = goodsSku.getIsPromotion() != null && goodsSku.getIsPromotion()
			? goodsSku.getPromotionPrice() : goodsSku.getPrice();
		this.isFreeFreight = false;
		this.utilPrice = 0D;
		this.setStoreId(goodsSku.getStoreId());
		this.setStoreName(goodsSku.getStoreName());
	}
}
