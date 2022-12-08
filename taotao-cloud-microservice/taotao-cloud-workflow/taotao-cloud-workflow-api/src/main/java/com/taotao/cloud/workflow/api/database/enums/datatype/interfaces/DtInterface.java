package com.taotao.cloud.workflow.api.database.enums.datatype.interfaces;


public interface DtInterface {

	/**
	 * 获取数据库自身数据类型
	 *
	 * @return ignore
	 */
	String getDbFieldType();

	/**
	 * 获取当前数据类型模型
	 *
	 * @return ignore
	 */
	DataTypeModel getDataTypeModel();

}
