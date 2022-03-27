/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.api.vo.generator;

import lombok.Builder;
import lombok.Data;

/**
 * ColumnInfo
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:05:32
 */
@Data
@Builder
public class ColumnInfo {

	/**
	 * 数据库字段名称
	 **/
	private Object columnName;

	/**
	 * 允许空值
	 **/
	private Object isNullable;

	/**
	 * 数据库字段类型
	 **/
	private Object columnType;

	/**
	 * 数据库字段注释
	 **/
	private Object columnComment;

	/**
	 * 数据库字段键类型
	 **/
	private Object columnKey;

	/**
	 * 额外的参数
	 **/
	private Object extra;

	/**
	 * 查询 1:模糊 2：精确
	 **/
	private String columnQuery;

	/**
	 * 是否在列表显示
	 **/
	private String columnShow;
}
