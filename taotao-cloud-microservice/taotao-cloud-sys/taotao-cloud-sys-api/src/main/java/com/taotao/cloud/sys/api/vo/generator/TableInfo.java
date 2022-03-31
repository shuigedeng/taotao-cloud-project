/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.api.vo.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TableInfo
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:05:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo {

	/**
	 * 表名称
	 */
	private Object tableName;

	/**
	 * 创建日期
	 */
	private Object createTime;

	/**
	 * 数据库引擎
	 */
	private Object engine;

	/**
	 * 编码集
	 */
	private Object coding;

	/**
	 * 备注
	 */
	private Object remark;
}
