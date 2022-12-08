package com.taotao.cloud.workflow.api.database.model.interfaces;

public interface DataSourceMod {

	/**
	 * 获取数据源参数传参对象 注意：此处方法不能命名为 get开头的名字， 会出现copy bean转换时候的错误
	 *
	 * @return ignore
	 */
	DataSourceDTO convertDTO();

	/**
	 * 多租户：获取数据源参数传参对象
	 *
	 * @param dbName 库名
	 * @return ignore
	 */
	DataSourceDTO convertDTO(String dbName);

}
