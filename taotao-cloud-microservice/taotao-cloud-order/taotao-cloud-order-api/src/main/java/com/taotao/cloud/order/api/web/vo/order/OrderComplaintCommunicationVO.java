package com.taotao.cloud.order.api.web.vo.order;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 投诉通信VO
 */
@RecordBuilder
@Schema(description = "投诉通信VO")
public record OrderComplaintCommunicationVO(
	@Schema(description = "投诉通信")
	OrderComplaintCommunicationBaseVO orderComplaintCommunicationBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -8460949951683122695L;

	// public OrderComplaintCommunicationVO(Long complainId, String content, String owner,
	// 	String ownerName, Long ownerId) {
	// 	super(complainId, content, owner, ownerName, ownerId);
	// }
}
