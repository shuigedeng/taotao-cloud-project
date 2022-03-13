package com.taotao.cloud.order.api.vo.order;

import cn.lili.modules.order.order.entity.dos.OrderComplaintCommunication;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 投诉通信VO
 **/
@Data
@Schema(description = "投诉通信VO")
@NoArgsConstructor
public class OrderComplaintCommunicationVO extends OrderComplaintCommunication {

	private static final long serialVersionUID = -8460949951683122695L;

	public OrderComplaintCommunicationVO(String complainId, String content, String owner,
		String ownerName, String ownerId) {
		super(complainId, content, owner, ownerName, ownerId);
	}
}
