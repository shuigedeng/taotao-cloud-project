package com.taotao.cloud.order.api.dto.order;

import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationBaseVO;
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
public class OrderComplaintCommunicationDTO {

	@Serial
	private static final long serialVersionUID = -8460949951683122695L;

	@Schema(description = "内容")
	private String content;
}
