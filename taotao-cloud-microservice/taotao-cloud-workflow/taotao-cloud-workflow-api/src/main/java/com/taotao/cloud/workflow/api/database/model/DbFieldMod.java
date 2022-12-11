package com.taotao.cloud.workflow.api.database.model;

import lombok.Data;

@Data
public class DbFieldMod {

	/**
	 * 基础配置创建对象
	 */
	public DbFieldMod(String tableName, String columnLabel, String columnName,
		String columnTypeName,
		Integer columnTypeEncode, String columnValue, Boolean lowercaseFlag) {
		this.tableName = tableName;
		this.columnLabel = columnLabel;
		this.columnName = lowercaseFlag ? columnName.toLowerCase() : columnName;
		this.columnTypeName = columnTypeName;
		this.columnTypeEncode = columnTypeEncode;
		this.columnValue = columnValue;
	}

	public DbFieldMod() {
	}

	/**
	 * 字段名
	 */
	private String columnName;

	/**
	 * 字段别名
	 */
	private String columnLabel;

	/**
	 * 字段类型
	 */
	private String columnTypeName;

	/**
	 * 字段类型jdbc编码
	 */
	private Integer columnTypeEncode;

	/**
	 * 字段值
	 */
	private String columnValue;

	/**
	 * 所属表名
	 */
	private String tableName;

	/*================扩展属性=================*/

	/**
	 * 字段长度
	 */
	private String columnSize;

	/**
	 * 字段精度
	 */
	private String decimalDigits;

	/**
	 * 字段默认值
	 */
	private String columnDefault;

	/**
	 * 字段注解
	 */
	private String columnComment;

	/**
	 * 字段位置
	 */
	private String ordinalPosition;

	/**
	 * 自增标识
	 */
	private String isAutoIncrement;

	/**
	 * 主键标识 1：是主键 0：非主键
	 */
	private String primaryKeyMark;

	/**
	 * 允空 1:可以为空 0：不为空
	 */
	private Integer isNull;

}