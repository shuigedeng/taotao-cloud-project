package com.taotao.cloud.goods.adapter.model.dto.i18n;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 国际化信息唯一值 = 国际化标识 + 语言标签
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "国际化信息唯一值")
public class I18nDataUnique {

	private static final long serialVersionUID = 1L;

	@Schema(title = "国际化标识")
	private String code;

	@Schema(title = "语言标签")
	private String languageTag;

}
