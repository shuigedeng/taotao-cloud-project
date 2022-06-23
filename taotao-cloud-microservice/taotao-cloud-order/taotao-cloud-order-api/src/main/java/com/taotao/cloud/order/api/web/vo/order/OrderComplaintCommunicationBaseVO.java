package com.taotao.cloud.order.api.web.vo.order;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 订单交易投诉通信表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "订单交易投诉通信VO")
public record OrderComplaintCommunicationBaseVO(

	@Schema(description = "投诉id")
	Long complainId,

	@Schema(description = "对话内容")
	String content,

	@Schema(description = "所属，买家/卖家")
	String owner,

	@Schema(description = "对话所属名称")
	String ownerName,

	@Schema(description = "对话所属id,卖家id/买家id")
	Long ownerId
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -2384351827382795547L;


}
