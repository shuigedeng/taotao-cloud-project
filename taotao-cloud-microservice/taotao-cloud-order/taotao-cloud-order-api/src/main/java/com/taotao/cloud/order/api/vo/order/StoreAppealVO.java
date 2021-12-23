package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 订单交易投诉VO
 *
 * @since 2020/12/4
 **/
@Schema(description = "订单交易投诉VO")
public class StoreAppealVO {

	@Schema(description = "投诉id")
	private String orderComplaintId;

	@Schema(description = "申诉商家内容")
	private String appealContent;

	@Schema(description = "申诉商家上传的图片")
	private String appealImages;
}
