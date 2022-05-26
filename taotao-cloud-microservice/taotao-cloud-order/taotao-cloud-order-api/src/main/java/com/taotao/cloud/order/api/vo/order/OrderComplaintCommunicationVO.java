package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;

/**
 * 投诉通信VO
 */
@Schema(description = "投诉通信VO")
public record OrderComplaintCommunicationVO(
	OrderComplaintCommunicationBaseVO orderComplaintCommunicationBase
) {

	@Serial
	private static final long serialVersionUID = -8460949951683122695L;

	public OrderComplaintCommunicationVO(Long complainId, String content, String owner,
		String ownerName, Long ownerId) {
		super(complainId, content, owner, ownerName, ownerId);
	}
}
