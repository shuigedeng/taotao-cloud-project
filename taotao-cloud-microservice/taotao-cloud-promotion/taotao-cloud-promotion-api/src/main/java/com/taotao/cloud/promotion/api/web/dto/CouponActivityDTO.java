package com.taotao.cloud.promotion.api.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import lombok.experimental.SuperBuilder;

/**
 * 优惠券活动DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityDTO extends CouponActivityBaseDTO {

	@Schema(description = "优惠券列表")
	private List<CouponActivityItemDTO> couponActivityItems;

	@Schema(description = "会员列表")
	private List<MemberDTO> members;
}
