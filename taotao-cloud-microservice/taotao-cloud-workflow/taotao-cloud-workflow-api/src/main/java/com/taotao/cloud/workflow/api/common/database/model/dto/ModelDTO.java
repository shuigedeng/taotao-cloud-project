package com.taotao.cloud.workflow.api.common.database.model.dto;

import java.sql.ResultSet;
import lombok.Data;

/**
 * 自定义模板参数对象
 *
 */
@Data
public class ModelDTO {

    public ModelDTO(ResultSet resultSet, DbBase dbBase){
        this.resultSet = resultSet;
        this.dbBase = dbBase;
    }

    /**
     * 结果集
     */
    private ResultSet resultSet;

    /**
     * 数据基类
     */
    private DbBase dbBase;

}
