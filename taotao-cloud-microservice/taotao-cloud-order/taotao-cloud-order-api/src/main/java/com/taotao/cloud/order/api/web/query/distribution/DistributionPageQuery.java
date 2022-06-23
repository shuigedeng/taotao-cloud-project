package com.taotao.cloud.order.api.web.query.distribution;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "售后搜索参数")
public class DistributionPageQuery extends PageParam {
	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	//结算单ID
	private String id;
}
