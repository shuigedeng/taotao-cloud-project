package com.taotao.cloud.workflow.api.visiual;


import lombok.Data;

@Data
public class ColumnListField {

	/**
	 * 字段
	 */
	private String prop;
	/**
	 * 列名
	 */
	private String label;
	/**
	 * 对齐
	 */
	private String align;
	/**
	 * 宽度
	 */
	private String width;

	private String flowKey;

	/**
	 * 是否勾选
	 */
	private Boolean checked;
}
