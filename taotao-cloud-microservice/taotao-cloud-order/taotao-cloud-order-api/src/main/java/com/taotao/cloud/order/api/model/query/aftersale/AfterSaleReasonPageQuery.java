package com.taotao.cloud.order.api.model.query.aftersale;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;

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

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "服务类型")
	private String serviceType;

}
