package com.taotao.cloud.order.api.web.query.order;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 订单投诉搜索参数
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单投诉搜索参数")
public class OrderComplaintCommunicationPageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "投诉id")
	private String complainId;

	@Schema(description = "所属，买家/卖家")
	private String owner;

	@Schema(description = "对话所属名称")
	private String ownerName;

	@Schema(description = "对话所属id,卖家id/买家id")
	private String ownerId;

}
