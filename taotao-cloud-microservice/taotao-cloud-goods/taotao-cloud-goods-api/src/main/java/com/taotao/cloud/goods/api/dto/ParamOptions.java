package com.taotao.cloud.goods.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数属性选择器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:45
 */
public record ParamOptions(
	String key,

	List<String> values
	) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
