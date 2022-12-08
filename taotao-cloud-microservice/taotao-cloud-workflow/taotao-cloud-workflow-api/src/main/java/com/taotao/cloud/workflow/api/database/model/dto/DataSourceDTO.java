package com.taotao.cloud.workflow.api.database.model.dto;

import lombok.Data;

@Data
public class DataSourceDTO extends DataSourceUtil {

	/**
	 * 数据来源 0：自身创建  1：配置  2：数据连接
	 */
	private Integer dataSourceFrom;

	/**
	 * 表名
	 */
	private String tableName;

}
