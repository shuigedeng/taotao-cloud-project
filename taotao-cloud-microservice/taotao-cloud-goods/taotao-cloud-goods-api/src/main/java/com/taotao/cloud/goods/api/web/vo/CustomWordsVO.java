package com.taotao.cloud.goods.api.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomWordsVO {

	private Long id;

	/**
	 * 分词名称
	 */
	private String name;

	/**
	 * 是否禁用
	 */
	private Integer disabled;
}
