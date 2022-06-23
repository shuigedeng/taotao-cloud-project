package com.taotao.cloud.promotion.api.web.vo.kanjia;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 砍价商品视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsVO implements Serializable {

	//@Schema(description =  "商品规格详细信息")
	//private GoodsSku goodsSku;

	@Schema(description = "最低购买金额")
	private BigDecimal purchasePrice;

	public BigDecimal getPurchasePrice() {
//		if (purchasePrice < 0) {
//			return 0D;
//		}
//		return purchasePrice;
		return BigDecimal.ZERO;
	}

	@Schema(description = "活动库存")
	private Integer stock;

}
