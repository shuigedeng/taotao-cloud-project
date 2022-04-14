package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	public OrderComplaintCommunicationVO(Long complainId, String content, String owner,
		String ownerName, Long ownerId) {
		super(complainId, content, owner, ownerName, ownerId);
	}
}
