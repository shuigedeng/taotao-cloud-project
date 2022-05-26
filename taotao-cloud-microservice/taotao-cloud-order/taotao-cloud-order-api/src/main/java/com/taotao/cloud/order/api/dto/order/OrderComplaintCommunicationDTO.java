package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 投诉通信VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Schema(description = "投诉通信VO")
public record OrderComplaintCommunicationDTO(
	@Schema(description = "内容")
	String content
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -8460949951683122695L;


}
