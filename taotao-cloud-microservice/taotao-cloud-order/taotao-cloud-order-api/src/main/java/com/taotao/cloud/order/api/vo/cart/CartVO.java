package com.taotao.cloud.order.api.vo.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;


/**
 * 购物车展示VO
 */
@Data
@Schema(description = "购物车展示VO")
public class CartVO extends CartBase implements Serializable {

	private static final long serialVersionUID = -5651775413457562422L;

	@Schema(description = "购物车中的产品列表")
	private List<CartSkuVO> skuList;

	@Schema(description = "sn")
	private String sn;

	@Schema(description = "购物车页展示时，店铺内的商品是否全选状态.1为店铺商品全选状态,0位非全选")
	private Boolean checked;

	@Schema(description = "满优惠活动")
	private FullDiscountVO fullDiscount;

	@Schema(description = "满优惠促销的商品")
	private List<String> fullDiscountSkuIds;

	@Schema(description = "是否满优惠")
	private Boolean isFull;

	@Schema(description = "使用的优惠券列表")
	private List<MemberCoupon> couponList;

	@Schema(description = "使用的优惠券列表")
	private List<CouponVO> canReceiveCoupon;

	@Schema(description = "赠品列表")
	private List<String> giftList;

	@Schema(description = "赠送优惠券列表")
	private List<String> giftCouponList;

	@Schema(description = "赠送积分")
	private Integer giftPoint;

	@Schema(description = "重量")
	private Double weight;

	@Schema(description = "购物车商品数量")
	private Integer goodsNum;

	@Schema(description = "购物车商品数量")
	private String remark;

	/**
	 * @see DeliveryMethodEnum
	 */
	@Schema(description = "配送方式")
	private String deliveryMethod;

	@Schema(description = "已参与的的促销活动提示，直接展示给客户")
	private String promotionNotice;

	public CartVO(CartSkuVO cartSkuVO) {
		this.setStoreId(cartSkuVO.getStoreId());
		this.setStoreName(cartSkuVO.getStoreName());
		this.setSkuList(new ArrayList<>());
		this.setCouponList(new ArrayList<>());
		this.setGiftList(new ArrayList<>());
		this.setGiftCouponList(new ArrayList<>());
		this.setChecked(false);
		this.isFull = false;
		this.weight = 0d;
		this.giftPoint = 0;
		this.remark = "";
	}

	public void addGoodsNum(Integer goodsNum) {
		if (this.goodsNum == null) {
			this.goodsNum = goodsNum;
		} else {
			this.goodsNum += goodsNum;
		}
	}


	/**
	 * 过滤购物车中已选择的sku
	 *
	 * @return
	 */
	public List<CartSkuVO> getCheckedSkuList() {
		if (skuList != null && !skuList.isEmpty()) {
			return skuList.stream().filter(CartSkuVO::getChecked).collect(Collectors.toList());
		}
		return skuList;
	}

}
