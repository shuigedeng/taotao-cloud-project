package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 订单交易投诉通信表
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单交易投诉通信VO")
public class OrderComplaintCommunicationBaseVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -2384351827382795547L;

	@Schema(description = "投诉id")
	private Long complainId;

	@Schema(description = "对话内容")
	private String content;

	@Schema(description = "所属，买家/卖家")
	private String owner;

	@Schema(description = "对话所属名称")
	private String ownerName;

	@Schema(description = "对话所属id,卖家id/买家id")
	private Long ownerId;


}
