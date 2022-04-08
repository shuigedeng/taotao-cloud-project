package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;

/**
 * 投诉通信VO
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@Schema(description = "投诉通信VO")
@NoArgsConstructor
public class OrderComplaintCommunicationVO extends OrderComplaintCommunicationBaseVO {

	@Serial
	private static final long serialVersionUID = -8460949951683122695L;

	public OrderComplaintCommunicationVO(String complainId, String content, String owner,
										 String ownerName, Long ownerId) {
		super(complainId, content, owner, ownerName, ownerId);
	}
}
