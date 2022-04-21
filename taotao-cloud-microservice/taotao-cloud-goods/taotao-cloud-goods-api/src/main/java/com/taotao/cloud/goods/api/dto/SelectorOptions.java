package com.taotao.cloud.goods.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-18 21:59:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectorOptions {

	private String name;

	private String value;

	private String url;

	private List<SelectorOptions> otherOptions;


}
