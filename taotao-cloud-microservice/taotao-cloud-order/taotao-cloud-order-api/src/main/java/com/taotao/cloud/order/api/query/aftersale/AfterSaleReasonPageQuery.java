package com.taotao.cloud.order.api.query.aftersale;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 售后原因搜索参数
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "售后原因搜索参数")
public class AfterSaleReasonPageQuery extends PageParam {

	@Schema(description = "服务类型")
	private String serviceType;

}
