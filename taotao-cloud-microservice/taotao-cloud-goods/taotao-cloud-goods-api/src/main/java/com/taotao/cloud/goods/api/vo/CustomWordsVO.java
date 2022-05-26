package com.taotao.cloud.goods.api.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * 自定义单词签证官
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:49
 */
public record CustomWordsVO(

	Long id,

	/**
	 * 分词名称
	 */
	String name,

	/**
	 * 是否禁用
	 */
	Integer disabled
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

}
