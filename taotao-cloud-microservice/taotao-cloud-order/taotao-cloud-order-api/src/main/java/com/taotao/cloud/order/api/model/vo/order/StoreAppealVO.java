package com.taotao.cloud.order.api.model.vo.order;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 订单交易投诉VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "订单交易投诉VO")
public record StoreAppealVO(

	@Schema(description = "投诉id")
	Long orderComplaintId,

	@Schema(description = "申诉商家内容")
	String appealContent,

	@Schema(description = "申诉商家上传的图片")
	String appealImages
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -6293102172184734928L;

}
