package com.taotao.cloud.store.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 店铺运费模板
 *
 * @since 2020/11/24 14:29
 */
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺运费模板")
public class FreightTemplateVO extends FreightTemplateBaseVO {

	private static final long serialVersionUID = 2422138942308945537L;

	@Schema(description = "运费详细规则")
	private List<FreightTemplateChildBaseVO> freightTemplateChildList;

}
