package com.taotao.cloud.goods.api.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品属性索引
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:18:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsGoodsAttributeVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 4018042777559970062L;

	/**
	 * 属性参数：0->规格；1->参数
	 */
	private Integer type;

	/**
	 * 属性名称
	 */
	private String nameId;

	/**
	 * 属性名称
	 */
	private String name;

	/**
	 * 属性值
	 */
	private String valueId;

	/**
	 * 属性值
	 */
	private String value;

	/**
	 * 排序
	 */
	private Integer sort;
}
