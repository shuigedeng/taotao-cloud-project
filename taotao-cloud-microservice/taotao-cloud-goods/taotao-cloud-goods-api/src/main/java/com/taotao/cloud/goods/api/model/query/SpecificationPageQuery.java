package com.taotao.cloud.goods.api.model.query;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;

import lombok.*;

/**
 * 规格查询参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "规格查询参数")
public class SpecificationPageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = 8906820486037326039L;

	@Schema(description = "名称")
	private String specName;
}
