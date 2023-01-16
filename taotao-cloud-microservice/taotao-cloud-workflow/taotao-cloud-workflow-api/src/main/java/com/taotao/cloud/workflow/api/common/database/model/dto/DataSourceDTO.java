package com.taotao.cloud.workflow.api.common.database.model.dto;

import lombok.Data;

/**
 * 数据源参数传输对象
 * -- 注意：这里的参数dataSourceUtil是spring托管的全局唯一变量，此数据传输对象防止数据源互串
 */
@Data
public class DataSourceDTO extends DataSourceUtil{

    /**
     * 数据来源
     * 0：自身创建  1：配置  2：数据连接
     */
    private Integer dataSourceFrom;

    /**
     * 表名
     */
    private String tableName;

}
