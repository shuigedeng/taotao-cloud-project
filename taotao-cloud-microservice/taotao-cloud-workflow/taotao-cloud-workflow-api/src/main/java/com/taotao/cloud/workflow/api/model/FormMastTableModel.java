package com.taotao.cloud.workflow.api.model;

import lombok.Data;

@Data
public class FormMastTableModel {

	/**
	 * 表名
	 */
	private String table;
	/**
	 * 字段
	 */
	private String field;
	/**
	 * 原始字段
	 */
	private String vModel;

	private FormColumnModel mastTable;
}
