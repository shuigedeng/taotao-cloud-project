package com.taotao.cloud.promotion.api.vo;

import com.taotao.cloud.promotion.api.enums.PromotionsApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 秒杀活动申请视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillApplyVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7076774723400062602L;

	private Long id;

	@Schema(description = "活动id", required = true)
	private Long seckillId;

	@Schema(description = "时刻")
	private Integer timeLine;

	@Schema(description = "skuID")
	private Long skuId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商家id")
	private Long storeId;

	@Schema(description = "商家名称")
	private String storeName;

	@Schema(description = "价格")
	private BigDecimal price;

	@Schema(description = "促销数量")
	private Integer quantity;

	/**
	 * @see PromotionsApplyStatusEnum
	 */
	@Schema(description = "APPLY(\"申请\"), PASS(\"通过\"), REFUSE(\"拒绝\")")
	private String promotionApplyStatus;

	@Schema(description = "驳回原因")
	private String failReason;

	@Schema(description = "已售数量")
	private Integer salesNum;

	@Schema(description = "商品原始价格")
	private BigDecimal originalPrice;

}
