package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 参数属性选择器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:45
 */
@RecordBuilder
public record ParamOptions(
	String key,

	List<String> values
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
