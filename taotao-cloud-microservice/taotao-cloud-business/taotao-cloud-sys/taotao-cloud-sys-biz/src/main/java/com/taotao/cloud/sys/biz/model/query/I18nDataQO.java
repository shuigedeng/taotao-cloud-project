package com.taotao.cloud.sys.biz.model.query;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/**
 * 国际化信息 查询对象
 */
@Data
@Schema(title = "国际化信息查询对象")
public class I18nDataQO {

	private static final long serialVersionUID = 1L;

	@Parameter(description = "国际化标识")
	private String code;

	@Parameter(description = "文本信息")
	private String message;

	@Parameter(description = "语言标签")
	private String languageTag;

}
