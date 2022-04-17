package com.taotao.cloud.goods.biz.elasticsearch;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 商品属性索引
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsGoodsAttribute implements Serializable {

	@Serial
	private static final long serialVersionUID = 4018042777559970062L;

	/**
	 * 属性参数：0->规格；1->参数
	 */
	@Field(type = FieldType.Integer)
	private Integer type;

	/**
	 * 属性名称
	 */
	private String nameId;

	/**
	 * 属性名称
	 */
	@Field(type = FieldType.Text)
	private String name;

	/**
	 * 属性值
	 */
	@Field(type = FieldType.Text)
	private String valueId;

	/**
	 * 属性值
	 */
	@Field(type = FieldType.Text)
	private String value;

	/**
	 * 排序
	 */
	@Field(type = FieldType.Integer)
	private Integer sort;
}
