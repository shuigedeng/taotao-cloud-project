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
import java.math.BigDecimal;

/**
 * 秒杀活动申请视图对象
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillApplyVO {

	private static final long serialVersionUID = 7076774723400062602L;

	private Long id;

	@Schema(description = "活动id", required = true)
	@NotNull(message = "活动id参数不能为空")
	@Min(value = 0, message = "活动id参数异常")
	private String seckillId;

	@Schema(description = "时刻")
	@NotNull(message = "时刻参数不能为空")
	private Integer timeLine;

	@Schema(description = "skuID")
	@NotNull(message = "skuId参数不能为空")
	@Min(value = 0, message = "skuID参数异常")
	private String skuId;

	@Schema(description = "商品名称")
	@NotEmpty(message = "商品名称参数不能为空")
	private String goodsName;

	@Schema(description = "商家id")
	private String storeId;

	@Schema(description = "商家名称")
	private String storeName;

	@Schema(description = "价格")
	@NotNull(message = "价格参数不能为空")
	@Min(value = 0, message = "价格参数不能小于0")
	private BigDecimal price;

	@Schema(description = "促销数量")
	@NotNull(message = "促销数量参数不能为空")
	@Min(value = 0, message = "促销数量数不能小于0")
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
