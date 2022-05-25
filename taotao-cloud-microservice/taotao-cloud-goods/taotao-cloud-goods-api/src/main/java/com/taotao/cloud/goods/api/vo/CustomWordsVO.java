package com.taotao.cloud.goods.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 自定义单词签证官
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomWordsVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

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
