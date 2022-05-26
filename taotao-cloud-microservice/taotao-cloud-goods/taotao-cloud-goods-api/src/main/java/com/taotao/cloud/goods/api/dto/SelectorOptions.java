package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 选择器选择
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:48
 */
@RecordBuilder
public record SelectorOptions(
	String name,

	String value,

	String url,

	List<SelectorOptions> otherOptions
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
