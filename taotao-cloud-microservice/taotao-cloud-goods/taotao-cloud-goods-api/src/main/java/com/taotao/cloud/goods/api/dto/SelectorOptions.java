package com.taotao.cloud.goods.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 选择器选择
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectorOptions implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	private String name;

	private String value;

	private String url;

	private List<SelectorOptions> otherOptions;


}
