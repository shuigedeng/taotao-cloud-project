package com.taotao.cloud.order.api.vo.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 满额活动VO
 */
@Data
@Schema(description = "满额活动VO")
public class FullDiscountVO extends FullDiscount {

	private static final long serialVersionUID = -2330552735874105354L;

	/**
	 * 促销关联的商品
	 */
	private List<PromotionGoods> promotionGoodsList;

	/**
	 * 赠品信息
	 */
	private GoodsSku giftSku;

	/**
	 * 参与商品，为-1则代表所有商品参加
	 */
	private Integer number;

	public FullDiscountVO(FullDiscount fullDiscount) {
		BeanUtils.copyProperties(fullDiscount, this);
	}

	public String notice() {
		StringBuilder stringBuffer = new StringBuilder();
		if (Boolean.TRUE.equals(this.getIsFullMinus())) {
			stringBuffer.append(" 减").append(this.getFullMinus()).append("元 ");
		}
		if (Boolean.TRUE.equals(this.getIsFullRate())) {
			stringBuffer.append(" 打").append(this.getFullRate()).append("折 ");
		}

		if (Boolean.TRUE.equals(this.getIsFreeFreight())) {
			stringBuffer.append(" 免运费 ");
		}

		if (Boolean.TRUE.equals(this.getIsPoint())) {
			stringBuffer.append(" 赠").append(this.getPoint()).append("积分 ");
		}
		if (Boolean.TRUE.equals(this.getIsCoupon())) {
			stringBuffer.append(" 赠").append("优惠券 ");
		}
		if (Boolean.TRUE.equals(this.getIsGift() && giftSku != null)) {
			stringBuffer.append(" 赠品[").append(giftSku.getGoodsName()).append("]");
		}

		return stringBuffer.toString();
	}

}
