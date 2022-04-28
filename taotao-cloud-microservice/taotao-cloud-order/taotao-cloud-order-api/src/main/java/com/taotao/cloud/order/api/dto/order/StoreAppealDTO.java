package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单交易投诉VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:19:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单交易投诉VO")
public class StoreAppealDTO {

	@Schema(description = "投诉id")
	private Long orderComplaintId;

	@Schema(description = "申诉商家内容")
	private String appealContent;

	@Schema(description = "申诉商家上传的图片")
	private String appealImages;
}
