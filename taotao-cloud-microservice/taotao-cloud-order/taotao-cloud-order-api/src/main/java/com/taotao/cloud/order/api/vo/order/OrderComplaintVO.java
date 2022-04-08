package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.util.List;

/**
 * 订单交易投诉VO
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单交易投诉VO")
public class OrderComplaintVO extends OrderComplaintBaseVO {

	@Serial
	private static final long serialVersionUID = -7013465343480854816L;

	@Schema(description = "投诉对话")
	private List<OrderComplaintCommunicationVO> orderComplaintCommunications;

	@Schema(description = "投诉图片")
	private String[] orderComplaintImages;

	@Schema(description = "申诉商家上传的图片")
	private String[] appealImagesList;
}
