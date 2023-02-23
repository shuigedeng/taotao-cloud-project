package com.taotao.cloud.store.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 店铺运费模板
 *
 */
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺运费模板")
public class FreightTemplateInfoVO extends FreightTemplateVO {

	private static final long serialVersionUID = 2422138942308945537L;

	@Schema(description = "运费详细规则")
	private List<FreightTemplateChildVO> freightTemplateChildList;

}
