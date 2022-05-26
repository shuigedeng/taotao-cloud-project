package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.List;

/**
 * 订单交易投诉VO
 */

@Schema(description = "订单交易投诉VO")
public record OrderComplaintVO(

	@Schema(description = "投诉对话")
	List<OrderComplaintCommunicationVO> orderComplaintCommunications,

	@Schema(description = "投诉图片")
	String[] orderComplaintImages,

	@Schema(description = "申诉商家上传的图片")
	String[] appealImagesList,

	OrderComplaintBaseVO orderComplaintBase
) {

	@Serial
	private static final long serialVersionUID = -7013465343480854816L;

}
