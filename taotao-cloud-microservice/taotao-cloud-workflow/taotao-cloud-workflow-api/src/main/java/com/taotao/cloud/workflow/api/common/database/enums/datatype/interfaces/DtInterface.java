package com.taotao.cloud.workflow.api.common.database.enums.datatype.interfaces;


/**
 * 数据库数据类型接口
 *
 */
public interface DtInterface {

    /**
     * 获取数据库自身数据类型
     * @return ignore
     */
    String getDbFieldType();

    /**
     * 获取当前数据类型模型
     * @return ignore
     */
    DataTypeModel getDataTypeModel();

}
