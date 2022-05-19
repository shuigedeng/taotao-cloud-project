package com.taotao.cloud.order.api.query.store;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "售后搜索参数")
public class StorePageQuery extends PageParam {
	//结算单ID
	private String id;
	//类型
	private String type;
}
