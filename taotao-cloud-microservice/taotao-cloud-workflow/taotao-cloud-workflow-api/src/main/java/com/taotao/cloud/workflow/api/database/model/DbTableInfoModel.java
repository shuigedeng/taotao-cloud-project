package com.taotao.cloud.workflow.api.database.model;

import lombok.Data;

@Data
public class DbTableInfoModel {

	/**
	 * 表名
	 */
	private String table;

	/**
	 * 表类型
	 */
	private String tableType;

	/**
	 * 表注释
	 */
	private String comment;

	/**
	 * 主键字段
	 */
	private String primaryField;


}
